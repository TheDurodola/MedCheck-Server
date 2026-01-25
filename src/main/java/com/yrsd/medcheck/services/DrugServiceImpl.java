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
import org.springframework.transaction.annotation.Transactional;

import static com.yrsd.medcheck.utils.CodeGenerator.generateDrugCode;
import static com.yrsd.medcheck.utils.Mutator.toSentenceCase;
import static com.yrsd.medcheck.utils.Mutator.toTitleCase;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DrugServiceImpl implements DrugService {

    private final Drugs drugs;
    private final UserAccounts userAccounts;
    private final ModelMapper modelMapper;

    @Override
    public CreateDrugResponse createDrug(CreateDrugRequest request, String currentUser) {

        UserAccount manufacturer = userAccounts.findByUsername(currentUser).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        log.info("{} is attempting to create a drug", manufacturer.getUsername());


        Drug drug = modelMapper.map(request, Drug.class);
        drug.setManufacturer(manufacturer);
        drug.setBrandName(toTitleCase(drug.getBrandName()));
        drug.setGenericName(toTitleCase(drug.getGenericName()));
        drug.setDrugCode(generateDrugCode(drug.getBrandName()));
        drug.setDescription(toSentenceCase(drug.getDescription()));
        drug.setExpiryDurationInDays(request.getExpirationDurationInDays());
        Drug savedDrug = drugs.save(drug);
        return modelMapper.map(savedDrug, CreateDrugResponse.class);
    }



}
