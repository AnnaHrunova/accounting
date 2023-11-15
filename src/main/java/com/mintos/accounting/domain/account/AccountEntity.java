package com.mintos.accounting.domain.account;

import com.mintos.accounting.common.Currency;
import com.mintos.accounting.domain.BaseEntity;
import com.mintos.accounting.domain.client.ClientEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "account")
public class AccountEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private Currency currency;

    private BigDecimal balance;

    @ManyToOne
    @JoinColumn(
            name = "client_id",
            foreignKey = @ForeignKey(name = "fk_acc_cl"),
            nullable = false)
    private ClientEntity client;

}


