package com.mintos.accounting.domain.account;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<AccountEntity, UUID>, JpaSpecificationExecutor<AccountEntity> {

    List<AccountEntity> findAllByClientIdOrderByCreatedDateDesc(UUID clientId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<AccountEntity> findById(UUID id);

    Optional<AccountEntity> findFirstById(UUID id);

}
