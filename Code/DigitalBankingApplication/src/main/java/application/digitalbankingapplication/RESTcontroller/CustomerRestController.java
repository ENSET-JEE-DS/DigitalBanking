package application.digitalbankingapplication.RESTcontroller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public List<CustomerDTO> getAllCustomers() {
        return bankAccountService.listCustomers();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{customerId}")
    public CustomerDTO getCustomer(@PathVariable(name = "customerId") Long customerId) {
        return bankAccountService.getCustomer(customerId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/search")
    public Page<CustomerDTO> searchCustomers(
            @RequestParam(name = "kw", defaultValue = "") String keyword,
            @RequestParam(name = "p", defaultValue = "0") int page,
            @RequestParam(name = "s", defaultValue = "4") int size) {
        return bankAccountService.searchCustomers(keyword, page, size);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public CustomerDTO addCustomer(@RequestBody CustomerDTO customerDTOToAdd) {
        return bankAccountService.saveCustomer(customerDTOToAdd);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{customerId}")
    public CustomerDTO updateCustomer(@PathVariable(name = "customerId") Long customerId,
            @RequestBody CustomerDTO customerDTOToUpdate) {
        return bankAccountService.updateCustomer(customerId, customerDTOToUpdate);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{customerId}")
    public void deleteCustomer(@PathVariable(name = "customerId") Long customerId) {
        bankAccountService.deleteCustomer(customerId);
    }

}