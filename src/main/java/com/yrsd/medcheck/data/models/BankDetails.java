package com.yrsd.medcheck.data.models;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class BankDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne
    @JoinColumn(name = "user_account_id")
    private UserAccount userAccount;

    private String bankName;

    private String accountNumber;
}
