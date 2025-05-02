package application.digitalbankingapplication.mapper;

import org.mapstruct.Mapper;

import application.digitalbankingapplication.dto.SavingAccountDTO;
import application.digitalbankingapplication.model.SavingAccount;

@Mapper(componentModel = "spring")
public interface SavingAccountMapper {
    SavingAccountDTO savingAccountToSavingAccountDTO(SavingAccount savingAccount);

    SavingAccount savingAccountDTOToSavingAccount(SavingAccountDTO savingAccountDTO);
}
