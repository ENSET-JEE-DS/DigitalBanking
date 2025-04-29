package application.digitalbankingapplication.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@DiscriminatorValue("CA")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class CurrentAccount extends BankAccount {
    private double currentAccountOverDraft;
}
