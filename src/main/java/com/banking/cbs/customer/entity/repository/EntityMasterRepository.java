package com.banking.cbs.customer.entity.repository;

import com.banking.cbs.customer.entity.entity.EntityMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EntityMasterRepository extends JpaRepository<EntityMaster, String> {

    List<EntityMaster> findByEntityStatus(String entityStatus);

    List<EntityMaster> findByEntityType(String entityType);

    Optional<EntityMaster> findByRegistrationNumberAndRegistrationCountry(String registrationNumber, String registrationCountry);

    List<EntityMaster> findByRelationshipManagerId(String relationshipManagerId);

    List<EntityMaster> findByEntityStatusAndEntityType(String entityStatus, String entityType);

    List<EntityMaster> findByRegistrationCountry(String registrationCountry);

    List<EntityMaster> findByParentEntityId(String parentEntityId);
}
