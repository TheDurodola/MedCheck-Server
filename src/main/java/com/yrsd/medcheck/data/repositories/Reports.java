package com.yrsd.medcheck.data.repositories;

import com.yrsd.medcheck.data.models.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Reports extends JpaRepository<Report, String> {
}
