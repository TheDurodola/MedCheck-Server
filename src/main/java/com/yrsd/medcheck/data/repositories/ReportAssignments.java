package com.yrsd.medcheck.data.repositories;

import com.yrsd.medcheck.data.models.Report;
import com.yrsd.medcheck.data.models.ReportAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportAssignments extends JpaRepository<ReportAssignment, String> {

    Optional<ReportAssignment> findFirstByReportOrderByAssignedAtDesc(Report report);
}
