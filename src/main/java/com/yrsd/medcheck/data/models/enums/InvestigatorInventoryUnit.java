package com.yrsd.medcheck.data.models.enums;

import com.yrsd.medcheck.data.models.InventoryUnit;
import com.yrsd.medcheck.data.models.UserAccount;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class InvestigatorInventoryUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;


    @ManyToOne
    @JoinColumn(name = "investigator_id")
    private UserAccount investigator;

    @ManyToOne
    @JoinColumn(name = "unit_id")
    private InventoryUnit unit;

}
