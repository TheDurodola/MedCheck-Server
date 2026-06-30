package com.yrsd.medcheck.data.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class InventoryUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

}
