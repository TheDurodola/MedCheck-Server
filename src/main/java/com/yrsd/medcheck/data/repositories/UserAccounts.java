package com.yrsd.medcheck.data.repositories;

import com.yrsd.medcheck.data.models.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccounts extends JpaRepository<UserAccount, String> {
    public boolean existsByEmail(String email);
    public boolean existsByUsername(String username);
}
