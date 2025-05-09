package application.digitalbankingapplication.service.implementation;

import java.time.LocalDate;
import java.util.List;

import application.digitalbankingapplication.dto.AccountHistoryDTO;
import application.digitalbankingapplication.dto.AccountOperationDTO;
import application.digitalbankingapplication.dto.BankAccountDTO;
import application.digitalbankingapplication.dto.CurrentAccountDTO;
import application.digitalbankingapplication.dto.SavingAccountDTO;
import application.digitalbankingapplication.mapper.AccountOperationMapper;
import application.digitalbankingapplication.mapper.BankAccountMapper;
import application.digitalbankingapplication.mapper.CurrentAccountMapper;
import application.digitalbankingapplication.mapper.SavingAccountMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import application.digitalbankingapplication.model.enums.AccountStatus;
import application.digitalbankingapplication.model.enums.OperationType;
import application.digitalbankingapplication.dto.CustomerDTO;
import application.digitalbankingapplication.exception.BankAccountNotFoundException;
import application.digitalbankingapplication.exception.CustomerAlreadyExistsException;
import application.digitalbankingapplication.exception.CustomerNotFoundException;
import application.digitalbankingapplication.exception.InsufficientBalanceException;
import application.digitalbankingapplication.mapper.CustomerMapper;
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

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class BankAccountService implements IBankAccountService {

    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    private CustomerMapper customerMapper;
    private CurrentAccountMapper currentAccountMapper;
    private SavingAccountMapper savingAccountMapper;
    private BankAccountMapper bankAccountMapper;
    private AccountOperationMapper accountOperationMapper;

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
    public SavingAccountDTO saveSavingAccount(double initialBalance, Long customerId, double interestRate) {
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
        return savingAccountMapper.savingAccountToSavingAccountDTO(savingAccount);
    }

    @Override
    public CurrentAccountDTO saveCurrentAccount(double initialBalance, Long customerId, double overDraft) {
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
        return currentAccountMapper.currentAccountToCurrentAccountDTO(currentAccount);
    }

    @Override
    public List<CurrentAccountDTO> getAllCurrentAccounts() {
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        return bankAccounts.stream()
                .filter(ba -> ba instanceof CurrentAccount)
                .map(ba -> (CurrentAccount) ba)
                .map(currentAccountMapper::currentAccountToCurrentAccountDTO)
                .toList();
    }

    @Override
    public List<SavingAccountDTO> getAllSavingAccounts() {
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        return bankAccounts.stream()
                .filter(ba -> ba instanceof SavingAccount)
                .map(ba -> (SavingAccount) ba)
                .map(savingAccountMapper::savingAccountToSavingAccountDTO)
                .toList();
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
    public List<BankAccountDTO> getAllBankAccounts() {
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

    @Override
    public Page<CustomerDTO> searchCustomers(String keyword, int page, int size) {
        log.info("Searching for customers with keyword {}", keyword);

        Page<Customer> customerPage = customerRepository
                .findCustomerByCustomerNameContaining(keyword, PageRequest.of(page, size));

        log.info("Found {} customers", customerPage.toList().size());
        return customerPage.map(customerMapper::customerToCustomerDTO);
    }

    @Override
    public SavingAccountDTO getSavingAccountByCustomerId(String customerId) {
        return savingAccountMapper.savingAccountToSavingAccountDTO(
                (SavingAccount) bankAccountRepository.findById(customerId).orElse(new SavingAccount()));

    }

    @Override
    public CurrentAccountDTO getCurrentAccountByCustomerId(String customerId) {
        return currentAccountMapper.currentAccountToCurrentAccountDTO(
                (CurrentAccount) bankAccountRepository.findById(customerId).orElse(new CurrentAccount()));
    }

    @Override
    public List<AccountOperationDTO> getAccountOperations(String bankAccountId) {
        return accountOperationRepository.findByBankAccount_BankAccountId(bankAccountId).stream()
                .map(accountOperationMapper::accountOperationToAccountOperationDTO).toList();
    }

    @Override
    public List<AccountOperationDTO> getAllAccountOperations() {
        return accountOperationRepository.findAll().stream()
                .map(accountOperationMapper::accountOperationToAccountOperationDTO).toList();
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String bankAccountId, int page, int size) {
        BankAccount bankAccount = bankAccountRepository.findById(bankAccountId).orElseThrow(() -> new BankAccountNotFoundException("Bank account not found"));
        
        Page<AccountOperation> accountOperations = accountOperationRepository.findByBankAccount_BankAccountId(bankAccountId, PageRequest.of(page, size));
        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        accountHistoryDTO.setBankAccountId(bankAccountId);
        accountHistoryDTO.setBalance(accountOperations.getTotalElements());
        accountHistoryDTO.setCurrentPage(accountOperations.getNumber());
        accountHistoryDTO.setPageSize(accountOperations.getSize());
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());
        accountHistoryDTO.setAccountOperationsDTO(accountOperations.getContent().stream().map(accountOperationMapper::accountOperationToAccountOperationDTO).toList());
        return accountHistoryDTO;
    }
}
