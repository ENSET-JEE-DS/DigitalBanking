package application.digitalbankingapplication.repository;

import application.digitalbankingapplication.model.AccountOperation;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {
    List<AccountOperation> findByBankAccount_BankAccountId(String bankAccountId);
    Page<AccountOperation> findByBankAccount_BankAccountId(String bankAccountId, Pageable pageable);
}