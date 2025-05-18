# Digital Banking Application

A Spring Boot-based digital banking application that manages customers, bank accounts, and account operations with secure JWT authentication.

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
    - [5. REST API Endpoints](#5-rest-api-endpoints)
    - [6. API Documentation](#6-api-documentation)
    - [7. Security Implementation](#7-security-implementation)
    - [8. Controllers](#8-controllers)
  - [Database Schema](#database-schema)
  - [Getting Started](#getting-started)
  - [Dependencies](#dependencies)

## Project Overview

This is a digital banking application built with Spring Boot that provides functionality for:

- Customer management
- Bank account management (Current and Savings accounts)
- Account operations (debits and credits)
- Account status tracking
- Secure authentication using JWT

## Technologies Used

- Java 21
- Spring Boot 3.4.5
- Spring Data JPA
- Spring Security
- JWT Authentication
- MySQL Database
- Lombok
- MapStruct
- OpenAPI (Swagger)
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
├── dto/
│   ├── CustomerDTO.java
│   ├── BankAccountDTO.java
│   └── AccountOperationDTO.java
├── mapper/
│   ├── CustomerMapper.java
│   ├── BankAccountMapper.java
│   └── AccountOperationMapper.java
├── repository/
│   ├── BankAccountRepository.java
│   ├── CustomerRepository.java
│   └── AccountOperationRepository.java
├── service/
│   ├── impl/
│   │   ├── BankAccountServiceImpl.java
│   │   └── CustomerServiceImpl.java
│   ├── BankAccountService.java
│   └── CustomerService.java
├── web/
│   ├── CustomerController.java
│   └── BankAccountController.java
├── security/
│   ├── SecurityConfig.java
│   ├── JwtAuthFilter.java
│   └── JwtService.java
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

### 5. REST API Endpoints

#### Customer Endpoints

- **GET /customer**: Get all customers
- **GET /customer/{id}**: Get a specific customer by ID
- **GET /customer/search?kw={keyword}&p={page}&s={size}**: Search customers with pagination
- **POST /customer**: Add a new customer
- **PUT /customer/{id}**: Update a customer
- **DELETE /customer/{id}**: Delete a customer

#### Bank Account Endpoints

- **GET /bankAccount**: Get all bank accounts
- **GET /bankAccount/{id}**: Get a specific bank account by ID
- **POST /bankAccount/current**: Create a new current account
- **POST /bankAccount/saving**: Create a new saving account
- **POST /bankAccount/debit**: Perform a debit operation
- **POST /bankAccount/credit**: Perform a credit operation
- **POST /bankAccount/transfer**: Transfer money between accounts

### 6. API Documentation

The API is documented using Swagger UI, which can be accessed at:
```
http://localhost:8080/swagger-ui.html
```

### 7. Security Implementation

```java
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/auth/**", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
                .requestMatchers("/api/v1/management/**").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers(HttpMethod.GET, "/api/v1/management/**").hasAnyAuthority("ADMIN_READ", "MANAGER_READ")
                .requestMatchers(HttpMethod.POST, "/api/v1/management/**").hasAnyAuthority("ADMIN_CREATE", "MANAGER_CREATE")
                .requestMatchers(HttpMethod.PUT, "/api/v1/management/**").hasAnyAuthority("ADMIN_UPDATE", "MANAGER_UPDATE")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/management/**").hasAnyAuthority("ADMIN_DELETE", "MANAGER_DELETE")
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
```

JWT Service Implementation:

```java
@Service
public class JwtService {
    
    private static final String SECRET_KEY = "${jwt.secret-key}";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
```

### 8. Controllers

Customer Controller Example:

```java
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public List<CustomerDTO> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public CustomerDTO getCustomer(@PathVariable Long id) {
        return customerService.getCustomer(id);
    }

    @GetMapping("/search")
    public Page<CustomerDTO> searchCustomers(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return customerService.searchCustomers(keyword, page, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
        return customerService.saveCustomer(customerDTO);
    }

    @PutMapping("/{id}")
    public CustomerDTO updateCustomer(@PathVariable Long id, @RequestBody CustomerDTO customerDTO) {
        customerDTO.setCustomerId(id);
        return customerService.updateCustomer(customerDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
    }
}
```

Bank Account Controller Example:

```java
@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @GetMapping
    public List<BankAccountDTO> getAllAccounts() {
        return bankAccountService.getAllBankAccounts();
    }

    @GetMapping("/{id}")
    public BankAccountDTO getBankAccount(@PathVariable String id) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(id);
    }

    @PostMapping("/debit")
    public DebitDTO debit(@RequestBody DebitDTO debitDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        bankAccountService.debit(debitDTO.getAccountId(), debitDTO.getAmount(), debitDTO.getDescription());
        return debitDTO;
    }

    @PostMapping("/credit")
    public CreditDTO credit(@RequestBody CreditDTO creditDTO) throws BankAccountNotFoundException {
        bankAccountService.credit(creditDTO.getAccountId(), creditDTO.getAmount(), creditDTO.getDescription());
        return creditDTO;
    }

    @PostMapping("/transfer")
    public void transfer(@RequestBody TransferRequestDTO transferRequestDTO) 
            throws BankAccountNotFoundException, BalanceNotSufficientException {
        bankAccountService.transfer(
            transferRequestDTO.getAccountSource(),
            transferRequestDTO.getAccountDestination(),
            transferRequestDTO.getAmount()
        );
    }
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
    <!-- Spring Boot Core -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
        <scope>runtime</scope>
    </dependency>
    
    <!-- Database -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <scope>runtime</scope>
    </dependency>
    
    <!-- Security -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
    </dependency>
    
    <!-- Tools -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>org.mapstruct</groupId>
        <artifactId>mapstruct</artifactId>
        <version>${org.mapstruct.version}</version>
    </dependency>
    
    <!-- Documentation -->
    <dependency>
        <groupId>org.springdoc</groupId>
        <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
        <version>2.2.0</version>
    </dependency>
    
    <!-- Testing -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```
