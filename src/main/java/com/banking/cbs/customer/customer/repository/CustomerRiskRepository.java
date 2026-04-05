package com.banking.cbs.customer.customer.repository;

import com.banking.cbs.customer.customer.entity.CustomerRisk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRiskRepository extends JpaRepository<CustomerRisk, String> {

    Optional<CustomerRisk> findByCustomerId(String customerId);
}
