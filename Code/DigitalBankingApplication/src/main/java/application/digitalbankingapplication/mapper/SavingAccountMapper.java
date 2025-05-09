package application.digitalbankingapplication.mapper;

import application.digitalbankingapplication.dto.CustomerDTO;
import application.digitalbankingapplication.dto.SavingAccountDTO;
import application.digitalbankingapplication.model.Customer;
import application.digitalbankingapplication.model.SavingAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = CustomerMapper.class)
public interface SavingAccountMapper {
    @Mapping(target = "customerDTO", source = "customer")
    SavingAccountDTO savingAccountToSavingAccountDTO(SavingAccount savingAccount);

    @Mapping(target = "customer", source = "customerDTO")
    SavingAccount savingAccountDTOToSavingAccount(SavingAccountDTO savingAccountDTO);
}