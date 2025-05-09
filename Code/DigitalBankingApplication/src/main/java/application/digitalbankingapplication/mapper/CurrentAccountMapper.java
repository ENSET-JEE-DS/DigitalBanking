package application.digitalbankingapplication.mapper;

import application.digitalbankingapplication.dto.CurrentAccountDTO;
import application.digitalbankingapplication.model.CurrentAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { CustomerMapper.class })
public interface CurrentAccountMapper {
    @Mapping(target = "customerDTO", source = "customer")
    CurrentAccountDTO currentAccountToCurrentAccountDTO(CurrentAccount currentAccount);

    @Mapping(target = "customer", source = "customerDTO")
    CurrentAccount currentAccountDTOToCurrentAccount(CurrentAccountDTO currentAccountDTO);
}