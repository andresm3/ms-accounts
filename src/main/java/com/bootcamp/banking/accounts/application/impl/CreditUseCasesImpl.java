package com.bootcamp.banking.accounts.application.impl;

import com.bootcamp.banking.accounts.application.CreditUseCases;
import com.bootcamp.banking.accounts.domain.dto.CreditResponse;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class CreditUseCasesImpl implements CreditUseCases {

  @Autowired
  private WebClient.Builder webClient;

  @Override
  public Mono<CreditResponse> consumeClientOwnsCreditCard(String documentNumber) {
    String urlCredit = "http://localhost:8083/credits";
    return webClient
        .build()
        .get()
        .uri(urlCredit + "/clientOwnsCard/{documentNumber}", documentNumber)
        .retrieve()
        .bodyToMono(CreditResponse.class)
        .map(credit -> credit)
        .switchIfEmpty(Mono.empty());
  }

  @Override
  public Mono<BigDecimal> checkIfClientHasDebts(String documentNumber) {
    String urlCredit = "http://localhost:8083/credits";
    return webClient
        .build()
        .get()
        .uri(urlCredit + "/check/debts/{documentNumber}", documentNumber)
        .retrieve()
        .bodyToMono(BigDecimal.class);
  }
}
