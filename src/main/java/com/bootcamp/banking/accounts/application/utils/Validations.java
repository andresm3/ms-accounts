package com.bootcamp.banking.accounts.application.utils;

import com.bootcamp.banking.accounts.application.exceptions.CustomInformationException;
import com.bootcamp.banking.accounts.domain.models.Account;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

/**
 * Validation class.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Validations {
  /**
   * Validate fields of account.
   *
   * @param account Account object
   */
  public static Mono<Account> validateFields(Account account) {
    if (account.getTypeAccount().getOption() == Constants.AccountType.SAVING) {
      System.out.println(">>validateFields> Saving ");
      return validateForSavingAccount(account);
    } else if (account.getTypeAccount().getOption() == Constants.AccountType.CHECKING) {
      return validateForCheckingAccount(account);
    } else {
      return validateForFixedTermAccount(account);
    }
  }

  /**
   * Validate before creating an account.
   *
   * @param count   Number of accounts per type
   * @param account Account object
   */
  public static Mono<Account> validateCreateAccount(Long count, Account account) {
    if (account.getClient().getType() == Constants.ClientType.PERSONAL && count > 0) {
      return Mono.error(new
          CustomInformationException("The type of client can only have 1 account of this type"));
    } else if (account.getClient().getType() == Constants.ClientType.BUSINESS
        && account.getTypeAccount().getOption() != Constants.AccountType.CHECKING) {
      return Mono.error(new
          CustomInformationException("The type of client can only have multiple current accounts"));
    } else if (account.getClient().getType() == Constants.ClientType.BUSINESS
        && (account.getHolders() == null || account.getHolders().isEmpty())) {
      return Mono.error(new
          CustomInformationException("The account type requires at least one holder"));
    }
    return Mono.just(account);
  }

  private static Mono<Account> validateForSavingAccount(Account account) {
    if (account.getTypeAccount().getMaxTransactions() == null) {
      return Mono.error(new CustomInformationException("Field maxTransactions for "
          + "typeAccount must be required"));
    } else if (account.getTypeAccount().getMaxTransactions() == 0) {
      return Mono.error(new CustomInformationException("Field maxTransactions for "
          + "typeAccount must be at least 1"));
    }
    account.getTypeAccount().setMaintenance(null);
    return Mono.just(account);
  }

  private static Mono<Account> validateForCheckingAccount(Account account) {
    if (account.getClient().getProfile() == Constants.ClientProfile.PYME) {
      account.getTypeAccount().setMaintenance(null);
    } else {
      if (account.getTypeAccount().getMaintenance() == null) {
        return Mono.error(new CustomInformationException("Field maintenance for typeAccount "
            + "must be required"));
      } else if (account.getTypeAccount().getMaintenance().compareTo(BigDecimal.ZERO) <= 0) {
        return Mono.error(new CustomInformationException("Field maintenance for typeAccount "
            + "must be greater than 0"));
      }
    }
    account.getTypeAccount().setMaxTransactions(null);
    return Mono.just(account);
  }

  private static Mono<Account> validateForFixedTermAccount(Account account) {
    if (account.getTypeAccount().getDay() == null) {
      return Mono.error(new CustomInformationException("Field day for typeAccount "
          + "must be required"));
    } else {
      int lastDayOfMonth = LocalDate
          .now()
          .withDayOfMonth(LocalDate.now().lengthOfMonth()).getDayOfMonth();
      if (account.getTypeAccount().getDay() <= 0
          || account.getTypeAccount().getDay() > lastDayOfMonth) {
        return Mono.error(new CustomInformationException("Field day for typeAccount "
            + "must be between 1 and " + lastDayOfMonth));
      }
    }
    account.getTypeAccount().setMaintenance(null);
    account.getTypeAccount().setMaxTransactions(1);
    return Mono.just(account);
  }
}
