package application.digitalbankingapplication.RESTcontroller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import application.digitalbankingapplication.dto.BankAccountDTO;
import application.digitalbankingapplication.model.BankAccount;
import application.digitalbankingapplication.service.IBankAccountService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/bankAccount")
@AllArgsConstructor
public class BankAccountRestController {
    private IBankAccountService bankAccountService;

    @GetMapping
    public List<BankAccountDTO> getAllBankAccounts() {
        return bankAccountService.getBankAccounts();
    }
}
