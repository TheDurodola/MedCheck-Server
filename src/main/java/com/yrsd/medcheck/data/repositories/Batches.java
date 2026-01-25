package com.yrsd.medcheck.data.repositories;

import com.yrsd.medcheck.data.models.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Batches extends JpaRepository<Batch, String> {
    Optional<Batch> findByVerificationCode(String verificationCode);
}
