package com.yrsd.medcheck.data.repositories;

import com.yrsd.medcheck.data.models.PackLogistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackLogisticsRepo extends JpaRepository<PackLogistics, String> {
}
