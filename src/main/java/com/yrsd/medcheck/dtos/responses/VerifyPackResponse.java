package com.yrsd.medcheck.dtos.responses;

import com.yrsd.medcheck.dtos.DrugDetail;
import com.yrsd.medcheck.dtos.LogisticsHistory;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Setter
@Getter
public class VerifyPackResponse {

    private String id;
    private String verificationCode;
    private BigInteger verificationCount;

    private DrugDetail drugDetail;
    private LocalDate expiryDate;
    private LocalDate manufactureDate;
    private int noOfSachets;


    private List<LogisticsHistory> history;
}
