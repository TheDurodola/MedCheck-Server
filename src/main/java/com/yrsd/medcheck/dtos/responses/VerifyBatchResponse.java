package com.yrsd.medcheck.dtos.responses;

import com.yrsd.medcheck.dtos.DrugDetail;
import com.yrsd.medcheck.dtos.LogisticsHistory;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Setter
@Getter
public class VerifyBatchResponse {
  private String id;

  private int noOfPacks;
  private LocalDate expiryDate;
  private DrugDetail drugDetail;
  private int noOfSachetsPerPack;
  private String verificationCode;
  private LocalDate manufactureDate;
  private List<LogisticsHistory> logisticsHistory;

}
