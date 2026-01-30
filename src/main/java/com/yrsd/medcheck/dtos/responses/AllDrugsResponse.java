package com.yrsd.medcheck.dtos.responses;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
public class AllDrugsResponse {
        private List<List<String>> drugs;
}
