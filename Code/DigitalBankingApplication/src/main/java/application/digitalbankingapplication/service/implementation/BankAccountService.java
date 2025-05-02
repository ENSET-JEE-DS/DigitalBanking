package application.digitalbankingapplication.service.implementation;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import application.digitalbankingapplication.model.enums.AccountStatus;
import application.digitalbankingapplication.model.enums.OperationType;
import application.digitalbankingapplication.dto.CustomerDTO;
import application.digitalbankingapplication.exception.BankAccountNotFoundException;
import application.digitalbankingapplication.exception.CustomerAlreadyExistsException;
import application.digitalbankingapplication.exception.CustomerNotFoundException;
import application.digitalbankingapplication.exception.InsufficientBalanceException;
import application.digitalbankingapplication.mapper.BankAccountMapper;
import application.digitalbankingapplication.mapper.CurrentAccountMapper;
import application.digitalbankingapplication.mapper.CustomerMapper;
import application.digitalbankingapplication.mapper.SavingAccountMapper;
import application.digitalbankingapplication.model.AccountOperation;
import application.digitalbankingapplication.model.BankAccount;
import application.digitalbankingapplication.model.CurrentAccount;
import application.digitalbankingapplication.model.Customer;
import application.digitalbankingapplication.model.SavingAccount;
import application.digitalbankingapplication.repository.AccountOperationRepository;
import application.digitalbankingapplication.repository.BankAccountRepository;
import application.digitalbankingapplication.repository.CustomerRepository;
import application.digitalbankingapplication.service.IBankAccountService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Servicef
@AllArgsConstructor
@Transactional
@Slf4j
public class BankAccountService implements IBankAccountService {

    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    private CustomerMapper customerMapper;
    private BankAccountMapper bankAccountMapper;
    private SavingAccountMapper savingAccountMapper;
    private CurrentAccountMapper currentAccountMapper;

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving new customer {}", customerDTO.getCustomerName());
        if (customerRepository.findCustomerByCustomerName(customerDTO.getCustomerName()) != null) {
            log.error("Customer already exists");
            throw new CustomerAlreadyExistsException("Customer already exists");
        }
        Customer savedCustomer = customerRepository.save(customerMapper.customerDTOToCustomer(customerDTO));
        log.info("Customer saved successfully with id {}", savedCustomer.getCustomerId());
        return customerMapper.customerToCustomerDTO(savedCustomer);
    }

    @Override
    public SavingAccount saveSavingAccount(double initialBalance, Long customerId, double interestRate) {
        log.info("Saving new saving account with interest rate {}", interestRate);

        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> {
            log.error("Customer not found");
            return new CustomerNotFoundException("Customer not found");
        });

        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setBankAccountBalance(initialBalance);
        savingAccount.setCustomer(customer);
        savingAccount.setBankAccountCreatedAt(LocalDate.now());
        savingAccount.setBankAccountStatus(AccountStatus.ACTIVATED);
        savingAccount.setSavingAccountInterestRate(interestRate);
        bankAccountRepository.save(savingAccount);

        log.info("Saving account saved successfully with id {}", savingAccount.getBankAccountId());
        return savingAccount;
    }

    @Override
    public CurrentAccount saveCurrentAccount(double initialBalance, Long customerId, double overDraft) {
        log.info("Saving new current account with over draft {}", overDraft);

        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> {
            log.error("Customer not found");
            return new CustomerNotFoundException("Customer not found");
        });

        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setBankAccountBalance(initialBalance);
        currentAccount.setCustomer(customer);
        currentAccount.setBankAccountCreatedAt(LocalDate.now());
        currentAccount.setBankAccountStatus(AccountStatus.ACTIVATED);
        currentAccount.setCurrentAccountOverDraft(overDraft);
        bankAccountRepository.save(currentAccount);

        log.info("Saving account saved successfully with id {}", currentAccount.getBankAccountId());
        return currentAccount;
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        return customerRepository.findAll().stream().map(customerMapper::customerToCustomerDTO).toList();
    }

    @Override
    public BankAccount getBankAccount(String accountId) {
        return bankAccountRepository.findById(accountId).orElseThrow(() -> {
            log.error("Bank account not found");
            return new BankAccountNotFoundException("Bank account not found");
        });
    }

    @Override
    public void debit(String accountId, double amount, String description) {
        log.info("Debiting account {}", accountId);
        BankAccount bankAccount = getBankAccount(accountId);

        if (bankAccount.getBankAccountBalance() < amount) {
            log.error("Insufficient balance");
            throw new InsufficientBalanceException("Insufficient balance");
        }

        bankAccount.setBankAccountBalance(bankAccount.getBankAccountBalance() - amount);
        bankAccountRepository.save(bankAccount);

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setOperationAmount(amount);
        accountOperation.setOperationDate(LocalDate.now());
        accountOperation.setOperationType(OperationType.DEBIT);
        accountOperation.setDescription(description);
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);

        log.info("Debiting account {} successfully", accountId);
    }

    @Override
    public void credit(String accountId, double amount, String description) {
        log.info("Crediting account {}", accountId);
        BankAccount bankAccount = getBankAccount(accountId);

        bankAccount.setBankAccountBalance(bankAccount.getBankAccountBalance() + amount);
        bankAccountRepository.save(bankAccount);

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setOperationAmount(amount);
        accountOperation.setOperationDate(LocalDate.now());
        accountOperation.setOperationType(OperationType.CREDIT);
        accountOperation.setDescription(description);
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);

        log.info("Crediting account {} successfully", accountId);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) {
        log.info("Transferring {} from {} to {}", amount, accountIdSource, accountIdDestination);
        this.debit(accountIdSource, amount, "Transfer to " + accountIdDestination);
        this.credit(accountIdDestination, amount, "Transfer from " + accountIdSource);
        log.info("Transferred {} from {} to {} successfully", amount, accountIdSource, accountIdDestination);
    }

    @Override
    public List<BankAccountDTO> getBankAccounts() {
        return bankAccountRepository.findAll().stream().map(bankAccountMapper::bankAccountToBankAccountDTO).toList();
    }

    @Override
    public CustomerDTO getCustomer(Long customerId) {
        log.info("Getting customer with id {}", customerId);
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Cutomer with Id '" + customerId + "' not found"));
        log.info("Found customer successfully");
        return customerMapper.customerToCustomerDTO(customer);
    }

    @Override
    public CustomerDTO updateCustomer(Long customerId, CustomerDTO customerDTOToUpdate) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Cutomer with Id '" + customerId + "' not found"));
        log.info("Found customer successfully");
        customer.setCustomerName(customerDTOToUpdate.getCustomerName());
        customer.setCustomerEmail(customerDTOToUpdate.getCustomerEmail());
        customerRepository.save(customer);
        log.info("Customer updated successfully");
        return customerMapper.customerToCustomerDTO(customer);
    }

    @Override
    public void deleteCustomer(Long customerId) {
        log.info("Deleting customer with id {}", customerId);
        customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Cutomer with Id '" + customerId + "' not found"));
        log.info("Found customer successfully");
        customerRepository.deleteById(customerId);
        log.info("Customer deleted successfully");
    }
}
