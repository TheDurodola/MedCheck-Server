package com.yrsd.medcheck.data.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.List;

@Entity
@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Batch {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(unique = true, nullable = false)
    private String verificationCode;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "batch_id")
    private List<Pack> packs;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant created;

    @CreatedBy
    @Column(updatable = false)
    private String createdBy;
}
