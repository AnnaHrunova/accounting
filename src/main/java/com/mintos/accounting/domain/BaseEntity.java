package com.mintos.accounting.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;
import java.util.UUID;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {

    @Id
    @GeneratedValue
    protected UUID id;

    @Version
    @Column(nullable = false)
    private Long entityVersion;

    @CreatedDate
    @Column(nullable = false)
    private OffsetDateTime createdDate;

    @LastModifiedDate
    private OffsetDateTime updatedDate;

    @CreatedBy
    @Column(nullable = false)
    private String createdBy;

    @LastModifiedBy
    private String updatedBy;
}
