package com.mintos.accounting.domain.view;

import com.mintos.accounting.domain.transaction.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface TransactionViewRepository extends JpaRepository<TransactionViewEntity, UUID>, JpaSpecificationExecutor<TransactionEntity> {


    List<TransactionViewEntity> findAllByAccount_IdOrderByCreatedDateDesc(UUID accountUUID);

}
