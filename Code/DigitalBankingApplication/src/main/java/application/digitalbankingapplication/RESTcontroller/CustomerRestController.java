package application.digitalbankingapplication.RESTcontroller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import application.digitalbankingapplication.dto.CustomerDTO;
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

    @GetMapping("/{id}")
    public CustomerDTO getCustomer(@PathVariable(name = "id") Long customerId) {
        return bankAccountService.getCustomer(customerId);
    }

    @PostMapping
    public CustomerDTO addCustomer(@RequestBody CustomerDTO customerDTOToAdd) {
        return bankAccountService.saveCustomer(customerDTOToAdd);
    }

    @PutMapping("/{id}")
    public CustomerDTO updateCustomer(@PathVariable(name = "id") Long customerId,
            @RequestBody CustomerDTO customerDTOToUpdate) {
        return bankAccountService.updateCustomer(customerId, customerDTOToUpdate);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable(name = "id") Long customerId) {
        bankAccountService.deleteCustomer(customerId);
    }

}