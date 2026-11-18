package com.yrsd.medcheck.data.repositories;

import com.yrsd.medcheck.data.models.Batch;
import com.yrsd.medcheck.data.models.Drug;
import com.yrsd.medcheck.data.models.Organisation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Batches extends JpaRepository<Batch, String> {
    Optional<Batch> findByVerificationCode(String verificationCode);

    Optional<List<Batch>> findByDrug(Drug drug);

    @Query("SELECT DISTINCT b FROM Batch b WHERE " +
           "(b.drug.manufacturer = :organisation AND NOT EXISTS (SELECT bl FROM BatchLogistics bl WHERE bl.batch = b)) " +
           "OR b.id IN (" +
           "    SELECT bl1.batch.id FROM BatchLogistics bl1 " +
           "    WHERE bl1.recipient = :organisation " +
           "    AND bl1.created = (SELECT MAX(bl2.created) FROM BatchLogistics bl2 WHERE bl2.batch = bl1.batch)" +
           ")")
    List<Batch> findBatchesCurrentlyAssignedToOrganisation(@Param("organisation") Organisation organisation);
}
