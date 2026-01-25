package com.yrsd.medcheck.data.repositories;

import com.yrsd.medcheck.data.models.Drug;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Drugs extends JpaRepository<Drug, String> {
}
