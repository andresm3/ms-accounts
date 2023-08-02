package com.bootcamp.banking.accounts;

import static org.mockito.Mockito.when;

import com.bootcamp.banking.accounts.domain.models.Account;
import com.bootcamp.banking.accounts.domain.models.Client;
import com.bootcamp.banking.accounts.application.impl.AccountUseCasesImpl;
import com.bootcamp.banking.accounts.application.impl.CreditUseCasesImpl;
import com.bootcamp.banking.accounts.infraestructure.repository.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


@ExtendWith(SpringExtension.class)
class AccountUseCasesTest {

  @Mock
  private AccountRepository accountRepository;

  @Mock
  private CreditUseCasesImpl creditUseCases;

  @InjectMocks
  private AccountUseCasesImpl accountUseCases;


  @Test
  void testGetAccountsByClientDocumentNumber() {
    Account account = new Account();
    Client client = new Client();
    client.setDocumentNumber("00000001");
    client.setFirstName("Juan");
    account.setClient(client);
    account.setNumber("1234567890");
    account.setDebitCard("4420652012504888");

    Account account1 = new Account();
    Client client1 = new Client();
    client1.setDocumentNumber("00000001");
    client.setFirstName("Juan");
    account1.setClient(client1);
    account1.setNumber("1234567891");
    account1.setDebitCard("4420652012504888");

    var fluxAccount = Flux.just(account, account1);
    when(accountRepository.findByClientDocumentNumber("00000001")).thenReturn(fluxAccount);

    var accounts = accountUseCases.getAccountsByClientDocumentNumber("00000001");
    StepVerifier
        .create(accounts)
        .expectSubscription()
        .consumeNextWith(ac -> {
          Assertions.assertNotNull(ac);
          Assertions.assertEquals("1234567890", ac.getNumber());
        })
        .consumeNextWith(ac -> {
          Assertions.assertNotNull(ac);
          Assertions.assertEquals("1234567891", ac.getNumber());
        })
        .verifyComplete();
  }

  @Test
  void testGetAccountById() {
    String id = "123456";

    Client client = new Client();
    client.setDocumentNumber("00000001");
    client.setFirstName("Juan");

    Account account = new Account();
    account.setId(id);
    account.setClient(client);
    account.setNumber("1234567890");
    account.setDebitCard("4420652012504888");

    var monoAccount = Mono.just(account);
    when(accountRepository.findById(id)).thenReturn(monoAccount);

    var resAccount = accountUseCases.getAccountById(id);
    StepVerifier
        .create(resAccount)
        .expectSubscription()
        .consumeNextWith(ac -> {
          Assertions.assertNotNull(ac);
          Assertions.assertEquals("1234567890", ac.getNumber());
        })
        .verifyComplete();
  }
}
