package application.digitalbankingapplication.RESTcontroller;

import application.digitalbankingapplication.dto.AccountOperationDTO;
import application.digitalbankingapplication.service.IBankAccountService;
import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accountOperation")
@AllArgsConstructor
@CrossOrigin("*")
public class AccountOperationRestController {

    private IBankAccountService bankAccountService;

 
    @GetMapping
    public List<AccountOperationDTO> getAllAccountOperations() {
        return bankAccountService.getAllAccountOperations();
    }

    @GetMapping("/{bankAccountId}")
    public List<AccountOperationDTO> getAccountOperations(@PathVariable String bankAccountId) {
        return bankAccountService.getAccountOperations(bankAccountId);
    }
}
