package com.banking.cbs.customer.customer.repository;

import com.banking.cbs.customer.customer.entity.CustomerDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerDocumentRepository extends JpaRepository<CustomerDocument, String> {

    List<CustomerDocument> findByCustomerId(String customerId);
}
