package application.digitalbankingapplication.mapper;

import org.mapstruct.Mapper;

import application.digitalbankingapplication.dto.CurrentAccountDTO;
import application.digitalbankingapplication.model.CurrentAccount;

@Mapper(componentModel = "spring")
public interface CurrentAccountMapper {
    CurrentAccountDTO currentAccountToCurrentAccountDTO(CurrentAccount currentAccount);

    CurrentAccount currentAccountDTOToCurrentAccount(CurrentAccountDTO currentAccountDTO);
}
