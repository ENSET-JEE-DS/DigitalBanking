package application.digitalbankingapplication.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    private String customerName;
    private String customerEmail; 
    @Builder.Default
    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
    private List<BankAccount> bankAccountList = new ArrayList<>();
}
