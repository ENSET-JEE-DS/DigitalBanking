package application.digitalbankingapplication.RESTcontroller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import application.digitalbankingapplication.dto.CustomerDTO;
import application.digitalbankingapplication.service.IBankAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/customer")
@AllArgsConstructor
@Slf4j
@CrossOrigin("*")
public class CustomerRestController {
    private IBankAccountService bankAccountService;

    @GetMapping
    public List<CustomerDTO> getAllCustomers() {
        return bankAccountService.listCustomers();
    }

    @GetMapping("/{customerId}")
    public CustomerDTO getCustomer(@PathVariable(name = "customerId") Long customerId) {
        return bankAccountService.getCustomer(customerId);
    }

    @GetMapping("/search")
    public Page<CustomerDTO> searchCustomers(
            @RequestParam(name = "kw", defaultValue = "") String keyword,
            @RequestParam(name = "p", defaultValue = "0") int page,
            @RequestParam(name = "s", defaultValue = "4") int size
            ){
        return bankAccountService.searchCustomers(keyword, page, size);
    }

    @PostMapping
    public CustomerDTO addCustomer(@RequestBody CustomerDTO customerDTOToAdd) {
        return bankAccountService.saveCustomer(customerDTOToAdd);
    }

    @PutMapping("/{customerId}")
    public CustomerDTO updateCustomer(@PathVariable(name = "customerId") Long customerId,
            @RequestBody CustomerDTO customerDTOToUpdate) {
        return bankAccountService.updateCustomer(customerId, customerDTOToUpdate);
    }

    @DeleteMapping("/{customerId}")
    public void deleteCustomer(@PathVariable(name = "customerId") Long customerId) {
        bankAccountService.deleteCustomer(customerId);
    }

}