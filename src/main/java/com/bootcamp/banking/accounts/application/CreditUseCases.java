package com.bootcamp.banking.accounts.application;

import com.bootcamp.banking.accounts.domain.dto.CreditResponse;
import java.math.BigDecimal;
import reactor.core.publisher.Mono;

public interface CreditUseCases {

  Mono<CreditResponse> consumeClientOwnsCreditCard(String number);

  Mono<BigDecimal> checkIfClientHasDebts(String documentNumber);
}
