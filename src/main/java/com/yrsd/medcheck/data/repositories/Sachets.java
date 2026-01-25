package com.yrsd.medcheck.data.repositories;

import com.yrsd.medcheck.data.models.Sachet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Sachets extends JpaRepository<Sachet, String> {
    Optional<Sachet> findByVerificationCode(String verificationCode);
}
