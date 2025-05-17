package application.digitalbankingapplication.RESTcontroller;

import java.util.List;

import application.digitalbankingapplication.dto.AccountHistoryDTO;
import application.digitalbankingapplication.dto.BankAccountDTO;
import application.digitalbankingapplication.dto.CurrentAccountDTO;
import application.digitalbankingapplication.dto.SavingAccountDTO;
import org.springframework.web.bind.annotation.*;

import application.digitalbankingapplication.service.IBankAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/bankAccount")
@AllArgsConstructor
@Slf4j
@CrossOrigin("*")
public class BankAccountRestController {
    private IBankAccountService bankAccountService;

    @GetMapping
    public List<BankAccountDTO> getAllBankAccounts() {
        return bankAccountService.getAllBankAccounts();
    }

    @GetMapping("/current")
    public List<CurrentAccountDTO> getAllCurrentAccounts() {
        return bankAccountService.getAllCurrentAccounts();
    }

    @GetMapping("/saving")
    public List<SavingAccountDTO> getAllSavingAccounts() {
        return bankAccountService.getAllSavingAccounts();
    }

    @GetMapping("/saving/{savingAccountId}")
    public SavingAccountDTO getSavingAccountByCustomerId(
            @PathVariable(name = "savingAccountId") String savingAccountId) {
        return bankAccountService.getSavingAccountByCustomerId(savingAccountId);
    }

    @GetMapping("/current/{currentAccountId}")
    public CurrentAccountDTO getCurrentAccountByCustomerId(
            @PathVariable(name = "currentAccountId") String currentAccountId) {
        return bankAccountService.getCurrentAccountByCustomerId(currentAccountId);
    }

    @GetMapping("/{bankAccountId}/pageOperations")
    public AccountHistoryDTO getAccountHistory(
            @PathVariable(name = "bankAccountId") String bankAccountId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size) {
        return bankAccountService.getAccountHistory(bankAccountId, page, size);
    }
}