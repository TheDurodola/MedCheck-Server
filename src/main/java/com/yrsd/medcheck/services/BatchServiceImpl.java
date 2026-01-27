package com.yrsd.medcheck.services;

import com.yrsd.medcheck.data.models.*;
import com.yrsd.medcheck.data.models.enums.Role;
import com.yrsd.medcheck.data.repositories.*;
import com.yrsd.medcheck.dtos.requests.*;
import com.yrsd.medcheck.dtos.responses.*;
import com.yrsd.medcheck.exceptions.*;
import com.yrsd.medcheck.services.interfaces.BatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.yrsd.medcheck.utils.CodeGenerator.generateCode;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BatchServiceImpl implements BatchService {

    private final UserAccounts userAccounts;
    private final Batches batches;
    private final Sachets sachets;
    private final Packs packs;
    private final Drugs drugs;
    private final BatchLogisticsRepo batchLogisticsRepo;
    private final PackLogisticsRepo packLogisticsRepo;
    private final ModelMapper modelMapper;

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
    public TransferBatchResponse transferBatch(@NonNull TransferBatchRequest request) {
        Batch batch = batches.findById(request.getBatchId()).orElseThrow(() ->
                new BatchDoesntExistException("This batch doesn't exist."));
        UserAccount sender = userAccounts.findByUsername(request.getSenderId()).orElseThrow(() ->
                new UsernameNotFoundException("User not found"));
        UserAccount receiver = userAccounts.findById(request.getReceiverId()).orElseThrow(() ->
                new UsernameNotFoundException("User not found"));

        if (sender.getRole() == Role.MANUFACTURER) {
            validateManufacturerTransfer(batch, receiver);
        }
        else if (sender.getRole() == Role.WHOLESALER) {
            validateWholesalerTransfer(receiver);
            validateCustody(batch, sender);
        }
        else {

            throw new RestrictedTransferException("Your role is not authorized to initiate transfers.");
        }

        if (receiver.getRole().toString().equals("MANUFACTURER")) {
            throw new RestrictedTransferException("Manufacturer cannot transfer to another Manufacturer");
        }

        if (!sender.getUsername().equals(batch.getDrug().getManufacturer().getUsername())) {
            throw new UnauthorizedException("Only The Manufacturer that created the Batch can transfer it");
        }

        BatchLogistics batchLogistics = new BatchLogistics();
        batchLogistics.setBatch(batch);
        batchLogistics.setSender(sender);
        batchLogistics.setRecipient(receiver);

        batchLogisticsRepo.save(batchLogistics);
        return null;
    }

    @Override
    public TransferPackResponse transferPack(@NonNull TransferPackRequest request) {
        Pack pack = packs.findById(request.getPackId()).orElseThrow(() ->
                new PackDoesntExistException("No Such Pack Exists"));
        Batch batch = pack.getBatch();
        if (!batchLogisticsRepo.existsBatchLogisticsByBatch(batch)) {
            throw new RestrictedTransferException("This pack is yet to be transfer from Manufacturer");
        }
        PackLogistics packLogistics = modelMapper.map(pack, PackLogistics.class);

        UserAccount sender = userAccounts.findById(request.getSenderId()).orElseThrow(() ->
                new UsernameNotFoundException("Sender not found"));
        UserAccount receiver = userAccounts.findById(request.getReceiverId()).orElseThrow(() ->
                new UsernameNotFoundException("Receiver not found"));

        if (sender.getRole().toString().equals("MANUFACTURER")) {
            throw new RestrictedTransferException("Manufacturers cannot transfer Packs, Only Batch");
        }
        packLogistics.setSender(sender);
        packLogistics.setRecipient(receiver);

        packLogisticsRepo.save(packLogistics);
        return null;
    }

    public SellPackResponse sellPack(@NonNull SellPackRequest request) {

        Pack pack = packs.findById(request.getPackId())
                .orElseThrow(() -> new PackDoesntExistException("Pack not found"));

        UserAccount retailer = userAccounts.findById(request.getRetailerId())
                .orElseThrow(() -> new UsernameNotFoundException("Retailer not found"));


        if (retailer.getRole() != Role.RETAILER) {
            throw new UnauthorizedException("Only Retailers can mark items as sold to consumers.");
        }


        if (pack.isSold()) {
            throw new RestrictedTransferException("This pack has already been sold!");
        }

        validateRetailerCustody(pack, retailer);


        pack.setSold(true);

        packs.save(pack);

        return new SellPackResponse("Pack marked as SOLD to consumer.");
    }

    @Override
    public VerifyBatchResponse verifyBatch(@NonNull VerifyBatchRequest request) {
        VerifyBatchResponse response = new VerifyBatchResponse();
        String verificationCode = request.getBatchVerificationCode();

        Batch batch = batches.findByVerificationCode(verificationCode)
                .orElseThrow(() -> new BatchDoesntExistException("Batch doesn't exist"));

        batch.setVerificationCount(batch.getVerificationCount().add(BigInteger.ONE));
        batches.save(batch);

        List<BatchLogistics> batchRecords = batchLogisticsRepo.findByBatchIdOrderByCreatedAsc(batch.getId())
                .orElse(new ArrayList<>());
        Drug drug = batch.getDrug();
        List<Pack> packList = batch.getPacks();

        response.setHistory(new ArrayList<>());
        for (BatchLogistics batchLogistics : batchRecords) {
            String record = String.format("From %s to %s on %s", batchLogistics.getSender().getUsername(),
                    batchLogistics.getRecipient().getUsername(), formatInstant(batchLogistics.getCreated()));
            response.getHistory().add(record);
        }


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
    public VerifyPackResponse verifyPack(@NonNull VerifyPackRequest request) {
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
    public VerifySachetResponse verifySachet(@NonNull VerifySachetRequest request) {
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

    private void validateRetailerCustody(Pack pack, UserAccount retailer) {

        Optional<PackLogistics> lastPackMovement = packLogisticsRepo.findTopByPackOrderByCreatedDesc(pack);

        if (lastPackMovement.isPresent()) {

            if (!lastPackMovement.get().getRecipient().equals(retailer)) {
                throw new RestrictedTransferException("Custody Error: You do not own this pack.");
            }
            return;
        }

        Batch batch = pack.getBatch();
        BatchLogistics lastBatchMovement = batchLogisticsRepo.findTopByBatchOrderByCreatedDesc(batch)
                .orElseThrow(() -> new RestrictedTransferException("This batch is still with the manufacturer."));

        if (!lastBatchMovement.getRecipient().equals(retailer)) {
            throw new RestrictedTransferException("Custody Error: You do not own the batch this pack belongs to.");
        }
    }

    private void validateCustody(Batch batch, UserAccount currentSender) {
        BatchLogistics lastMovement = batchLogisticsRepo.findTopByBatchOrderByCreatedDesc(batch)
                .orElseThrow(() -> new RestrictedTransferException("This batch has never been moved from the manufacturer yet."));


        if (!lastMovement.getRecipient().equals(currentSender)) {
            throw new RestrictedTransferException(
                    "Custody Error: You cannot transfer a batch you do not currently possess. " +
                            "Current holder is: " + lastMovement.getRecipient().getUsername()
            );
        }
    }

    private void validateManufacturerTransfer(Batch batch, UserAccount recipient) {

        boolean hasMovedBefore = batchLogisticsRepo.existsByBatch(batch);
        if (hasMovedBefore) {
            throw new RestrictedTransferException("This batch has already left the manufacturing plant.");
        }


        if (recipient.getRole() != Role.WHOLESALER) {
            throw new RestrictedTransferException("Manufacturers can only transfer goods to Wholesalers.");
        }
    }

    private void validateWholesalerTransfer(@NonNull UserAccount recipient) {

        boolean isWholesaler = recipient.getRole() == Role.WHOLESALER;
        boolean isRetailer   = recipient.getRole() == Role.RETAILER;

        if (!isWholesaler && !isRetailer) {
            throw new RestrictedTransferException(
                    "Wholesalers can only transfer to other Wholesalers or Retailers."
            );
        }

    }
    private @NonNull String formatInstant(Instant instant) {
        if (instant == null) return "N/A";

        ZoneId zoneId = ZoneId.of("Africa/Lagos");
        ZonedDateTime zdt = instant.atZone(zoneId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy hh:mm a");

        return zdt.format(formatter);
    }
}