package com.mintos.accounting.domain.transaction;

import com.mintos.accounting.common.Currency;
import com.mintos.accounting.common.TransactionStatus;
import com.mintos.accounting.domain.BaseEntity;
import com.mintos.accounting.domain.account.AccountEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "transaction")
public class TransactionEntity extends BaseEntity {

    private String requestId;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @ManyToOne
    @JoinColumn(
            name = "account_id_from",
            foreignKey = @ForeignKey(name = "fk_tr_acc_from"),
            nullable = false)
    private AccountEntity fromAccount;

    @ManyToOne
    @JoinColumn(
            name = "account_id_to",
            foreignKey = @ForeignKey(name = "fk_tr_acc_to"),
            nullable = false)
    private AccountEntity toAccount;

}
