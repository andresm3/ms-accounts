package com.bootcamp.banking.accounts.infraestructure.rest;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

import com.bootcamp.banking.accounts.application.AccountUseCases;
import com.bootcamp.banking.accounts.domain.models.Account;
import java.math.BigDecimal;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountResource {

  @Autowired
  private AccountUseCases accountUseCases;
  @PostMapping("/create")
  @ResponseStatus(CREATED)
  public Mono<Account> create(@Valid @RequestBody Account request) {
    return accountUseCases.createAccount(request);
  }
  @GetMapping("/id/{id}")
  public Mono<Account> findById(@PathVariable String id) {
    System.out.println(">>id>>>>>>> " + id);
    return accountUseCases.getAccountById(id);
  }

  @GetMapping("/number/{number}")
  public Mono<Account> findByNumber(@PathVariable String number) {
    System.out.println(">>>number>>>>>> " + number);
    return accountUseCases.getAccountByNumber(number);
  }
  @GetMapping(value = "/client/documentNumber/{documentNumber}")
  public Flux<Account> listByClientDocumentNumber(@PathVariable String documentNumber) {
    return accountUseCases.getAccountsByClientDocumentNumber(documentNumber);
  }

  @GetMapping(value = "/client/main/documentNumber/{documentNumber}")
  public Mono<Account> findMainAccountByClientDocumentNumber(@PathVariable String documentNumber) {
    return accountUseCases.getMainAccountByClientDocumentNumber(documentNumber);
  }

  @GetMapping(value = "/debitCard/{debitCard}")
  public Flux<Account> listByDebitCard(@PathVariable String debitCard) {
    return accountUseCases.getAccountsByClientDebitCard(debitCard);
  }

  @GetMapping("/totalBalance/{debitCard}")
  public Mono<BigDecimal> getTotalBalanceByDebitCard(@PathVariable String debitCard) {
    return accountUseCases.getTotalBalanceByDebitCard(debitCard);
  }

  @GetMapping(value = "/all", produces = TEXT_EVENT_STREAM_VALUE)
  public Flux<Account> listAll() {
    return accountUseCases.getAll();
  }

  @PutMapping("/balance/{id}/amount/{amount}")
  public Mono<Account> updateBalance(@PathVariable String id,
      @PathVariable BigDecimal amount) {
    return accountUseCases.updateBalance(id, amount);
  }
}
