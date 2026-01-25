package com.yrsd.medcheck.data.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Batch {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant created;

    @LastModifiedDate
    @Column(nullable = false, updatable = false)
    private Instant lastModified;
    @CreatedBy
    @Column(updatable = false)
    private String createdBy;




    @Column(nullable = false)
    private LocalDate manufactureDate;

    @Column(nullable = false)
    private LocalDate expirationDate;

    @Column(nullable = false)
    private BigInteger verificationCount;

    @Column(unique = true, nullable = false)
    private String verificationCode;

    @OneToMany(mappedBy = "batch", cascade = CascadeType.ALL)
    private List<Pack> packs;

    @ManyToOne
    @JoinColumn(name = "drug_id")
    private Drug drug;


    public void addPack(Pack pack) {
        if (packs == null) {
            packs = new ArrayList<>();
        }
        this.packs.add(pack);
        pack.setBatch(this);
    }
}
