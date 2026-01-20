package com.yrsd.medcheck.data.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Drug {

    @Id
    private String id;

    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "manufacturer_id")
    private UserAccount manufacturer;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdDate;

    @Column(nullable = false, updatable = false)
    private Instant expirationDate;

    @LastModifiedDate
    private Instant lastModifiedDate;
}
