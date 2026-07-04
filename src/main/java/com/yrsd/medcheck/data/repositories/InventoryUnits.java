package com.yrsd.medcheck.data.repositories;

import com.yrsd.medcheck.data.models.InventoryUnit;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryUnits extends JpaRepository<InventoryUnit, String> {
    @Override
    @NonNull
    Optional<InventoryUnit> findById(String s);
}
