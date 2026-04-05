package com.banking.cbs.customer.customer.repository;

import com.banking.cbs.customer.customer.entity.CustomerMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerMasterRepository extends JpaRepository<CustomerMaster, String> {

    List<CustomerMaster> findByCustomerStatus(String customerStatus);

    List<CustomerMaster> findByCustomerSegment(String customerSegment);

    List<CustomerMaster> findByRelationshipManagerId(String relationshipManagerId);

    List<CustomerMaster> findByCustomerSegmentAndRelationshipManagerId(String customerSegment, String relationshipManagerId);

    boolean existsByCustomerNumber(String customerNumber);
}
