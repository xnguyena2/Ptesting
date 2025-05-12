package com.example.heroku.services;

import com.example.heroku.model.joinwith.PaymentTransactionJoinBuyer;
import com.example.heroku.model.repository.PaymentTransactionRepository;
import com.example.heroku.model.repository.join.PaymentTransactionJoinBuyerRepository;
import com.example.heroku.request.carousel.IDContainer;
import com.example.heroku.request.client.PackageID;
import com.example.heroku.request.transaction.PaymentTransactionBuyer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class PaymentTransation {

    @Autowired
    com.example.heroku.services.Buyer buyer;

    @Autowired
    PaymentTransactionRepository paymentTransactionRepository;

    @Autowired
    PaymentTransactionJoinBuyerRepository paymentTransactionJoinBuyerRepository;

    public Mono<PaymentTransactionBuyer> insertOrUpdate(PaymentTransactionBuyer paymentTransation) {
        paymentTransation.AutoFill();
        System.out.println("add or update transaction with buyer: " + paymentTransation.getTransaction_second_id() + ", group: " + paymentTransation.getGroup_id());
        return Mono.just(paymentTransation)
                .flatMap(this::saveBuyer)
                .flatMap(this::_insertOrUpdate)
                .then(Mono.just(paymentTransation));
    }

    public Mono<com.example.heroku.model.PaymentTransation> delete(IDContainer idContainer){
        return paymentTransactionRepository.deleteTransaction(idContainer.getGroup_id(), idContainer.getId());
    }

    public Mono<com.example.heroku.model.PaymentTransation> deleteOfPackgeID(IDContainer idContainer){
        return paymentTransactionRepository.deleteTransactionOfPackge(idContainer.getGroup_id(), idContainer.getId());
    }

    public Flux<PaymentTransactionBuyer> getAllTransactionByPackageID(IDContainer idContainer) {
        return paymentTransactionJoinBuyerRepository.getTransactionByPackageID(idContainer.getGroup_id(), idContainer.getId())
                .map(PaymentTransactionJoinBuyer::GeneratePaymentTransactionBuyer);
    }

    public Flux<PaymentTransactionBuyer> getAllTransactionBettwen(PackageID query) {
        return paymentTransactionJoinBuyerRepository.getStatictis(query.getGroup_id(), query.getFrom(), query.getTo())
                .map(PaymentTransactionJoinBuyer::GeneratePaymentTransactionBuyer);
    }

    public Flux<com.example.heroku.model.PaymentTransation> getAllCategory(IDContainer idContainer) {
        return paymentTransactionRepository.getAllCategory(idContainer.getGroup_id());
    }

    Mono<com.example.heroku.model.PaymentTransation> _insertOrUpdate(com.example.heroku.model.PaymentTransation paymentTransation) {
        return paymentTransactionRepository.saveTransaction(paymentTransation.getGroup_id(), paymentTransation.getTransaction_second_id(), paymentTransation.getDevice_id(),
                paymentTransation.getPackage_second_id(), paymentTransation.getPackage_second_id(), paymentTransation.getAction_type(), paymentTransation.getTransaction_type(), paymentTransation.getAmount(), paymentTransation.getCategory(),
                paymentTransation.getMoney_source(), paymentTransation.getNote(), paymentTransation.getStatus(), paymentTransation.getCreateat());
    }

    Mono<PaymentTransactionBuyer> saveBuyer(PaymentTransactionBuyer paymentTransactionBuyer) {

        com.example.heroku.model.Buyer buyerInfo = paymentTransactionBuyer.getBuyer();
        if (buyerInfo == null) {
            return Mono.just(paymentTransactionBuyer);
        }
        buyerInfo.AutoFill(paymentTransactionBuyer.getGroup_id());
        paymentTransactionBuyer.setDevice_id(buyerInfo.getDevice_id());
        float totalPrice = 0;
        float realPrice = 0;
        float discount = 0;
        float ship = 0;
        int point = 0;
        return buyer.insertOrUpdate(buyerInfo, totalPrice, realPrice, ship, discount, point).then(Mono.just(paymentTransactionBuyer));
    }
}
