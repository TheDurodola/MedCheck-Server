package com.yrsd.medcheck.dtos.responses;

import com.yrsd.medcheck.dtos.DrugDetail;
import com.yrsd.medcheck.dtos.LogisticsHistory;
import com.yrsd.medcheck.dtos.PackDetail;
import com.yrsd.medcheck.dtos.SachetDetail;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Setter
@Getter
public class VerifySachetResponse {
    private String verificationCode;
    private BigInteger verificationCount;
    private DrugDetail drugdetail;
    private PackDetail packdetail;
    private SachetDetail sachetdetail;

    private List<LogisticsHistory> logisticsHistory;

    public VerifySachetResponse(String verificationCode, BigInteger verificationCount) {
        this.verificationCode = verificationCode;
        this.verificationCount = verificationCount;
    }
}
