package com.yrsd.medcheck.services;

import com.yrsd.medcheck.data.models.*;
import com.yrsd.medcheck.data.models.enums.OrganisationType;
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
import java.time.*;
import java.util.*;

import static com.yrsd.medcheck.utils.CodeGenerator.generateCode;
import static com.yrsd.medcheck.utils.Mutator.formatInstant;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BatchServiceImpl implements BatchService {

    private final Organisations organisations;
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
        log.debug("Service: Layer hit");
        UserAccount userAccount = userAccounts.findByUsername(request.getManufacturing_employee_Id()).orElseThrow(()
                -> new UsernameNotFoundException(request.getManufacturing_employee_Id()));


        Drug drug = drugs.findById(request.getDrugId())
                .orElseThrow(() -> new DrugDoesntExistException("Drug with ID " + request.getDrugId() + " not found"));


        log.info("Organisation = {}, Drug = {}, Employee Username = {}", drug.getManufacturer().getName(),
                drug.getBrandName() , request.getManufacturing_employee_Id());
        if (!drug.getManufacturer().equals(userAccount.getOrganisation())) {
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
        log.info("Transfer batch request = {}", request);
        Batch batch = batches.findById(request.getBatchId()).orElseThrow(() ->
                new BatchDoesntExistException("This batch doesn't exist."));
        UserAccount sender = userAccounts.findById(request.getSenderId()).orElseThrow(() ->
                new UsernameNotFoundException("User not found"));
        Organisation receiver = organisations.findById(request.getReceiverOrganisationId()).orElseThrow(() ->
                new OrganizationDoesntExistException("Organisation not found"));
        log.info(receiver.getOrganisationType().toString());
        validateAuthority(sender, batch, receiver);



        BatchLogistics batchLogistics = new BatchLogistics();
        batchLogistics.setBatch(batch);
        batchLogistics.setSender(sender.getOrganisation());
        batchLogistics.setRecipient(receiver);

        batchLogisticsRepo.save(batchLogistics);

        TransferBatchResponse response = new  TransferBatchResponse();
        response.setMessage("Batch transferred successfully");
        response.setTimestamp(LocalDateTime.now());
        return response;
    }

    @Override
    public TransferPackResponse transferPack(@NonNull TransferPackRequest request) {
        log.info("Transfer Pack Request Initiated");
        UserAccount sender = userAccounts.findByUsername(request.getSenderId()).orElseThrow(() -> new UsernameNotFoundException("Sender not found"));
        Organisation receiver =  organisations.findById(request.getReceiverId()).orElseThrow(() -> new OrganizationDoesntExistException("Organisation not found"));
        Pack pack = packs.findById(request.getPackId()).orElseThrow(() -> new PackDoesntExistException("Pack with ID " + request.getPackId() + " not found"));
        Batch batch = pack.getBatch();

        validateCustody(request, pack, sender, batch);
        PackLogistics packLogistics = buildPackLogisticsRepo(pack, sender, receiver);
        packLogisticsRepo.save(packLogistics);
        return buildResponse(receiver, sender);
    }

    public SellPackResponse sellPack(@NonNull SellPackRequest request) {

        Pack pack = packs.findById(request.getPackId())
                .orElseThrow(() -> new PackDoesntExistException("Pack not found"));

        UserAccount retailer = userAccounts.findById(request.getRetailerId())
                .orElseThrow(() -> new UsernameNotFoundException("Retailer not found"));


        if (retailer.getRole() != Role.RETAIL_EMPLOYEE) {
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
            String record = String.format("From %s to %s on %s", batchLogistics.getSender().getName(),
                    batchLogistics.getRecipient().getName(), formatInstant(batchLogistics.getCreated()));
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
        response.getDrug().put("manufacturer", drug.getManufacturer().getName());

        response.setPack(new HashMap<>());
        response.getPack().put("noOfPacks", String.valueOf(packList.size()));

        response.setSachet(new HashMap<>());
        response.getSachet().put("noOfSachets", String.valueOf(totalSachets));

        return response;
    }

    public AllBatchesResponse getAllBatches(AllBatchesRequest request){
        AllBatchesResponse response = new AllBatchesResponse();
        response.setBatches(new ArrayList<>());
        Drug drug = drugs.findById(request.getDrugId()).orElseThrow(() -> new DrugDoesntExistException("This drug doesn't exist."));
        List<Batch> batchList = batches.findByDrug(drug).orElseThrow(() -> new BatchDoesntExistException("This drug doesn't have any batches yet."));

        for (Batch batch : batchList) {
            List<String> drugDetails = new ArrayList<>();
            drugDetails.add(batch.getId());
            drugDetails.add(batch.getBatchIdentifier());
            response.getBatches().add(drugDetails);
        }
        return response;

    }

    public AllPackResponse getAllPacks(AllPackRequest request){
        AllPackResponse response = new AllPackResponse();
        response.setBatches(new ArrayList<>());
        List<Pack> packList = packs.findAllByBatch_Id(request.getBatchId()).orElseThrow(() -> new BatchDoesntExistException("This batch doesn't exist."));

        for (Pack pack : packList) {
            List<String> drugDetails = new ArrayList<>();
            drugDetails.add(pack.getId());
            drugDetails.add(pack.getPackIdentifier());
            response.getBatches().add(drugDetails);
        }
        return response;

    }

    @Override
    public VerifyPackResponse verifyPack(@NonNull VerifyPackRequest request) {
        log.info("Pack Verification Initiated");
        VerifyPackResponse response = new VerifyPackResponse();
        String verificationCode = request.getPackVerificationCode();

        Pack pack = packs.findByVerificationCode(verificationCode)
                .orElseThrow(() -> new PackDoesntExistException("Pack doesn't exist"));

        pack.setVerificationCount(pack.getVerificationCount().add(BigInteger.ONE));
        packs.save(pack);

        Drug drug = pack.getDrug();
        Batch batch = pack.getBatch();
        List<Sachet> sachetList = pack.getSachets();
        List<BatchLogistics> batchRecords = batchLogisticsRepo.findByBatchIdOrderByCreatedAsc(batch.getId())
                .orElse(new ArrayList<>());

        List<PackLogistics> packRecords = packLogisticsRepo.findByPackIdOrderByCreatedAsc(pack.getId())
                .orElse(new ArrayList<>());

        response.setPack(new HashMap<>());
        response.getPack().put("verificationCode", pack.getVerificationCode());
        response.getPack().put("packId", pack.getId());
        response.getPack().put("verificationCount", String.valueOf(pack.getVerificationCount().subtract(BigInteger.ONE)));

        response.setBatch(new HashMap<>());
        response.getBatch().put("batchId", batch.getId());
        response.getBatch().put("batchCode", batch.getVerificationCode());
        response.getBatch().put("expiryDate", batch.getExpirationDate().toString());

        response.setDrug(new HashMap<>());
        response.getDrug().put("brandName", drug.getBrandName());
        response.getDrug().put("genericName", drug.getGenericName());
        response.getDrug().put("manufacturer", drug.getManufacturer().getName());
        response.getDrug().put("nafdac", drug.getNafdacRegistrationNumber());
        response.getDrug().put("description", drug.getDescription());

        response.setSachet(new HashMap<>());
        response.getSachet().put("noOfSachets", String.valueOf(sachetList.size()));

        response.setHistory(new ArrayList<>());
        for (BatchLogistics batchLogistics : batchRecords) {
            String record = String.format("From %s to %s on %s", batchLogistics.getSender().getName(),
                    batchLogistics.getRecipient().getName(), formatInstant(batchLogistics.getCreated()));
            response.getHistory().add(record);
        }

        for (PackLogistics packLogistics : packRecords) {
            String record = String.format("From %s to %s on %s", packLogistics.getSender().getName(),
                    packLogistics.getRecipient().getName(), formatInstant(packLogistics.getCreated()));
            response.getHistory().add(record);
        }

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
        response.getDrug().put("manufacturer", drug.getManufacturer().getName());

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
            batch.setBatchIdentifier(drug.getDrugCode() + "B" + batches.count());

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
        long currentDbCount = packs.count();

        for (int index = 0; index < request.getAmountOfPacks(); index++) {
            Pack pack = new Pack();
            pack.setVerificationCode(drug.getDrugCode() + "P" + generateCode());
            pack.setVerificationCount(BigInteger.ZERO);
            pack.setDrug(drug);
            pack.setBatch(batch);


            long uniqueIdSuffix = currentDbCount + index;
            pack.setPackIdentifier(drug.getDrugCode() + "P" + uniqueIdSuffix);

            createSachets(request, drug, pack);
            batch.addPack(pack);
        }
    }

    private void validateRetailerCustody(Pack pack, UserAccount retailer) {

        Optional<PackLogistics> lastPackMovement = packLogisticsRepo.findTopByPackOrderByCreatedDesc(pack);

        if (lastPackMovement.isPresent()) {

            if (!lastPackMovement.get().getRecipient().equals(retailer.getOrganisation())) {
                throw new RestrictedTransferException("Custody Error: Your organisation doesn't own this pack.");
            }
            return;
        }

        Batch batch = pack.getBatch();
        BatchLogistics lastBatchMovement = batchLogisticsRepo.findTopByBatchOrderByCreatedDesc(batch)
                .orElseThrow(() -> new RestrictedTransferException("This batch is still with the manufacturer."));

        if (!lastBatchMovement.getRecipient().equals(retailer.getOrganisation())) {
            throw new RestrictedTransferException("Custody Error: You do not own the batch this pack belongs to.");
        }
    }

    private void validateCustody(Batch batch, UserAccount currentSender) {
        BatchLogistics lastMovement = batchLogisticsRepo.findTopByBatchOrderByCreatedDesc(batch)
                .orElseThrow(() -> new RestrictedTransferException("This batch has never been moved from the" +
                        " manufacturer yet."));


        if (!lastMovement.getRecipient().equals(currentSender.getOrganisation())) {
            throw new RestrictedTransferException(
                    "Custody Error: You cannot transfer a batch you do not currently possess. " +
                            "Current holder is: " + lastMovement.getRecipient().getName()
            );
        }
    }

    private void validateManufacturerTransfer(Batch batch, Organisation recipient) {

        boolean hasMovedBefore = batchLogisticsRepo.existsByBatch(batch);
        if (hasMovedBefore) {
            throw new RestrictedTransferException("This batch has already left the manufacturing plant.");
        }

        log.info(recipient.getOrganisationType().toString());

        if (!recipient.getOrganisationType().equals(OrganisationType.WHOLESALE)) {
            throw new RestrictedTransferException("Manufacturers can only transfer goods to wholesalers.");
        }
    }

    private void validateWholesalerTransfer(@NonNull Organisation recipient) {

        boolean isWholesaler = recipient.getOrganisationType() == OrganisationType.WHOLESALE;
        boolean isRetailer   = recipient.getOrganisationType() == OrganisationType.RETAIL;

        if (!isWholesaler && !isRetailer) {
            throw new RestrictedTransferException(
                    "Wholesalers can only transfer to other wholesalers or retailers."
            );
        }

    }

    private void validateAuthority(@NonNull UserAccount sender, Batch batch, Organisation receiver) {
        log.info(receiver.getOrganisationType().toString());
        if (sender.getRole().equals(Role.MANUFACTURING_EMPLOYEE)) {
            validateManufacturerTransfer(batch, receiver);
        }
        else if (sender.getRole().equals(Role.WHOLESALE_EMPLOYEE)) {
            validateWholesalerTransfer(receiver);
            validateCustody(batch, sender);
        }
        else {
            throw new RestrictedTransferException("Your role is not authorized to initiate transfers.");
        }

        if (receiver.getOrganisationType().equals(OrganisationType.MANUFACTURE)) {
            throw new RestrictedTransferException("Manufacturer cannot transfer to another manufacturer");
        }

//        if (!sender.getOrganisation().equals(batch.getDrug().getManufacturer())) {
//            throw new RestrictedTransferException("Only the manufacturer that created the batch can transfer it");
//        }
    }

    private static @NonNull PackLogistics buildPackLogisticsRepo(Pack pack, UserAccount sender, Organisation receiver) {
        PackLogistics packLogistics = new PackLogistics();
        packLogistics.setPack(pack);
        packLogistics.setSender(sender.getOrganisation());
        packLogistics.setRecipient(receiver);
        return packLogistics;
    }

    private static @NonNull TransferPackResponse buildResponse(Organisation receiver, UserAccount sender) {
        TransferPackResponse response = new TransferPackResponse();
        response.setMessage("Pack transfer successful");
        response.setReceiver(receiver.getName());
        response.setSender(sender.getUsername());
        response.setTimestamp(LocalDateTime.now());
        return response;
    }

    private void validateCustody(@NonNull TransferPackRequest request, Pack pack, UserAccount sender, Batch batch) {
        if (packLogisticsRepo.existsByPack(pack)) {
            PackLogistics record = packLogisticsRepo.findTopByPackOrderByCreatedDesc(pack).orElseThrow(()
                    -> new PackDoesntExistException("Pack with ID " + request.getPackId() + " not found"));
            if (!record.getRecipient().equals(sender.getOrganisation())) {
                throw new RestrictedTransferException("Unauthorized: Your organisation is not currently with this Pack");
            }

        } else if (batchLogisticsRepo.existsByBatch(batch)) {
            BatchLogistics record = batchLogisticsRepo.findTopByBatchOrderByCreatedDesc(batch).orElseThrow(() ->
                    new BatchDoesntExistException("Batch with ID " + request.getPackId() + " not found"));
            if (!record.getRecipient().equals(sender.getOrganisation())) {
                throw new RestrictedTransferException("Unauthorized: Your organisation is not currently with this Batch");
            } else if (!batchLogisticsRepo.existsBatchLogisticsByBatch(batch)) {
                throw new RestrictedTransferException("Unauthorized: Batch hasn't been released");
            }
        }
    }
}