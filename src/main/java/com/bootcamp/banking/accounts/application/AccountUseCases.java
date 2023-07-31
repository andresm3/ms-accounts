package com.bootcamp.banking.accounts.application;

import com.bootcamp.banking.accounts.domain.models.Account;
import java.math.BigDecimal;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountUseCases {

  Flux<Account> getAll();
  Mono<Account> createAccount(Account account);
  Mono<Account> getAccountById(String id);
  Mono<Account> getAccountByNumber(String number);
  Flux<Account> getAccountsByClientDocumentNumber(String documentNumber);
  Flux<Account> getAccountsByClientDebitCard(String debitCard);
  Mono<BigDecimal> getTotalBalanceByDebitCard(String debitCard);

}
