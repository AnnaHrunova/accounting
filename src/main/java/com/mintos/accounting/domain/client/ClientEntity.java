package com.mintos.accounting.domain.client;

import com.mintos.accounting.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "client")
public class ClientEntity extends BaseEntity {

    private String firstName;
    private String lastName;
    private String personalId;

}
