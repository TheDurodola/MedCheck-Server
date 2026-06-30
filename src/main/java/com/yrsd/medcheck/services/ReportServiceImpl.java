package com.yrsd.medcheck.services;

import com.yrsd.medcheck.data.models.Report;
import com.yrsd.medcheck.data.models.enums.Status;
import com.yrsd.medcheck.data.repositories.*;
import com.yrsd.medcheck.dtos.requests.CreateReportRequest;
import com.yrsd.medcheck.dtos.responses.CreateReportResponse;
import com.yrsd.medcheck.exceptions.OrganizationDoesntExistException;
import com.yrsd.medcheck.exceptions.UserNotFoundException;
import com.yrsd.medcheck.services.interfaces.ReportService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final UserAccounts userAccounts;
    private final Organisations organisations;
    private final Drugs drugs;
    private final Batches  batches;
    private final Packs packs;
    private final Reports reports;
    private final ModelMapper mapper;


    public CreateReportResponse createReportForSachet(CreateReportRequest request){
        
        Report report = new Report();
        report.setStatus(Status.INITIATED);
        report.setUser(userAccounts
                .findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found")));
        report.setOrganisation(organisations
                .findById(request.getOrganisationId())
                .orElseThrow(() -> new OrganizationDoesntExistException("Organization doesn't exist")));
        report.setDescription(request.getDescription());

        reports.save(report);
        return new  CreateReportResponse();
    }





}
