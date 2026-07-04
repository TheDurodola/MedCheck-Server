package com.yrsd.medcheck.services;

import com.yrsd.medcheck.data.models.*;
import com.yrsd.medcheck.data.models.enums.Role;
import com.yrsd.medcheck.data.models.enums.Status;
import com.yrsd.medcheck.data.repositories.*;
import com.yrsd.medcheck.dtos.requests.*;
import com.yrsd.medcheck.dtos.responses.*;
import com.yrsd.medcheck.exceptions.*;
import com.yrsd.medcheck.services.interfaces.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final Reports reports;
    private final UserAccounts accounts;
    private final Organisations organisations;
    private final InventoryUnits inventoryUnits;
    private final ReportAssignments reportAssignments;


    @Override
    public CreateReportResponse createReport(CreateReportRequest request) {


        UserAccount userAccount = accounts.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Organisation organisation = organisations.findById(request.getOrganisationId())
                .orElseThrow(() -> new OrganizationDoesntExistException("Organisation not found"));

        InventoryUnit unit = inventoryUnits.findById(request.getUnitId())
                .orElseThrow(() -> new UnitNotFoundException("Unit not found"));


        Report report = new Report();
        report.setUser(userAccount);
        report.setOrganisation(organisation);
        report.setDescription(request.getDescription());
        report.setStatus(Status.INITIATED);
        report.setInventoryUnit(unit);
        reports.save(report);

        return null;
    }

    public AssignInvestigatorResponse assignInvestigator(AssignInvestigatorRequest request){
        if (reportAssignments.existsById(request.getUnitId())){
            throw new UnitUnassignableException("Unit has already been assigned to a investigator");
        }

        Report report = reports.findById(request.getUnitId())
                .orElseThrow(() -> new UnitNotFoundException("Unit not found"));

        UserAccount investigator = accounts.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!investigator.getRole().equals(Role.INVESTIGATOR)){
            throw new UnauthorizedException("Invalid Role");
        }


        ReportAssignment reportAssignment = new ReportAssignment();
        reportAssignment.setInvestigator(investigator);
        reportAssignment.setReport(report);
        reportAssignment.setActive(true);
        report.setStatus(Status.INVESTIGATING);


        reportAssignments.save(reportAssignment);
        reports.save(report);
        return AssignInvestigatorResponse
                .builder()
                .message("Successfully assigned investigator")
                .build();
    }


    @Override
    public TransferReportResponse transferReport(TransferReportRequest request) {

        Report report = reports.findById(request.getReportId()).orElseThrow(() -> new ReportNotFoundException("Report not found"));
        UserAccount sender = accounts.findById(request.getRecipientId()).orElseThrow(() -> new UserNotFoundException("Sender not found"));

        ReportAssignment reportAssignment = reportAssignments.findFirstByReportOrderByAssignedAtDesc(report)
                .orElseThrow(() -> new ReportNotFoundException("Report hasn't been assigned"));

        UserAccount receiver = accounts.findById(request.getSenderId())
                .orElseThrow(() -> new UserNotFoundException("Receiver not found"));

        if (sender.equals(reportAssignment.getInvestigator())){
            throw new UnauthorizedException("This investigator isn't in-charge of this report");
        }


        reportAssignment.setActive(false);
        reportAssignments.save(reportAssignment);


        ReportAssignment newAssignment = new ReportAssignment();
        newAssignment.setReport(report);
        newAssignment.setInvestigator(receiver);
        newAssignment.setActive(true);

        reportAssignments.save(newAssignment);


        return TransferReportResponse
                .builder()
                .message("Report successfully transferred to")
                .build();
    }

    @Override
    public DenyReportResponse denyReport(DenyReportRequest request) {

        Report report = reports.findById(request.getReportId())
                .orElseThrow(() -> new ReportNotFoundException("Report not found"));

        ReportAssignment reportAssignment = reportAssignments.findFirstByReportOrderByAssignedAtDesc(report)
                .orElseThrow(() -> new ReportNotFoundException("Report hasn't been assigned"));


        UserAccount investigator = accounts.findById(request.getInvestigatorId())
                .orElseThrow(() -> new UserNotFoundException("Investigator not found"));

        if (investigator.equals(reportAssignment.getInvestigator())){
            throw new UnauthorizedException("This investigator isn't in-charge of this report");
        }

        reportAssignment.setActive(false);
        report.setStatus(Status.DENIED);

        reportAssignments.save(reportAssignment);
        reports.save(report);

        //TODO: Integrate an email or push notification to
        // inform the user that made the report that it has been denied

        return DenyReportResponse
                .builder()
                .message("Report Denied")
                .build();
    }

    @Override
    public ApproveReportResponse approveReport(ApproveReportRequest request) {

        Report report = reports.findById(request.getReportId())
                .orElseThrow(() -> new ReportNotFoundException("Report not found"));

        ReportAssignment reportAssignment = reportAssignments.findFirstByReportOrderByAssignedAtDesc(report)
                .orElseThrow(() -> new ReportNotFoundException("Report hasn't been assigned"));


        UserAccount investigator = accounts.findById(request.getInvestigatorId())
                .orElseThrow(() -> new UserNotFoundException("Investigator not found"));

        if (investigator.equals(reportAssignment.getInvestigator())){
            throw new UnauthorizedException("This investigator isn't in-charge of this report");
        }

        reportAssignment.setActive(false);
        report.setStatus(Status.CONFIRMED);


        //TODO: Integrate an email or push notification to
        // inform the user that made the report that it has
        // been approve and that the payment is being processed


        return ApproveReportResponse
                .builder()
                .message("Report Approved, Payment Initiated")
                .build();
    }



}
