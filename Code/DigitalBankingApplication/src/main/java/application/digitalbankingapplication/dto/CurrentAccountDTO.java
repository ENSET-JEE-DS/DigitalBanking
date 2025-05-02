package application.digitalbankingapplication.dto;

import java.time.LocalDate;

import application.digitalbankingapplication.model.enums.AccountStatus;
import lombok.Data;

@Data
public class CurrentAccountDTO {
    
    private String bankAccountId;
    private double bankAccountBalance;
    private LocalDate bankAccountCreatedAt;
    private AccountStatus bankAccountStatus;
    private CustomerDTO customer;
    private double overDraft;
}
