package com.yrsd.medcheck.services;

import com.yrsd.medcheck.data.models.Batch;
import com.yrsd.medcheck.data.models.Drug;
import com.yrsd.medcheck.data.models.Pack;
import com.yrsd.medcheck.data.models.Sachet;
import com.yrsd.medcheck.data.repositories.*;
import com.yrsd.medcheck.dtos.requests.*;
import com.yrsd.medcheck.dtos.responses.*;
import com.yrsd.medcheck.exceptions.BatchDoesntExistException;
import com.yrsd.medcheck.exceptions.DrugDoesntExistException;
import com.yrsd.medcheck.exceptions.PackDoesntExistException;
import com.yrsd.medcheck.exceptions.SachetDoesntExistException;
import com.yrsd.medcheck.exceptions.UnauthorizedException;
import com.yrsd.medcheck.services.interfaces.BatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.*;

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
    private final BatchLogisticsRepo batchLogisticsRepo;
    private final PackLogisticsRepo packLogisticsRepo;

    @Override
    public CreateBatchResponse createBatch(@NonNull CreateBatchRequest request) {

        Drug drug = drugs.findById(request.getDrugId())
                .orElseThrow(() -> new DrugDoesntExistException("Drug with ID " + request.getDrugId() + " not found"));


        log.info("Drug = {}, Request = {}", drug.getManufacturer().getUsername(), request.getManufacturerId());
        if (!drug.getManufacturer().getUsername().equals(request.getManufacturerId())) {
            throw new UnauthorizedException("Unauthorized: Invalid Manufacturer");
        }

        CreateBatchResponse response = new CreateBatchResponse();
        response.setSachet(new HashSet<>());
        response.setPack(new HashSet<>());
        response.setBatch(new HashSet<>());

        response.setDrug(new HashMap<>());
        response.getDrug().put("drugDescription", drug.getDescription());
        response.getDrug().put("brandName", drug.getBrandName());
        response.getDrug().put("genericName", drug.getGenericName());
        response.getDrug().put("manufactureDate", LocalDate.now());
        response.setMessage("Batch created");
        response.getDrug().put("expiryDate", LocalDate.now().plusDays(drug.getExpiryDurationInDays()));

        createBatches(request, drug, response);
        return response;
    }

    @Override
    public TransferBatchResponse transferBatch(TransferBatchRequest request) {
        return null;
    }

    @Override
    public TransferPackResponse transferPack(TransferSachetRequest request) {
        return null;
    }

    private void createBatches(@NonNull CreateBatchRequest request, Drug drug, CreateBatchResponse response) {
        List<Batch> batchList = new ArrayList<>();

        for (int counter = 0; counter < request.getAmountOfBatches(); counter++) {
            Batch batch = new Batch();
            batch.setDrug(drug);
            batch.setExpirationDate(LocalDate.now().plusDays(drug.getExpiryDurationInDays()));
            batch.setManufactureDate(LocalDate.now());
            batch.setVerificationCount(BigInteger.ZERO);
            batch.setVerificationCode(drug.getDrugCode() + "B" + generateCode());

            createPacks(request, drug, batch);
            batchList.add(batch);
        }


        List<Batch> savedBatches = batches.saveAll(batchList);


        for (Batch savedBatch : savedBatches) {
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

    private void createPacks(@NonNull CreateBatchRequest request, Drug drug, Batch batch) {
        for (int index = 0; index < request.getAmountOfPacks(); index++) {
            Pack pack = new Pack();
            pack.setVerificationCode(drug.getDrugCode() + "P" + generateCode());
            pack.setVerificationCount(BigInteger.ZERO);
            pack.setDrug(drug);
            pack.setBatch(batch);
            createSachets(request, drug, pack);
            batch.addPack(pack);
        }
    }

    private void createSachets(@NonNull CreateBatchRequest request, Drug drug, Pack pack) {
        for (int count = 0; count < request.getAmountOfSachets(); count++) {
            Sachet sachet = new Sachet();
            sachet.setVerificationCode(drug.getDrugCode() + "S" + generateCode());
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

        Batch batch = batches.findByVerificationCode(verificationCode)
                .orElseThrow(() -> new BatchDoesntExistException("Batch doesn't exist"));

        batch.setVerificationCount(batch.getVerificationCount().add(BigInteger.ONE));
        batches.save(batch);

        Drug drug = batch.getDrug();
        List<Pack> packList = batch.getPacks();


        int totalSachets = packList.stream()
                .mapToInt(p -> p.getSachets().size())
                .sum();


        response.setBatch(new HashMap<>());
        response.getBatch().put("verificationCode", verificationCode);
        response.getBatch().put("batchId", batch.getId());
        response.getBatch().put("manufactureDate", batch.getManufactureDate().toString());
        response.getBatch().put("expiryDate", batch.getExpirationDate().toString());


        response.setDrug(new HashMap<>());
        response.getDrug().put("drugId", drug.getId());
        response.getDrug().put("brandName", drug.getBrandName());
        response.getDrug().put("genericName", drug.getGenericName());
        response.getDrug().put("nafdacRegistrationNumber", drug.getNafdacRegistrationNumber());
        response.getDrug().put("drugCode", drug.getDrugCode());
        response.getDrug().put("manufacturer", drug.getManufacturer().getUsername());

        response.setPack(new HashMap<>());
        response.getPack().put("noOfPacks", String.valueOf(packList.size()));

        response.setSachet(new HashMap<>());
        response.getSachet().put("noOfSachets", String.valueOf(totalSachets));

        return response;
    }

    @Override
    public VerifyPackResponse verifyPack(VerifyPackRequest request) {
        VerifyPackResponse response = new VerifyPackResponse();
        String verificationCode = request.getPackVerificationCode();

        Pack pack = packs.findByVerificationCode(verificationCode)
                .orElseThrow(() -> new PackDoesntExistException("Pack doesn't exist"));

        pack.setVerificationCount(pack.getVerificationCount().add(BigInteger.ONE));
        packs.save(pack);

        Drug drug = pack.getDrug();
        Batch batch = pack.getBatch();
        List<Sachet> sachetList = pack.getSachets();

        response.setPack(new HashMap<>());
        response.getPack().put("verificationCode", verificationCode);
        response.getPack().put("packId", pack.getId());
        response.getPack().put("verificationCount", pack.getVerificationCount().toString());

        response.setBatch(new HashMap<>());
        response.getBatch().put("batchId", batch.getId());
        response.getBatch().put("batchCode", batch.getVerificationCode());
        response.getBatch().put("expiryDate", batch.getExpirationDate().toString());

        response.setDrug(new HashMap<>());
        response.getDrug().put("brandName", drug.getBrandName());
        response.getDrug().put("genericName", drug.getGenericName());
        response.getDrug().put("manufacturer", drug.getManufacturer().getUsername());

        response.setSachet(new HashMap<>());
        response.getSachet().put("noOfSachets", String.valueOf(sachetList.size()));

        return response;
    }

    @Override
    public VerifySachetResponse verifySachet(VerifySachetRequest request) {
        VerifySachetResponse response = new VerifySachetResponse();
        String verificationCode = request.getSachetVerificationCode();

        Sachet sachet = sachets.findByVerificationCode(verificationCode)
                .orElseThrow(() -> new SachetDoesntExistException("Sachet doesn't exist"));

        sachet.setVerificationCount(sachet.getVerificationCount().add(BigInteger.ONE));
        sachets.save(sachet);

        Drug drug = sachet.getDrug();
        Pack pack = sachet.getPack();
        Batch batch = pack.getBatch();

        response.setSachet(new HashMap<>());
        response.getSachet().put("verificationCode", verificationCode);
        response.getSachet().put("sachetId", sachet.getId());
        response.getSachet().put("verificationCount", sachet.getVerificationCount().toString());

        response.setPack(new HashMap<>());
        response.getPack().put("packCode", pack.getVerificationCode());
        response.getPack().put("expiryDate", batch.getExpirationDate().toString());
        response.getPack().put("manufactureDate", batch.getCreated().toString());

        response.setDrug(new HashMap<>());
        response.getDrug().put("brandName", drug.getBrandName());
        response.getDrug().put("genericName", drug.getGenericName());
        response.getDrug().put("manufacturer", drug.getManufacturer().getUsername());

        return response;
    }


}