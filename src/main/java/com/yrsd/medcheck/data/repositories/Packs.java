package com.yrsd.medcheck.data.repositories;

import com.yrsd.medcheck.data.models.Pack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Packs extends JpaRepository<Pack, String> {
}
