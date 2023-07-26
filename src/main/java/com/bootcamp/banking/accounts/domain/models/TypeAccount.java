package com.bootcamp.banking.accounts.domain.models;

import com.bootcamp.banking.accounts.domain.dto.TypeAccountRequest;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypeAccount {

  private int option;
  @Field(targetType = FieldType.DECIMAL128)
  private BigDecimal maintenance;
  private Integer maxTransactions;
  @Field(targetType = FieldType.DECIMAL128)
  private BigDecimal commission;
  private Integer day;

  public TypeAccount(TypeAccountRequest request) {
    option = request.getOption();
    maxTransactions = request.getMaxTransactions();
    maintenance = request.getMaintenance();
    commission = request.getCommission();
    day = request.getDay();
  }
}
