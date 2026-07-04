package com.yrsd.medcheck.services;

import com.yrsd.medcheck.data.models.Organisation;
import com.yrsd.medcheck.data.models.Report;
import com.yrsd.medcheck.data.models.UserAccount;
import com.yrsd.medcheck.data.models.enums.AccountStatus;
import com.yrsd.medcheck.data.models.enums.Role;
import com.yrsd.medcheck.data.repositories.Reports;
import com.yrsd.medcheck.data.repositories.UserAccounts;
import com.yrsd.medcheck.dtos.requests.CreateReportRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {

    @InjectMocks
    private ReportServiceImpl reportService;

    @Mock
    private Reports reports;

    @Mock
    private UserAccounts accounts;


    private UserAccount complainant;
    private Organisation organisation;

    @BeforeEach
    void setUp() {
        complainant = new UserAccount();
        complainant.setUsername("username");
        complainant.setPassword("password");
        complainant.setEmail("email");
        complainant.setId("1");
        complainant.setRole(Role.CONSUMER);
        complainant.setPhoneNumber("08141234567");
        complainant.setNationalIdentityNumber("123456789");
        complainant.setDateOfBirth(LocalDate.of(1990, 1, 1));
        complainant.setAccountStatus(AccountStatus.ACTIVE);
        complainant.setCreatedDate(Instant.now());

    }

    @Test
    public void reportCanBeCreated(){
        CreateReportRequest reportRequest = CreateReportRequest.builder().userId("1").build();
        reportService.createReport(reportRequest);
        verify(reports, times(1)).save(any(Report.class));

    }

    @Test
    public void reportCreatorIsAssigned(){

        CreateReportRequest reportRequest = CreateReportRequest.builder().userId(complainant.getId()).build();

        when(accounts.findById(reportRequest.getUserId())).thenReturn(Optional.of(complainant));
        reportService.createReport(reportRequest);

        ArgumentCaptor<Report> captor = ArgumentCaptor.forClass(Report.class);
        verify(reports, times(1)).save(captor.capture());

        Report savedReport = captor.getValue();

        assertEquals(savedReport.getUser(), complainant);
    }








}