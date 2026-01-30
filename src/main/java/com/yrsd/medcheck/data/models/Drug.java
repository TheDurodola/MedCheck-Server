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
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String brandName;

    @Column(nullable = false)
    private String genericName;


    @Column(nullable = false, unique = true)
    private  String nafdacRegistrationNumber;

    @Column(nullable = false)
    private Integer expiryDurationInDays;

    private String description;

    @Column(nullable = false, updatable = false, unique = true)
    private String drugCode;

    @ManyToOne
    @JoinColumn(name = "manufacturing_company_id")
    private Organisation manufacturer;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdDate;

    @LastModifiedDate
    private Instant lastModifiedDate;
}
