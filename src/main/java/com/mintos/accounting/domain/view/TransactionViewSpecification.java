package com.mintos.accounting.domain.view;

import com.mintos.accounting.domain.StringSearchCriteria;
import com.mintos.accounting.domain.account.AccountEntity;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class TransactionViewSpecification implements Specification<TransactionViewEntity>  {

    private final String accountId;

    @Override
    public Predicate toPredicate(
            Root<TransactionViewEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

            Join<TransactionViewEntity, AccountEntity> groupJoin = root.join("account");
            return builder.equal(groupJoin.<UUID> get("id"), UUID.fromString(accountId));
    }
}
