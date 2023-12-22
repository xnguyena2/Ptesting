package com.example.heroku.services;

import com.example.heroku.model.repository.PaymentTransactionRepository;
import com.example.heroku.request.carousel.IDContainer;
import com.example.heroku.request.client.PackageID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class PaymentTransation {

    @Autowired
    PaymentTransactionRepository paymentTransactionRepository;

    public Mono<com.example.heroku.model.PaymentTransation> insertOrUpdate(com.example.heroku.model.PaymentTransation paymentTransation) {
        paymentTransation.AutoFill();
        return paymentTransactionRepository.saveTransaction(paymentTransation.getGroup_id(), paymentTransation.getTransaction_second_id(), paymentTransation.getDevice_id(),
                paymentTransation.getPackage_second_id(), paymentTransation.getTransaction_type(), paymentTransation.getAmount(), paymentTransation.getCategory(),
                paymentTransation.getMoney_source(), paymentTransation.getNote(), paymentTransation.getStatus(), paymentTransation.getCreateat());
    }

    public Mono<com.example.heroku.model.PaymentTransation> delete(IDContainer idContainer){
        return paymentTransactionRepository.deleteTransaction(idContainer.getGroup_id(), idContainer.getId());
    }

    public Flux<com.example.heroku.model.PaymentTransation> getAllTransactionByPackageID(IDContainer idContainer) {
        return paymentTransactionRepository.getTransactionByPackageID(idContainer.getGroup_id(), idContainer.getId());
    }

    public Flux<com.example.heroku.model.PaymentTransation> getAllTransactionBettwen(PackageID query) {
        return paymentTransactionRepository.getStatictis(query.getGroup_id(), query.getFrom(), query.getTo());
    }
}
