package com.yrsd.medcheck.services;

import com.yrsd.medcheck.data.models.Drug;
import com.yrsd.medcheck.data.models.Organisation;
import com.yrsd.medcheck.data.models.UserAccount;
import com.yrsd.medcheck.data.repositories.Drugs;
import com.yrsd.medcheck.data.repositories.UserAccounts;
import com.yrsd.medcheck.dtos.requests.CreateDrugRequest;
import com.yrsd.medcheck.dtos.responses.AllBatchesResponse;
import com.yrsd.medcheck.dtos.responses.AllDrugsResponse;
import com.yrsd.medcheck.dtos.responses.CreateDrugResponse;
import com.yrsd.medcheck.exceptions.DrugDoesntExistException;
import com.yrsd.medcheck.exceptions.OrganizationDoesntExistException;
import com.yrsd.medcheck.services.interfaces.DrugService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.yrsd.medcheck.utils.CodeGenerator.generateCode;
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

        UserAccount user = userAccounts.findByUsername(currentUser).orElseThrow(()
                -> new UsernameNotFoundException("User not found"));
        Organisation organisation = user.getOrganisation();
        log.info("{} is attempting to create a drug", user.getUsername());
        Drug drug = modelMapper.map(request, Drug.class);

        buildDrug(request, drug, organisation);


        Drug savedDrug = drugs.save(drug);
        return modelMapper.map(savedDrug, CreateDrugResponse.class);
    }

    public AllDrugsResponse getAllDrug(String username){
        AllDrugsResponse response = new AllDrugsResponse();
        UserAccount userAccount = userAccounts.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found."));
        Organisation organisation = userAccount.getOrganisation();
        List<Drug> drugs1 = drugs.findByManufacturer(organisation).orElseThrow(() -> new DrugDoesntExistException("Manufacturer hasn't registered any drugs yet."));

        response.setDrugs(new ArrayList<>());
        for (Drug drug : drugs1) {
            List<String> drugDetails = new ArrayList<>();
            drugDetails.add(drug.getBrandName());
            drugDetails.add(drug.getId());
            drugDetails.add(drug.getGenericName());
            response.getDrugs().add(drugDetails);
        }

        return response;

    }
    private static void buildDrug(CreateDrugRequest request, Drug drug, Organisation organisation) {
        drug.setManufacturer(organisation);
        drug.setBrandName(toTitleCase(drug.getBrandName()));
        drug.setGenericName(toTitleCase(drug.getGenericName()));
        drug.setDrugCode(generateCode(drug.getBrandName()));
        drug.setDescription(toSentenceCase(drug.getDescription()));
        drug.setExpiryDurationInDays(request.getExpirationDurationInDays());
    }


}
