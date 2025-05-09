package application.digitalbankingapplication.service;

import java.util.List;

import application.digitalbankingapplication.dto.AccountHistoryDTO;
import application.digitalbankingapplication.dto.AccountOperationDTO;
import application.digitalbankingapplication.dto.BankAccountDTO;
import application.digitalbankingapplication.dto.CurrentAccountDTO;
import application.digitalbankingapplication.dto.CustomerDTO;
import application.digitalbankingapplication.dto.SavingAccountDTO;
import application.digitalbankingapplication.model.BankAccount;
import org.springframework.data.domain.Page;

public interface IBankAccountService {

    CustomerDTO saveCustomer(CustomerDTO customerDTO);

    SavingAccountDTO saveSavingAccount(double initialBalance, Long customerId, double interestRate);

    CurrentAccountDTO saveCurrentAccount(double initialBalance, Long customerId, double overDraft);

    List<CurrentAccountDTO> getAllCurrentAccounts();

    List<CustomerDTO> listCustomers();

    BankAccount getBankAccount(String accountId);

    void debit(String accountId, double amount, String description);

    void credit(String accountId, double amount, String description);

    void transfer(String accountIdSource, String accountIdDestination, double amount);

    List<BankAccountDTO> getAllBankAccounts();

    CustomerDTO getCustomer(Long customerId);

    CustomerDTO updateCustomer(Long customerId, CustomerDTO customerDTOToUpdate);

    void deleteCustomer(Long customerId);

    Page<CustomerDTO> searchCustomers(String keyword, int page, int size);

    List<SavingAccountDTO> getAllSavingAccounts();

    SavingAccountDTO getSavingAccountByCustomerId(String customerId);
    
    CurrentAccountDTO getCurrentAccountByCustomerId(String customerId);

    List<AccountOperationDTO> getAccountOperations(String customerId);

    List<AccountOperationDTO> getAllAccountOperations();

    AccountHistoryDTO getAccountHistory(String bankAccountId, int page, int size);
}