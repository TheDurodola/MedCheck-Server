package com.yrsd.medcheck.data.models;

import com.yrsd.medcheck.data.models.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.catalina.User;

@Entity
@Setter
@Getter
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserAccount user;

    @ManyToOne
    @JoinColumn(name = "organisation_id")
    private Organisation organisation;

    @ManyToOne
    @JoinColumn(name = "investigator_id")
    private UserAccount investigator;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String description;


}
