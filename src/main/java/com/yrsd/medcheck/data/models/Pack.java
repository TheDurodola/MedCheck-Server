package com.yrsd.medcheck.data.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Pack {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true, nullable = false)
    private String packIdentifier;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant created;

    @LastModifiedDate
    @Column(nullable = false, updatable = false)
    private Instant lastModified;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isSold = false;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pack")
    private List<Sachet> sachets;

    @Column(nullable = false)
    private BigInteger verificationCount;

    @Column(unique = true, nullable = false)
    private String verificationCode;

    @ManyToOne
    @JoinColumn(name = "batch_id")
    private Batch batch;

    @ManyToOne
    @JoinColumn(name = "drug_id")
    private Drug drug;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isConsumed = false;


    public void addSachet(Sachet sachet) {
        if (sachets == null) {
            sachets = new ArrayList<>();
        }
        this.sachets.add(sachet);
        sachet.setPack(this);
    }
}
