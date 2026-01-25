package com.yrsd.medcheck.data.repositories;

import com.yrsd.medcheck.data.models.BatchLogistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchLogisticsRepo extends JpaRepository<BatchLogistics, String> {
}
