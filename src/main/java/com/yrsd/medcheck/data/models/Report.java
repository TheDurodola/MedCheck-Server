package com.yrsd.medcheck.data.models;

import com.yrsd.medcheck.data.models.enums.Status;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    @JoinColumn(name = "inventory_unit_id")
    private InventoryUnit  inventoryUnit;


    @Enumerated(EnumType.STRING)
    private Status status;

    private String description;


}
