package com.yrsd.medcheck.data.repositories;

import com.yrsd.medcheck.data.models.Tablet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Tablets extends JpaRepository<Tablet, String> {
}
