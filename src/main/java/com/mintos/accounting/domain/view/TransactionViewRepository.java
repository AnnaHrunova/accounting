package com.mintos.accounting.domain.view;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface TransactionViewRepository extends JpaRepository<TransactionViewEntity, UUID>, JpaSpecificationExecutor<TransactionViewEntity> {


    List<TransactionViewEntity> findAllByAccount_IdOrderByCreatedDateDesc(UUID accountUUID);

}
