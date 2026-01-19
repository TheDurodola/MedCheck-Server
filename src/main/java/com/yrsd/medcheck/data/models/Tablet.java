package com.yrsd.medcheck.data.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Tablet {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;


    private String name;

    private String description;

    private String verificationCode;

    @CreatedDate
    private Instant created;
}
