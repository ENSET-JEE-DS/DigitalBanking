package application.digitalbankingapplication.service;

import java.util.List;

import application.digitalbankingapplication.model.BankAccount;
import application.digitalbankingapplication.model.CurrentAccount;
import application.digitalbankingapplication.model.Customer;
import application.digitalbankingapplication.model.SavingAccount;

public interface IBankAccountService {

    Customer saveCustomer(Customer customer);

    SavingAccount saveSavingAccount(double initialBalance, Long customerId, double interestRate);

    CurrentAccount saveCurrentAccount(double initialBalance, Long customerId, double overDraft);

    List<Customer> listCustomers();

    BankAccount getBankAccount(String accountId);

    void debit(String accountId, double amount, String description);

    void credit(String accountId, double amount, String description);

    void transfer(String accountIdSource, String accountIdDestination, double amount);

    List<BankAccount> getBankAccounts();

}