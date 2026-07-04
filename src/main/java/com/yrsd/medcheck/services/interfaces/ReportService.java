package com.yrsd.medcheck.services.interfaces;

import com.yrsd.medcheck.dtos.requests.*;
import com.yrsd.medcheck.dtos.responses.*;

public interface ReportService {
    CreateReportResponse createReport(CreateReportRequest reportRequest);
    TransferReportResponse transferReport(TransferReportRequest transferPackRequest);
    DenyReportResponse denyReport(DenyReportRequest request);
    ApproveReportResponse approveReport(ApproveReportRequest response);
    AssignInvestigatorResponse assignInvestigator(AssignInvestigatorRequest request);
}
