package application.digitalbankingapplication.dto;

import application.digitalbankingapplication.model.AccountOperation;
import application.digitalbankingapplication.model.Customer;
import application.digitalbankingapplication.model.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SavingAccountDTO extends BankAccountDTO {
    private String bankAccountId;
    private double bankAccountBalance;
    private LocalDate bankAccountCreatedAt;
    private AccountStatus bankAccountStatus;
    private CustomerDTO customerDTO;
    private double savingAccountInterestRate;
    // private List<AccountOperation> accountOperationList;
}
