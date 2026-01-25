package com.yrsd.medcheck.services;

import com.yrsd.medcheck.data.models.Drug;
import com.yrsd.medcheck.data.models.UserAccount;
import com.yrsd.medcheck.data.repositories.Drugs;
import com.yrsd.medcheck.data.repositories.UserAccounts;
import com.yrsd.medcheck.dtos.requests.CreateDrugRequest;
import com.yrsd.medcheck.dtos.responses.CreateDrugResponse;
import com.yrsd.medcheck.services.interfaces.DrugService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.yrsd.medcheck.utils.CodeGenerator.generateDrugCode;

@Slf4j
@Service
@RequiredArgsConstructor
public class DrugServiceImpl implements DrugService {

    private final Drugs drugs;
    private final UserAccounts userAccounts;
    private final ModelMapper modelMapper;

    @Override
    public CreateDrugResponse createDrug(CreateDrugRequest request, String currentUser) {
        CreateDrugResponse response = new CreateDrugResponse();
        UserAccount manufacturer = userAccounts.findByUsername(currentUser).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        log.info(manufacturer.getUsername());
        Drug drug = modelMapper.map(request, Drug.class);
        drug.setManufacturer(manufacturer);
        drug.setDrugCode(generateDrugCode(drug.getBrandName()));
        drugs.save(drug);
        return response;
    }



}
