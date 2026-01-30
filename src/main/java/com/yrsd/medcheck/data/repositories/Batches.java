package com.yrsd.medcheck.data.repositories;

import com.yrsd.medcheck.data.models.Batch;
import com.yrsd.medcheck.data.models.Drug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Batches extends JpaRepository<Batch, String> {
    Optional<Batch> findByVerificationCode(String verificationCode);

    Optional<List<Batch>> findByDrug(Drug drug);
}
