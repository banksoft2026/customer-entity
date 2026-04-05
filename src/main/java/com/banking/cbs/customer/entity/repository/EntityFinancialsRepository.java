package com.banking.cbs.customer.entity.repository;

import com.banking.cbs.customer.entity.entity.EntityFinancials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EntityFinancialsRepository extends JpaRepository<EntityFinancials, String> {

    List<EntityFinancials> findByEntityIdOrderByFinancialYearDesc(String entityId);

    Optional<EntityFinancials> findByEntityIdAndFinancialYear(String entityId, String financialYear);
}
