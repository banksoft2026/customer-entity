package com.banking.cbs.customer.entity.repository;

import com.banking.cbs.customer.entity.entity.CustomerEntityLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerEntityLinkRepository extends JpaRepository<CustomerEntityLink, String> {

    List<CustomerEntityLink> findByEntityIdAndIsActiveTrue(String entityId);

    List<CustomerEntityLink> findByCustomerIdAndIsActiveTrue(String customerId);

    Optional<CustomerEntityLink> findByCustomerIdAndEntityIdAndIsActiveTrue(String customerId, String entityId);
}
