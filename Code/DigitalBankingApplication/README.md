# Digital Banking Application

A Spring Boot-based digital banking application that manages customers, bank accounts, and account operations.

## Table of Contents

- [Digital Banking Application](#digital-banking-application)
  - [Table of Contents](#table-of-contents)
  - [Project Overview](#project-overview)
  - [Technologies Used](#technologies-used)
  - [Project Structure](#project-structure)
  - [Entity Models](#entity-models)
    - [Customer](#customer)
    - [Bank Account (Base Class)](#bank-account-base-class)
    - [Current Account](#current-account)
    - [Saving Account](#saving-account)
    - [Account Operation](#account-operation)
  - [Features](#features)
    - [1. Account Types](#1-account-types)
    - [2. Account Status Management](#2-account-status-management)
    - [3. Operation Types](#3-operation-types)
    - [4. JPA Repositories](#4-jpa-repositories)
  - [Database Schema](#database-schema)
  - [Getting Started](#getting-started)
  - [Dependencies](#dependencies)

## Project Overview

This is a digital banking application built with Spring Boot that provides functionality for:

- Customer management
- Bank account management (Current and Savings accounts)
- Account operations (debits and credits)
- Account status tracking

## Technologies Used

- Java 21
- Spring Boot 3.4.5
- Spring Data JPA
- MySQL Database
- Lombok
- Maven

## Project Structure

```
src/main/java/application/digitalbankingapplication/
├── model/
│   ├── BankAccount.java
│   ├── CurrentAccount.java
│   ├── SavingAccount.java
│   ├── Customer.java
│   ├── AccountOperation.java
│   └── enums/
│       ├── AccountStatus.java
│       └── OperationType.java
├── repository/
│   ├── BankAccountRepository.java
│   ├── CustomerRepository.java
│   └── AccountOperationRepository.java
└── DigitalBankingApplication.java
```

## Entity Models

### Customer

```java
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    private String customerName;
    private String customerEmail;
    @OneToMany(mappedBy = "customer")
    private List<BankAccount> bankAccountList;
}
```

### Bank Account (Base Class)

```java
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "account_type", discriminatorType = DiscriminatorType.STRING, length = 4)
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String bankAccountId;
    private double bankAccountBalance;
    private LocalDate bankAccountCreatedAt;
    private AccountStatus bankAccountStatus;
    @ManyToOne
    private Customer customer;
    @OneToMany(mappedBy = "bankAccount")
    private List<AccountOperation> accountOperationList;
}
```

### Current Account

```java
@Entity
@DiscriminatorValue("CA")
public class CurrentAccount extends BankAccount {
    private double currentAccountOverDraft;
}
```

### Saving Account

```java
@Entity
@DiscriminatorValue("SA")
public class SavingAccount extends BankAccount {
    private double savingAccountInterestRate;
}
```

### Account Operation

```java
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountOperation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate operationDate;
    private Double operationAmount;
    private OperationType operationType;
    @ManyToOne
    private BankAccount bankAccount;
}
```

## Features

### 1. Account Types

- **Current Account**: Supports overdraft functionality
- **Saving Account**: Includes interest rate calculation

### 2. Account Status Management

```java
public enum AccountStatus {
    CREATED, ACTIVATED, SUSPENDED
}
```

### 3. Operation Types

```java
public enum OperationType {
    DEBIT, CREDIT
}
```

### 4. JPA Repositories

```java
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
}

@Repository
public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {
}
```

## Database Schema

The application uses a single-table inheritance strategy for bank accounts with the following structure:

```sql
create table bank_account (
    account_type varchar(4) not null,
    bank_account_id varchar(255) not null,
    bank_account_balance float(53) not null,
    bank_account_created_at date,
    bank_account_status tinyint,
    current_account_over_draft float(53),
    saving_account_interest_rate float(53),
    customer_customer_id bigint,
    primary key (bank_account_id)
)
```

## Getting Started

1. **Prerequisites**

   - Java 21
   - MySQL Database
   - Maven

2. **Configuration**

   - Update the `application.properties` file with your database credentials
   - Configure the database connection settings

3. **Running the Application**

   ```bash
   mvn spring-boot:run
   ```

4. **Accessing the Application**
   - The application will start on the default port (8080)
   - Use the provided REST endpoints to interact with the application

## Dependencies

The project uses the following key dependencies (from pom.xml):

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```
