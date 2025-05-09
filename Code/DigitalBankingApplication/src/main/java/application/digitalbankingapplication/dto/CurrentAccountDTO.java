package application.digitalbankingapplication.dto;

import application.digitalbankingapplication.model.enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CurrentAccountDTO extends BankAccountDTO {
    private String bankAccountId;
    private double bankAccountBalance;
    private LocalDate bankAccountCreatedAt;
    private AccountStatus bankAccountStatus;
    private CustomerDTO customerDTO;
    private double currentAccountOverDraft;
    // private List<AccountOperation> accountOperationList;
}
