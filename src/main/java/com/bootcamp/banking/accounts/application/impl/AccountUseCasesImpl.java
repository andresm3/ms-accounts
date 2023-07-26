package com.bootcamp.banking.accounts.application.impl;

import com.bootcamp.banking.accounts.application.AccountUseCases;
import com.bootcamp.banking.accounts.application.CreditUseCases;
import com.bootcamp.banking.accounts.application.exceptions.CustomInformationException;
import com.bootcamp.banking.accounts.application.exceptions.CustomNotFoundException;
import com.bootcamp.banking.accounts.application.utils.Constants;
import com.bootcamp.banking.accounts.application.utils.Validations;
import com.bootcamp.banking.accounts.domain.models.Account;
import com.bootcamp.banking.accounts.infraestructure.repository.AccountRepository;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AccountUseCasesImpl implements AccountUseCases {

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private CreditUseCases creditUseCases;

  private static final String FLUX_NOT_FOUND_MESSAGE = "Data not found";
  private static final String MONO_NOT_FOUND_MESSAGE = "Account not found";

  @Override
  public Flux<Account> getAll() {
    return accountRepository.findAll();
  }

  @Override
  public Mono<Account> createAccount(Account account) {
    return accountRepository.findByNumber(account.getNumber())
        .doOnNext(ac -> {
          throw new CustomInformationException("Account number has already been created");
        })
        .switchIfEmpty(Validations.validateFields(account)
            .flatMap(a -> accountRepository
                .countByClientDocumentNumberAndType(account.getClient().getDocumentNumber(),
                    account.getTypeAccount().getOption())
                .flatMap(co -> Validations.validateCreateAccount(co, account))
                .flatMap(this::checkIfRequiresCreditCard)
                .flatMap(this::checkIfHasDebt)
                .flatMap(this::setPosition)
                .flatMap(ac -> accountRepository.save(ac)
                    .map(c -> {
//                      logger.info("Created a new id = {} for the account with number= {}",
//                          account.getId(), account.getNumber());
                      System.out.println(">>Created a new id = {} for the account with number=> Saving " + account.getNumber());
                      return c;
                    })))
        );
  }

  @Override
  public Mono<Account> getAccountById(String id) {
    return accountRepository.findById(id)
        .switchIfEmpty(Mono.error(new CustomNotFoundException(MONO_NOT_FOUND_MESSAGE)));

  }

  @Override
  public Flux<Account> getAccountsByClientDocumentNumber(String documentNumber) {
    return accountRepository.findByClientDocumentNumber(documentNumber)
        .switchIfEmpty(Flux.error(new CustomNotFoundException(FLUX_NOT_FOUND_MESSAGE)));
  }

  @Override
  public Flux<Account> getAccountsByClientDebitCard(String debitCard) {
    return accountRepository.findByDebitCard(debitCard)
        .switchIfEmpty(Flux.error(new CustomNotFoundException(FLUX_NOT_FOUND_MESSAGE)));

  }

  @Override
  public Mono<BigDecimal> getTotalBalanceByDebitCard(String debitCard) {
    return accountRepository.findByDebitCard(debitCard)
        .map(Account::getBalance)
        .reduce(BigDecimal.ZERO, BigDecimal::add)
        .switchIfEmpty(Mono.error(new CustomNotFoundException(FLUX_NOT_FOUND_MESSAGE)));

  }

  private Mono<Account> checkIfRequiresCreditCard(Account account) {
    if (account.getClient().getType() == Constants.ClientType.PERSONAL
        && account.getClient().getProfile() == Constants.ClientProfile.VIP
        && account.getTypeAccount().getOption() == Constants.AccountType.SAVING
        || account.getClient().getType() == Constants.ClientType.BUSINESS
        && account.getClient().getProfile() == Constants.ClientProfile.PYME) {
      return creditUseCases.consumeClientOwnsCreditCard(
              account.getClient().getDocumentNumber())
          .switchIfEmpty(
              Mono.error(new CustomInformationException("The account type requires "
                  + "that the client owns a credit card")))
          .flatMap(cr -> Mono.just(account));
    } else {
      System.out.println(">>checkIfRequiresCreditCard> ELSE ");
      return Mono.just(account);
    }
  }

  private Mono<Account> checkIfHasDebt(Account account) {
    return creditUseCases.checkIfClientHasDebts(account.getClient().getDocumentNumber())
        .flatMap(res -> {
          if (Boolean.TRUE.equals(res)) {
            return Mono.error(new CustomInformationException("You cannot create an account "
                + "because you have a credit debt"));
          } else {
            return Mono.just(account);
          }
        });
  }

  private Mono<Account> setPosition(Account account) {
    return accountRepository.getLastByDebitCard(account.getDebitCard())
        .map(ac -> {
          int pos = ac.getPosition() + 1;
          account.setPosition(pos);
          return account;
        })
        .switchIfEmpty(Mono.just(account));
  }
}
