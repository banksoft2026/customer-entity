package com.banking.cbs.customer.entity.repository;

import com.banking.cbs.customer.entity.entity.EntityCompliance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EntityComplianceRepository extends JpaRepository<EntityCompliance, String> {

    Optional<EntityCompliance> findByEntityId(String entityId);
}
