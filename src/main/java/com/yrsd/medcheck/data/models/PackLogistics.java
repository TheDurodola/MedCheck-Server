package com.yrsd.medcheck.data.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class PackLogistics {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;


    @ManyToOne
    @JoinColumn(name = "sender_id")
    private UserAccount sender;


    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private UserAccount recipient;


    @ManyToOne
    @JoinColumn(name = "pack_id")
    private Pack pack;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant created;
}
