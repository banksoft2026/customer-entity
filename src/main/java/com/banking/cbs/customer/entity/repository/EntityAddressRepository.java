package com.banking.cbs.customer.entity.repository;

import com.banking.cbs.customer.entity.entity.EntityAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EntityAddressRepository extends JpaRepository<EntityAddress, String> {

    List<EntityAddress> findByEntityIdAndEffectiveToIsNull(String entityId);

    List<EntityAddress> findByEntityId(String entityId);

    Optional<EntityAddress> findByEntityIdAndAddressTypeAndIsPrimaryTrueAndEffectiveToIsNull(String entityId, String addressType);
}
