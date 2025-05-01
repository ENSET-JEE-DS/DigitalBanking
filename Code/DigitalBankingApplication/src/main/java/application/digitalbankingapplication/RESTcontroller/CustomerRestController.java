package application.digitalbankingapplication.RESTcontroller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import application.digitalbankingapplication.dto.CustomerDTO;
import application.digitalbankingapplication.model.Customer;
import application.digitalbankingapplication.service.IBankAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/customer")
@AllArgsConstructor
@Slf4j
public class CustomerRestController {
    private IBankAccountService bankAccountService;

    @GetMapping
    public List<CustomerDTO> getAllCustomers() {
        return bankAccountService.listCustomers();
    }
    /*
     * @PostMapping
     * public CustomerDTO addCustomer(@RequestBody Customer customerToAdd) {
     * return bankAccountService.saveCustomer(customerToAdd);
     * }
     */

}