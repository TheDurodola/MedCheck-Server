package com.yrsd.medcheck.data.repositories;

import com.yrsd.medcheck.data.models.Pack;
import com.yrsd.medcheck.data.models.Sachet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Packs extends JpaRepository<Pack, String> {
    Optional<Pack> findByVerificationCode(String verificationCode);

    Optional<List<Pack>> findAllByBatch_Id(String batchId);
}
