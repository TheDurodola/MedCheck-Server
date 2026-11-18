package com.yrsd.medcheck.data.repositories;

import com.yrsd.medcheck.data.models.Organisation;
import com.yrsd.medcheck.data.models.enums.OrganisationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Organisations extends JpaRepository<Organisation, String> {
//    Optional<Organisation> findOrganisationByUserAccount(String userAccountId);
List<Organisation> findAllByOrganisationType(OrganisationType type);
}
