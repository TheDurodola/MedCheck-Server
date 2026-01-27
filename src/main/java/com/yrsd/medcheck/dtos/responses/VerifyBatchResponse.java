package com.yrsd.medcheck.dtos.responses;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
public class VerifyBatchResponse {
  private Map<String, String> drug;
  private Map<String, String> batch;
  private Map<String, String> pack;
  private Map<String, String> sachet;
  private List<String> history;

}
