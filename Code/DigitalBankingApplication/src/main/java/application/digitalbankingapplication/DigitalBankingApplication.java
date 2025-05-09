package application.digitalbankingapplication;

import application.digitalbankingapplication.dto.CurrentAccountDTO;
import application.digitalbankingapplication.dto.SavingAccountDTO;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import application.digitalbankingapplication.dto.CustomerDTO;
import application.digitalbankingapplication.repository.AccountOperationRepository;
import application.digitalbankingapplication.repository.BankAccountRepository;
import application.digitalbankingapplication.service.IBankAccountService;
import lombok.AllArgsConstructor;

@SpringBootApplication
@AllArgsConstructor
public class DigitalBankingApplication {

    private final AccountOperationRepository accountOperationRepository;
    private final BankAccountRepository bankAccountRepository;

    public static void main(String[] args) {
        SpringApplication.run(DigitalBankingApplication.class, args);
    }


//    @Bean
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

            SavingAccountDTO savingAccount1 = bankAccountService.saveSavingAccount(1000, 2L, 3);
            SavingAccountDTO savingAccount2 = bankAccountService.saveSavingAccount(1200, 3L, 3.5);

            CurrentAccountDTO currentAccount1 = bankAccountService.saveCurrentAccount(1000, 1L, 200);
            CurrentAccountDTO currentAccount2 = bankAccountService.saveCurrentAccount(1200, 3L, 100);

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

    @Bean
    public CommandLineRunner dataLoader(IBankAccountService bankAccountService ) {
        return args -> {
            System.out.println("Lodging all bank accounts");
            bankAccountService.getAllBankAccounts().forEach(bankAccount -> {
                System.out.println("Account ID: " + bankAccount.getBankAccountId());
                System.out.println("Account Balance: " + bankAccount.getBankAccountBalance());
            });
            System.out.println("Lodging all Saving accounts");
            bankAccountService.getAllSavingAccounts().forEach(savingAccount -> {
                System.out.println("Saving Account ID: " + savingAccount.getBankAccountId());
                System.out.println("Saving Account Balance: " + savingAccount.getBankAccountBalance());
            });
            System.out.println("Lodging all Current accounts");
            bankAccountService.getAllCurrentAccounts().forEach(currentAccount -> {
                System.out.println("Current Account ID: " + currentAccount.getBankAccountId());
                System.out.println("Current Account Balance: " + currentAccount.getBankAccountBalance());
                System.out.println("Current Account Overdraft: " + currentAccount.getCustomerDTO());
            });

            System.out.println("----");
            bankAccountRepository.findAll().forEach(bankAccount -> {
                System.out.println("Account ID: " + bankAccount.getBankAccountId());
                System.out.println("Customer Name: " + bankAccount.getCustomer().getCustomerName());
            });
        };
    }

}
