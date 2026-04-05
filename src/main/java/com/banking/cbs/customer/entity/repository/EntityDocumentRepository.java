package com.banking.cbs.customer.entity.repository;

import com.banking.cbs.customer.entity.entity.EntityDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntityDocumentRepository extends JpaRepository<EntityDocument, String> {

    List<EntityDocument> findByEntityId(String entityId);
}
