package com.yrsd.medcheck.data.repositories;

import com.yrsd.medcheck.data.models.Pack;
import com.yrsd.medcheck.data.models.PackLogistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PackLogisticsRepo extends JpaRepository<PackLogistics, String> {
    Optional<List<PackLogistics>> findByPackIdOrderByCreatedAsc(String packId);
    Optional<PackLogistics> findTopByPackOrderByCreatedDesc(Pack pack);
    Optional<PackLogistics> findByPack(Pack pack);
    boolean existsByPack(Pack pack);
}
