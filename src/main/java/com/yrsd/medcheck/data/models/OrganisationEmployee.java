package com.yrsd.medcheck.data.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;



@Entity
@Setter
@Getter
public class OrganisationEmployee {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private UserAccount employee;


    @ManyToOne
    @JoinColumn(name = "organisation_id")
    private Organisation organisation;
}
