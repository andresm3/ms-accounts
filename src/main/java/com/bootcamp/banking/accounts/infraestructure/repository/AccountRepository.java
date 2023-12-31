package com.bootcamp.banking.accounts.infraestructure.repository;

import com.bootcamp.banking.accounts.domain.models.Account;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;


@Repository
public interface AccountRepository extends ReactiveMongoRepository<Account, String>, CustomAccountRepository {

  Mono<Account> findByNumber(String number);
  Mono<Account> findById(String number);
}