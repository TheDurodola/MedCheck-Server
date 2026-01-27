package com.yrsd.medcheck.data.repositories;

import com.yrsd.medcheck.data.models.Batch;
import com.yrsd.medcheck.data.models.BatchLogistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BatchLogisticsRepo extends JpaRepository<BatchLogistics, String> {
    Optional<List<BatchLogistics>> findByBatchIdOrderByCreatedAsc(String batchId);
    Optional<BatchLogistics> findTopByBatchOrderByCreatedDesc(Batch batch);

    boolean existsBatchLogisticsByBatch(Batch batch);
    boolean existsByBatch(Batch batch);
}
