# Digital Banking

## Video of Execution


https://github.com/user-attachments/assets/613cc9e7-77c4-4740-8588-0a443d5648d0



### ClassDiagram

```plantuml
@startuml
class Customer {
    -id: String
    -name: String
    -email: String
    -getAccounts(): List<BankAccount>
}

class BankAccount {
    -id: String
    -createdAt: Date
    -balance: double
    -currency: String
    -getOperations(): List<Operation>
}

class CurrentAccount{
    -overDraft: double
}
class SavingAccount{
    -interestRate: double
}

CurrentAccount --|> BankAccount
SavingAccount --|> BankAccount

class Operation {
    -id: long
    -date: Date
    -amount: double
    -type: OperationType
}

Customer "1" --> "*" BankAccount : owns
BankAccount "1" --> "*" Operation : has

enum OperationType {
    DEBIT
    CREDIT
}

enum AccountStatus {
    CREATED
    ACTIVATED
    SUSPENDED
}

BankAccount --> AccountStatus : status
Operation --> OperationType : operation
@enduml
```

## Screenshot

![image diagram](assets/img.png)

## FrontEnd Report
Please refer to 
- [FrontEnd Report](https://github.com/ENSET-JEE-DS/DigitalBankingFrontEnd/blob/main/README.md)

## BackEnd Report
Please refer to
- [BanckEnd Report](https://github.com/ENSET-JEE-DS/DigitalBanking/blob/NewMain/Code/DigitalBankingApplication/README.md)

