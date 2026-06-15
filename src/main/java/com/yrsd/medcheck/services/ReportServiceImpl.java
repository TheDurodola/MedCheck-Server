package com.yrsd.medcheck.services;

import com.yrsd.medcheck.data.repositories.Reports;
import com.yrsd.medcheck.services.interfaces.ReportService;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl implements ReportService {
    private final Reports reports;

    public ReportServiceImpl(Reports reports) {
        this.reports = reports;
    }

    
}
