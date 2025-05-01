package com.example.heroku.services;

import com.example.heroku.model.repository.UserPaySoDiRepository;
import com.example.heroku.response.Format;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.ok;

@Component
public class UserPaySoDi {
    @Autowired
    private UserPaySoDiRepository userPaySoDiRepository;

    public Mono<ResponseEntity<Format>> createTransaction(@Valid @ModelAttribute com.example.heroku.model.UserPaySoDi payment) {
        payment.AutoFill();
        return
                userPaySoDiRepository.saveTransaction(payment.getGroup_id(), payment.getAmount(), payment.getNote(), payment.getPlan(), payment.getBonus(), payment.getCreateat())
                        .flatMap(userRepository ->
                                Mono.just(ok(Format.builder().response(payment.getGroup_id()).build()))
                        );
    }

    public Flux<com.example.heroku.model.UserPaySoDi> findByGroupId(String groupid) {
        return userPaySoDiRepository.findByGroupId(groupid);
    }

    public Mono<com.example.heroku.model.UserPaySoDi> deleteByID(String groupid, long id) {
        return userPaySoDiRepository.deleteById(groupid, id);
    }
}
