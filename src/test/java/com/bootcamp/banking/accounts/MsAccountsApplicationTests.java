package com.bootcamp.banking.accounts;

import static org.mockito.Mockito.when;

import com.bootcamp.banking.accounts.application.AccountUseCases;
import com.bootcamp.banking.accounts.domain.models.Account;
import com.bootcamp.banking.accounts.domain.models.Client;
import com.bootcamp.banking.accounts.infraestructure.rest.AccountResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@WebFluxTest(AccountResource.class)
class MsAccountsApplicationTests {

	@Autowired
	private WebTestClient webTestClient;

	@MockBean
	private AccountUseCases accountUseCases;
	@Test
	void testGetAccountsByClientDocumentNumber() {
		Account account = new Account();
		Client client = new Client();
		client.setDocumentNumber("00000001");
		account.setClient(client);
		account.setNumber("1234567890");
		account.setDebitCard("4420652012504888");

		Account account1 = new Account();
		Client client1 = new Client();
		client1.setDocumentNumber("00000001");
		account1.setClient(client1);
		account1.setNumber("1234567891");
		account1.setDebitCard("4420652012504888");

		Flux<Account> fluxAccount = Flux.just(account, account1);
		when(accountUseCases.getAccountsByClientDocumentNumber("00000001")).thenReturn(fluxAccount);

		var responseBody = webTestClient
				.get()
				.uri("/accounts/client/documentNumber/00000001")
				.exchange()
				.expectStatus().isOk()
				.returnResult(Account.class)
				.getResponseBody();

		StepVerifier
				.create(responseBody)
				.expectSubscription()
				.expectNext(account)
				.expectNext(account1)
				.verifyComplete();
	}

}
