package com.example.heroku.services;

import com.example.heroku.model.ProductImport;
import com.example.heroku.model.repository.DebtTransactionRepository;
import com.example.heroku.model.repository.StatisticDebtOfBuyerRepository;
import com.example.heroku.model.statistics.DebtOfBuyer;
import com.example.heroku.request.carousel.IDContainer;
import com.example.heroku.request.client.PackageID;
import com.example.heroku.request.client.UserID;
import com.example.heroku.request.debt.PendingDebtOfBuyer;
import com.example.heroku.response.DebtImportAndUserPackge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class DebtTransation {

    @Autowired
    DebtTransactionRepository debtTransactionRepository;

    @Autowired
    StatisticDebtOfBuyerRepository statisticDebtOfBuyerRepository;

    @Autowired
    UserPackage userPackage;

    @Autowired
    GroupImport groupImport;

    public Mono<com.example.heroku.model.DebtTransation> insertOrUpdate(com.example.heroku.model.DebtTransation paymentTransation) {
        paymentTransation.AutoFill();
        return debtTransactionRepository.saveTransaction(paymentTransation.getGroup_id(), paymentTransation.getTransaction_second_id(), paymentTransation.getDevice_id(),
                paymentTransation.getAction_id(), paymentTransation.getAction_type(), paymentTransation.getTransaction_type(), paymentTransation.getAmount(), paymentTransation.getCategory(),
                paymentTransation.getMoney_source(), paymentTransation.getNote(), paymentTransation.getStatus(), paymentTransation.getCreateat());
    }

    public Mono<com.example.heroku.model.DebtTransation> delete(IDContainer idContainer) {
        return debtTransactionRepository.deleteTransaction(idContainer.getGroup_id(), idContainer.getId());
    }

    public Mono<com.example.heroku.model.DebtTransation> deleteOfPackgeID(IDContainer idContainer) {
        return debtTransactionRepository.deleteTransactionOfPackge(idContainer.getGroup_id(), idContainer.getId());
    }

    public Flux<com.example.heroku.model.DebtTransation> getAllTransactionByPackageID(IDContainer idContainer) {
        return debtTransactionRepository.getTransactionByPackageID(idContainer.getGroup_id(), idContainer.getId());
    }

    public Flux<com.example.heroku.model.DebtTransation> getAllTransactionBettwen(PackageID query) {
        return debtTransactionRepository.getStatictis(query.getGroup_id(), query.getFrom(), query.getTo());
    }

    public Flux<com.example.heroku.model.DebtTransation> getAllTransaction(UserID query) {
        long id = query.getAfter_id();
        if (id > 0) {
            return debtTransactionRepository.getAllDebtAfterID(query.getGroup_id(), id, query.getPage(), query.getSize());
        }
        return debtTransactionRepository.getAllDebt(query.getGroup_id(), query.getPage(), query.getSize());
    }

    public Flux<com.example.heroku.model.DebtTransation> getAllTransactionOfBuyer(UserID query) {
        long id = query.getAfter_id();
        if (id > 0) {
            return debtTransactionRepository.getAllDebtAfterIDOfBuyer(query.getGroup_id(), query.getId(), id, query.getPage(), query.getSize());
        }
        return debtTransactionRepository.getAllDebtOfBuyer(query.getGroup_id(), query.getId(), query.getPage(), query.getSize());
    }

    public Flux<com.example.heroku.model.DebtTransation> getAllCategory(IDContainer idContainer) {
        return debtTransactionRepository.getAllCategory(idContainer.getGroup_id());
    }

    public Flux<DebtOfBuyer> getDebtOfAllBuyer(IDContainer idContainer) {
        return statisticDebtOfBuyerRepository.getIncomeOutComeAllBuyer(idContainer.getGroup_id())
                .map(DebtOfBuyer::new);
    }

    public Flux<DebtOfBuyer> getDebtOfAllBuyerInner(IDContainer idContainer) {
        return statisticDebtOfBuyerRepository.getIncomeOutComeAllBuyerInner(idContainer.getGroup_id())
                .map(DebtOfBuyer::new);
    }

    public Mono<DebtImportAndUserPackge> getAllDebtGroupImportAndPackageofBuyer(PendingDebtOfBuyer pendingDebtOfBuyer) {
        String groupID = pendingDebtOfBuyer.getGroup_id();
        String deviceID = pendingDebtOfBuyer.getDevice_id();
        com.example.heroku.model.DebtTransation.TType type = pendingDebtOfBuyer.getType();
        return Mono.just(DebtImportAndUserPackge.builder().build())
                .flatMap(debtImportAndUserPackge -> {
                    if (type == null || type == com.example.heroku.model.DebtTransation.TType.INCOME) {
                        return userPackage.GetPackageByDebtOfUser(groupID, deviceID).collectList().map(debtImportAndUserPackge::setPackage);
                    }
                    return Mono.just(debtImportAndUserPackge);
                })
                .flatMap(debtImportAndUserPackge -> {
                    if (type == null) {
                        return groupImport.GetAllDebtOfSupplier(groupID, ProductImport.ImportType.UN_KNOW, deviceID).collectList().map(debtImportAndUserPackge::setImport);
                    }
                    if (type == com.example.heroku.model.DebtTransation.TType.INCOME) {
                        return groupImport.GetAllDebtOfSupplier(groupID, ProductImport.ImportType.IMPORT, deviceID).collectList().map(debtImportAndUserPackge::setImport);
                    }
                    if (type == com.example.heroku.model.DebtTransation.TType.OUTCOME) {
                        return groupImport.GetAllDebtOfSupplier(groupID, ProductImport.ImportType.EXPORT, deviceID).collectList().map(debtImportAndUserPackge::setImport);
                    }
                    return Mono.just(debtImportAndUserPackge);
                });
    }
}
