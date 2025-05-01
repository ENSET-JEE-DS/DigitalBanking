package application.digitalbankingapplication;

import java.time.LocalDate;
import java.util.stream.Stream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import application.digitalbankingapplication.dto.CustomerDTO;
import application.digitalbankingapplication.model.AccountOperation;
import application.digitalbankingapplication.model.CurrentAccount;
import application.digitalbankingapplication.model.Customer;
import application.digitalbankingapplication.model.SavingAccount;
import application.digitalbankingapplication.model.enums.AccountStatus;
import application.digitalbankingapplication.model.enums.OperationType;
import application.digitalbankingapplication.repository.AccountOperationRepository;
import application.digitalbankingapplication.repository.BankAccountRepository;
import application.digitalbankingapplication.repository.CustomerRepository;
import application.digitalbankingapplication.service.IBankAccountService;
import lombok.AllArgsConstructor;

@SpringBootApplication
@AllArgsConstructor
public class DigitalBankingApplication {

    private final AccountOperationRepository accountOperationRepository;

    public static void main(String[] args) {
        SpringApplication.run(DigitalBankingApplication.class, args);
    }

    // @Bean
    public CommandLineRunner repositoryTest(CustomerRepository customerRepository,
            BankAccountRepository bankAccountRepository) {

        return args -> {
            Stream.of("John", "Jane", "Jim", "Jill").forEach(name -> {
                Customer customer = Customer.builder()
                        .customerName(name)
                        .customerEmail(name + "@gmail.com")
                        .build();
                customerRepository.save(customer);
            });

            customerRepository.findAll().forEach(customer -> {
                CurrentAccount currentAccount = new CurrentAccount();
                currentAccount.setBankAccountBalance(Math.random() * 9000);
                currentAccount.setBankAccountCreatedAt(LocalDate.now().plusDays((long) (Math.random() * 10)));
                currentAccount.setBankAccountStatus(AccountStatus.ACTIVATED);
                currentAccount.setCustomer(customer);
                currentAccount.setCurrentAccountOverDraft(900.0);
                bankAccountRepository.save(currentAccount);
            });

            customerRepository.findAll().forEach(customer -> {
                SavingAccount savingAccount = new SavingAccount();
                savingAccount.setBankAccountBalance(Math.random() * 9000);
                savingAccount.setBankAccountCreatedAt(LocalDate.now().plusDays((long) (Math.random() * 10)));
                savingAccount.setBankAccountStatus(AccountStatus.ACTIVATED);
                savingAccount.setCustomer(customer);
                savingAccount.setSavingAccountInterestRate(Math.random() * 15);
                bankAccountRepository.save(savingAccount);
            });

            bankAccountRepository.findAll().forEach(bankAccount -> {
                for (int i = 0; i < 9; i++) {
                    AccountOperation accountOperation = new AccountOperation();
                    accountOperation.setOperationDate(LocalDate.now().minusDays((long) (Math.random() * 10)));
                    accountOperation.setOperationAmount(Math.random() * 1000);
                    accountOperation.setOperationType(Math.random() > 0.5 ? OperationType.CREDIT : OperationType.DEBIT);
                    accountOperation.setBankAccount(bankAccount);
                    accountOperationRepository.save(accountOperation);
                }
            });
        };
    }

    @Bean
    public CommandLineRunner serviceTest(IBankAccountService bankAccountService) {
        return args -> {
            bankAccountService.saveCustomer(
                    CustomerDTO.builder()
                            .customerName("John")
                            .customerEmail("john@gmail.com")
                            .build());
            bankAccountService.saveCustomer(CustomerDTO.builder()
                    .customerName("Jane")
                    .customerEmail("jane@gmail.com")
                    .build());

            bankAccountService.saveCustomer(
                    CustomerDTO.builder()
                            .customerName("Doe")
                            .customerEmail("doe@gmail.com")
                            .build());

            SavingAccount savingAccount1 = bankAccountService.saveSavingAccount(1000, 2L, 3);
            SavingAccount savingAccount2 = bankAccountService.saveSavingAccount(1200, 3L, 3.5);

            CurrentAccount currentAccount1 = bankAccountService.saveCurrentAccount(1000, 1L, 200);
            CurrentAccount currentAccount2 = bankAccountService.saveCurrentAccount(1200, 3L, 100);

            System.out.print("*****************************************\n");
            bankAccountService.transfer(savingAccount1.getBankAccountId(), savingAccount2.getBankAccountId(), 100);
            System.out.println(
                    bankAccountService.getBankAccount(savingAccount1.getBankAccountId()).getBankAccountBalance());
            System.out.println(
                    bankAccountService.getBankAccount(savingAccount2.getBankAccountId()).getBankAccountBalance());
            System.out.print("*****************************************\n");

            System.out.print("*****************************************\n");
            bankAccountService.transfer(currentAccount2.getBankAccountId(), currentAccount1.getBankAccountId(), 254);
            System.out.println(
                    bankAccountService.getBankAccount(currentAccount1.getBankAccountId()).getBankAccountBalance());
            System.out.println(
                    bankAccountService.getBankAccount(currentAccount2.getBankAccountId()).getBankAccountBalance());
            System.out.print("*****************************************\n");

            System.out.print("*****************************************\n");
            bankAccountService.transfer(savingAccount1.getBankAccountId(), currentAccount1.getBankAccountId(), 254);
            System.out.println(
                    bankAccountService.getBankAccount(currentAccount1.getBankAccountId()).getBankAccountBalance());
            System.out.println(
                    bankAccountService.getBankAccount(savingAccount1.getBankAccountId()).getBankAccountBalance());
            System.out.print("*****************************************\n");

        };
    }
}
