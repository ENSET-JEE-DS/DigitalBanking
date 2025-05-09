package application.digitalbankingapplication.model;

import java.time.LocalDate;
import java.util.List;

import application.digitalbankingapplication.model.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "account_type", discriminatorType = DiscriminatorType.STRING, length = 4)
public abstract class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String bankAccountId;
    private double bankAccountBalance;
    private LocalDate bankAccountCreatedAt;
    @Enumerated(EnumType.STRING)
    private AccountStatus bankAccountStatus;
    @ManyToOne(fetch = FetchType.EAGER)
    private Customer customer;
    @OneToMany(mappedBy = "bankAccount")
    private List<AccountOperation> accountOperationList;
}
