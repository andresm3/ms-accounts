package com.bootcamp.banking.accounts.infraestructure.repository;

import com.bootcamp.banking.accounts.domain.models.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Custom account repository.
 */
public interface CustomAccountRepository {
  Flux<Account> findByClientFirstName(String firstName);

  Flux<Account> findByClientFirstNameAndLastName(String firstName, String lastName);

  Flux<Account> findByClientDocumentNumber(String documentNumber);

  Flux<Account> findByDebitCard(String debitCard);

  Mono<Account> findByNumberAndClientDocumentNumber(String number, String documentNumber);

  Mono<Long> countByClientDocumentNumberAndType(String documentNumber, Integer option);

  Mono<Account> getLastByDebitCard(String debitCard);

  Mono<Account> findByClientDocumentNumberAndPosition(String documentNumber);
}
