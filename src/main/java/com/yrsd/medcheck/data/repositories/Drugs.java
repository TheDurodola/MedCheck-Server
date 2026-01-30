package com.yrsd.medcheck.data.repositories;

import com.yrsd.medcheck.data.models.Drug;
import com.yrsd.medcheck.data.models.Organisation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface Drugs extends JpaRepository<Drug, String> {
    Optional<List<Drug>> findByManufacturer(Organisation manufacturer);
}
