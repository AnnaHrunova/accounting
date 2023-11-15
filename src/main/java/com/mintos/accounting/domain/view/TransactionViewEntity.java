package com.mintos.accounting.domain.view;

import com.mintos.accounting.common.TransactionType;
import com.mintos.accounting.domain.BaseEntity;
import com.mintos.accounting.domain.account.AccountEntity;
import com.mintos.accounting.domain.transaction.TransactionEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "transaction_view")
public class TransactionViewEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @ManyToOne
    @JoinColumn(
            name = "account_id",
            foreignKey = @ForeignKey(name = "fk_vw_acc"),
            nullable = false)
    private AccountEntity account;

    @ManyToOne
    @JoinColumn(
            name = "transaction_id",
            foreignKey = @ForeignKey(name = "fk_vw_tr"),
            nullable = false)
    private TransactionEntity transaction;
}
