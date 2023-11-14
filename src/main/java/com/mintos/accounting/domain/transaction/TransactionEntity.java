package com.mintos.accounting.domain.transaction;

import com.mintos.accounting.common.Currency;
import com.mintos.accounting.common.TransactionType;
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

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private BigDecimal amount;

    private Currency currency;

    @ManyToOne
    @JoinColumn(
            name = "account_id_part",
            foreignKey = @ForeignKey(name = "fk_tr_acc_part"),
            nullable = false)
    private AccountEntity participantAccount;

    @ManyToOne
    @JoinColumn(
            name = "account_id_main",
            foreignKey = @ForeignKey(name = "fk_tr_acc_main"),
            nullable = false)
    private AccountEntity mainAccount;

}
