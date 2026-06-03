package com.yrsd.medcheck.services;

import com.yrsd.medcheck.data.models.Drug;
import com.yrsd.medcheck.data.models.UserAccount;
import com.yrsd.medcheck.data.models.enums.AccountStatus;
import com.yrsd.medcheck.data.models.enums.Gender;
import com.yrsd.medcheck.data.repositories.*;
import com.yrsd.medcheck.dtos.requests.CreateDrugRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DrugServiceImplTest {

    private static final Logger log = LoggerFactory.getLogger(DrugServiceImplTest.class);
    CreateDrugRequest request;
    UserAccount userAccount;


    @Mock
    private Sachets sachets;

    @Spy
    private ModelMapper modelMapper;

    @Mock
    private UserAccounts userAccounts;

    @Mock
    private Packs  packs;

    @Mock
    private Batches batches;

    @Mock
    private Drugs drugs;


    @InjectMocks
    private DrugServiceImpl drugService;

    @BeforeEach
    void setUp() {
         userAccount = new UserAccount();
        userAccount.setUsername("lord_boj");
        userAccount.setAccountStatus(AccountStatus.ACTIVE);
        userAccount.setGender(Gender.MALE);
        request = new CreateDrugRequest();
        request.setBrandName("Paracetamol");
        request.setGenericName("Acetaminophen");
        request.setExpirationDurationInDays(365);
        request.setDescription("common over-the-counter medicine for reducing fever and relieving mild to moderate pain " +
                "from headaches, muscle aches, toothaches, and colds, acting as an analgesic (pain reliever) and " +
                "antipyretic (fever reducer). Sold under names like Tylenol, it's available in many forms, but taking " +
                "too much can cause severe liver damage, so it's crucial to follow dosing instructions and check labels " +
                "for its presence in other medications like cold and flu remedies.");
        request.setNafdacRegistrationNumber("12345");
    }

    @Test
    public  void createDrug() {
        Drug drug = new Drug();
        drug.setDrugCode("PPP");
        drug.setId("blah");
        drug.setCreatedDate(Instant.now());

        when(drugs.save(any(Drug.class))).thenReturn(drug);
        when(userAccounts.findByUsername("lord_boj")).thenReturn(Optional.of(userAccount));
        String currentUser = "lord_boj";
        drugService.createDrug(request, currentUser);
        verify(drugs).save(any(Drug.class));
    }
    @Test
    void thatDrugHasTheRightDetails(){
        when(userAccounts.findByUsername("lord_boj")).thenReturn(Optional.of(userAccount));
        Drug drug = new Drug();
        drug.setDrugCode("PPP");
        drug.setId("blah");
        drug.setCreatedDate(Instant.now());

        when(drugs.save(any(Drug.class))).thenReturn(drug);
        String currentUser = "lord_boj";
        drugService.createDrug(request, currentUser);
        ArgumentCaptor<Drug> captor = ArgumentCaptor.forClass(Drug.class);
        verify(drugs).save(captor.capture());

        Drug value = captor.getValue();
        assertThat(value.getBrandName()).isEqualTo(request.getBrandName());
        assertThat(value.getGenericName()).isEqualTo(request.getGenericName());
        assertThat(value.getNafdacRegistrationNumber()).isEqualTo(request.getNafdacRegistrationNumber());

    }


    @Test
    void  thatDrugCodeIsGeneratedFromThreeLettersOfTheBrandName(){
        Drug drug = new Drug();
        drug.setDrugCode("PPP");
        drug.setId("blah");
        drug.setCreatedDate(Instant.now());

        when(drugs.save(any(Drug.class))).thenReturn(drug);
        when(userAccounts.findByUsername("lord_boj")).thenReturn(Optional.of(userAccount));
        String currentUser = "lord_boj";
        drugService.createDrug(request, currentUser);
        ArgumentCaptor<Drug> captor = ArgumentCaptor.forClass(Drug.class);
        verify(drugs).save(captor.capture());

        Drug value = captor.getValue();
        assertThat(value.getBrandName()).isEqualTo(request.getBrandName());
        assertThat(value.getDrugCode()).isNotNull();
        log.info(value.getDrugCode());
    }

}