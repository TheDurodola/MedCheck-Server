package com.yrsd.medcheck.services;

import com.yrsd.medcheck.data.models.Batch;
import com.yrsd.medcheck.data.models.Drug;
import com.yrsd.medcheck.data.models.Pack;
import com.yrsd.medcheck.data.models.Sachet;
import com.yrsd.medcheck.data.repositories.Batches;
import com.yrsd.medcheck.data.repositories.Drugs;
import com.yrsd.medcheck.data.repositories.Packs;
import com.yrsd.medcheck.data.repositories.Sachets;
import com.yrsd.medcheck.dtos.requests.CreateBatchRequest;
import com.yrsd.medcheck.dtos.requests.VerifyBatchRequest;
import com.yrsd.medcheck.dtos.requests.VerifyPackRequest;
import com.yrsd.medcheck.dtos.requests.VerifySachetRequest;
import com.yrsd.medcheck.dtos.responses.CreateBatchResponse;
import com.yrsd.medcheck.dtos.responses.VerifyBatchResponse;
import com.yrsd.medcheck.dtos.responses.VerifyPackResponse;
import com.yrsd.medcheck.dtos.responses.VerifySachetResponse;
import com.yrsd.medcheck.exceptions.BatchDoesntExistException;
import com.yrsd.medcheck.exceptions.DrugDoesntExistException;
import com.yrsd.medcheck.exceptions.UnauthorizedException;
import com.yrsd.medcheck.services.interfaces.BatchService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static com.yrsd.medcheck.utils.CodeGenerator.generateCode;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BatchServiceImpl implements BatchService {

    private final Batches batches;
    private final Sachets sachets;
    private final Packs packs;
    private final Drugs drugs;

    @Override
    public CreateBatchResponse createBatch(@NonNull CreateBatchRequest request) {


        Drug drug = drugs.findById(request.getDrugId())
                .orElseThrow(() -> new DrugDoesntExistException("Drug with ID " + request.getDrugId() + " not found"));

        if (!drug.getManufacturer().getId().equals(request.getManufacturerId())) {
            throw new UnauthorizedException("Unauthorized: Invalid Manufacturer");
        }
        CreateBatchResponse response = new CreateBatchResponse();
        response.setSachet(new HashSet<>());
        response.setPack(new HashSet<>());
        response.setBatch(new HashSet<>());

        response.setDrug(new HashMap<>());
        response.getDrug().put("drugId", drug.getId());
        response.getDrug().put("brandName", drug.getBrandName());
        response.getDrug().put("genericName", drug.getGenericName());
        response.getDrug().put("manufactureDate", LocalDate.now());
        response.getDrug().put("expiryDate", LocalDate.now().plusDays(drug.getExpiryDurationInDays()));


        createBatches(request, drug, response);
        return response;
    }

    private void createBatches(@NonNull CreateBatchRequest request, Drug drug, CreateBatchResponse response) {
        for (int counter = 0; counter < request.getAmountOfBatches(); counter++) {
            log.info(String.valueOf(counter));
            Batch batch = new Batch();
            batch.setDrug(drug);
            batch.setExpirationDate(LocalDate.now().plusDays(drug.getExpiryDurationInDays()));
            batch.setManufactureDate(LocalDate.now());
            batch.setVerificationCount(BigInteger.ZERO);
            batch.setVerificationCode(drug.getDrugCode()+"B"+generateCode());
            createPacks(request, drug, batch);
            Batch savedBatch = batches.save(batch);
            log.info("Created Batch: {}", savedBatch.getVerificationCode());
            response.getBatch().add(savedBatch.getVerificationCode());

            if (savedBatch.getPacks() != null) {
                for (Pack pack : savedBatch.getPacks()) {
                    response.getPack().add(pack.getVerificationCode());

                    if (pack.getSachets() != null) {
                        for (Sachet sachet : pack.getSachets()) {
                            response.getSachet().add(sachet.getVerificationCode());
                        }
                    }
                }
            }
        }
    }

    private static void createPacks(@NonNull CreateBatchRequest request, Drug drug, Batch batch) {
        for (int index = 0; index < request.getAmountOfPacks(); index++) {
            Pack pack = new Pack();
            pack.setVerificationCode(drug.getDrugCode()+"P"+generateCode());
            pack.setVerificationCount(BigInteger.ZERO);
            pack.setDrug(drug);
            pack.setBatch(batch);
            createSachets(request, drug, pack);
            batch.addPack(pack);
        }
    }

    private static void createSachets(@NonNull CreateBatchRequest request, Drug drug, Pack pack) {
        for (int count = 0; count < request.getAmountOfSachets(); count++) {
            Sachet sachet = new Sachet();
            sachet.setVerificationCode(drug.getDrugCode()+"S"+generateCode());
            sachet.setVerificationCount(BigInteger.ZERO);
            sachet.setDrug(drug);
            sachet.setPack(pack);
            pack.addSachet(sachet);
        }
    }

    @Override
    public VerifyBatchResponse verifyBatch(VerifyBatchRequest request) {
        VerifyBatchResponse response = new VerifyBatchResponse();
        String verificationCode = request.getBatchVerificationCode();
        Batch batch = batches.findByVerificationCode(verificationCode).orElseThrow(() -> new BatchDoesntExistException("Batch doesn't exist"));
        batch.setVerificationCount(batch.getVerificationCount().add(BigInteger.ONE));
        batches.save(batch);

        Drug drug = batch.getDrug();
        List<Pack> packs = batch.getPacks();

        response.getBatch().put("verificationCode", verificationCode);
        response.getBatch().put("batchId", batch.getId());
        response.getBatch().put("manufactureDate", batch.getCreated().toString());
        response.getBatch().put("expiryDate", batch.getExpirationDate().toString());
        response.getBatch().put("lastVerifiedOn", batch.getLastModified().toString());

        response.getDrug().put("drugId", drug.getId());
        response.getDrug().put("brandName", drug.getBrandName());
        response.getDrug().put("genericName", drug.getGenericName());
        response.getDrug().put("nafdacRegistrationNumber", drug.getNafdacRegistrationNumber());
        response.getDrug().put("drugCode", drug.getDrugCode());
        response.getDrug().put("manufacture", drug.getManufacturer().getUsername());

        response.getPack().put("noOfPacks", String.valueOf(packs.size()));
        response.getSachet().put("noOfSachets",  String.valueOf(packs.size()));
        return response;
    }

    @Override
    public VerifyPackResponse verifyPack(VerifyPackRequest request) {
        return null;
    }

    @Override
    public VerifySachetResponse verifySachet(VerifySachetRequest request) {

        return null;
    }
}
