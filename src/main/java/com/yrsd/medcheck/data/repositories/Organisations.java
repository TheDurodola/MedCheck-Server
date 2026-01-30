package com.yrsd.medcheck.data.repositories;

import com.yrsd.medcheck.data.models.Organisation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Organisations extends JpaRepository<Organisation, String> {
//    Optional<Organisation> findOrganisationByUserAccount(String userAccountId);
}
