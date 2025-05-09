package application.digitalbankingapplication.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import application.digitalbankingapplication.dto.AccountOperationDTO;
import application.digitalbankingapplication.model.AccountOperation;


@Mapper(componentModel = "spring")
public interface AccountOperationMapper {
    @Mapping(source = "bankAccount", target = "bankAccountDTO")
    AccountOperationDTO accountOperationToAccountOperationDTO(AccountOperation accountOperation);

    // BankAccount bankAccountDTOToBankAccount(BankAccountDTO bankAccountDTO);
}
