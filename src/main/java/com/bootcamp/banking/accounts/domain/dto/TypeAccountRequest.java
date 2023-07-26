package com.bootcamp.banking.accounts.domain.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TypeAccount object.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypeAccountRequest {
  //@NotNull(message = "Field option for typeAccount must be required")
  private Integer option;
  private BigDecimal maintenance;
  //@NotNull(message = "Field maxTransactions for typeAccount must be required")
  //@Min(value = 1, message = "Field maxTransactions for typeAccount must be at least 1")
  private Integer maxTransactions;
  //@NotNull(message = "Field commission for typeAccount must be required")
  private BigDecimal commission;
  private Integer day;
}
