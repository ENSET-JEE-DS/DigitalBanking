package application.digitalbankingapplication.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import application.digitalbankingapplication.dto.BankAccountDTO;
import application.digitalbankingapplication.model.BankAccount;

@Mapper(componentModel = "spring")
public interface BankAccountMapper {
    @Mapping(source = "customer", target = "customerDTO")
    BankAccountDTO bankAccountToBankAccountDTO(BankAccount bankAccount);

    // BankAccount bankAccountDTOToBankAccount(BankAccountDTO bankAccountDTO);
}
