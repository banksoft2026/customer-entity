package com.banking.cbs.customer.customer.repository;

import com.banking.cbs.customer.customer.entity.CustomerContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerContactRepository extends JpaRepository<CustomerContact, String> {

    List<CustomerContact> findByCustomerIdAndEffectiveToIsNull(String customerId);

    List<CustomerContact> findByCustomerId(String customerId);

    Optional<CustomerContact> findByCustomerIdAndContactTypeAndIsPrimaryTrueAndEffectiveToIsNull(String customerId, String contactType);
}
