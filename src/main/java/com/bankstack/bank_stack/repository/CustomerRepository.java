package com.bankstack.bank_stack.repository;

import com.bankstack.bank_stack.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByExternalId(String externalid);
    boolean existsByEmail(String email);
}
