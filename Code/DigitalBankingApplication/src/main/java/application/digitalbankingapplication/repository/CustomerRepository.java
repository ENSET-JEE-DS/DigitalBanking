package application.digitalbankingapplication.repository;

import application.digitalbankingapplication.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findCustomerByCustomerName(String customerName);
    Page<Customer> findCustomerByCustomerNameContaining(String customerName, Pageable pageable);
}