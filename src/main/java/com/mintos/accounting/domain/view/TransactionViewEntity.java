package com.mintos.accounting.domain.view;

import com.mintos.accounting.common.Currency;
import com.mintos.accounting.common.TransactionType;
import com.mintos.accounting.domain.AccountingSchema;
import com.mintos.accounting.domain.BaseEntity;
import com.mintos.accounting.domain.account.AccountEntity;
import com.mintos.accounting.domain.transaction.TransactionEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "transaction_view", schema = AccountingSchema.NAME)
public class TransactionViewEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    private Currency convertedFrom;

    private BigDecimal convertedAmount;

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
