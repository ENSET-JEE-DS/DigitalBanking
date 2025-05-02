package application.digitalbankingapplication.mapper;

import org.mapstruct.Mapper;

import application.digitalbankingapplication.dto.BankAccountDTO;
import application.digitalbankingapplication.model.BankAccount;

@Mapper(componentModel = "spring")
public interface BankAccountMapper {
    BankAccountDTO bankAccountToBankAccountDTO(BankAccount bankAccount);

    BankAccount bankAccountDTOToBankAccount(BankAccountDTO bankAccountDTO);
}
