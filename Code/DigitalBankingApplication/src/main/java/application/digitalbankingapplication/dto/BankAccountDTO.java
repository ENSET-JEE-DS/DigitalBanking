package application.digitalbankingapplication.dto;

import java.time.LocalDate;
import application.digitalbankingapplication.model.enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountDTO {
    private String bankAccountId;
    private double bankAccountBalance;
    private LocalDate bankAccountCreatedAt;
    private AccountStatus bankAccountStatus;
    private CustomerDTO customerDTO;
    // private List<AccountOperation> accountOperationList;
}
