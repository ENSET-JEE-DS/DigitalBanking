package application.digitalbankingapplication.dto;

import java.time.LocalDate;

import application.digitalbankingapplication.model.enums.OperationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountOperationDTO {
    private Long id;
    private LocalDate operationDate;
    private Double operationAmount;
    private OperationType operationType;
    private BankAccountDTO bankAccountDTO;
    private String description;
}
