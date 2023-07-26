package com.bootcamp.banking.accounts.domain.dto;

import com.bootcamp.banking.accounts.domain.models.Client;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Credit object.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditResponse {
  private String id;
  private Client client;
  private String number;
  private boolean active;
  private int type;
  private BigDecimal creditTotal;
  private BigDecimal creditBalance;
}
