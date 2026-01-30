package com.yrsd.medcheck.data.models;

import com.yrsd.medcheck.data.models.enums.OrganisationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
public class Organisation {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String organizationCode;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<UserAccount> userAccounts;

    @Enumerated(EnumType.STRING)
    private OrganisationType organisationType;

    @CreatedDate
    private Instant createdDate;

    @LastModifiedDate
    private Instant modifiedDate;


    public void addUserAccount(UserAccount user) {
        if (this.userAccounts == null) {
            this.userAccounts = new ArrayList<>();
        }
        this.userAccounts.add(user);

        user.setOrganisation(this);
    }
}
