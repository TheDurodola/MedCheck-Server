package com.yrsd.medcheck.services;

import com.yrsd.medcheck.config.MapperConfig;
import com.yrsd.medcheck.data.models.UserAccount;
import com.yrsd.medcheck.data.repositories.UserAccounts;
import com.yrsd.medcheck.dtos.requests.RegisterUserRequest;
import com.yrsd.medcheck.dtos.responses.CloudServiceResponse;
import com.yrsd.medcheck.dtos.responses.RegisterUserResponse;
import com.yrsd.medcheck.exceptions.*;
import com.yrsd.medcheck.proxy.cloud.CloudService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    private RegisterUserRequest request;
    private UserAccount saved;

    @Spy
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CloudService cloudService;

    @Mock
    private UserAccounts userAccounts;

    @Captor
    private ArgumentCaptor<UserAccount> userAccountCaptor;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();

        MapperConfig.configureMatchingStrategy(modelMapper);
        MapperConfig.configureForUserAccountToRegisterUserResponse(modelMapper);
        MapperConfig.configureForRegisterUserRequestToUserAccount(modelMapper);

        request = new RegisterUserRequest();
        request.setDateOfBirth(LocalDate.of(2000, 1, 1));
        request.setGender("MALE");
        request.setNationalIdentityNumber("123456789012");
        request.setRole("CONSUMER");
        request.setPhone("123456789");
        request.setEmail("johndoe@gmail.com");
        request.setUsername("johndoe");
        request.setPassword("Password123");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setMiddleName("John");
        saved = modelMapper.map(request, UserAccount.class);

    }


    @Test
    void saveAUserAccount() {
        when(userAccounts.save(any(UserAccount.class))).thenReturn(new UserAccount());
        authService.registerUser(request);
        verify(userAccounts, Mockito.times(1)).save(Mockito.any(UserAccount.class));
    }

    @Test
    void returnTheRightResponse() {
        
        when(userAccounts.save(any(UserAccount.class))).thenReturn(saved);
        RegisterUserResponse response = authService.registerUser(request);
        assertThat(response.getFirstName()).isNotNull();
        assertThat(response.getFirstName()).isEqualTo("John");
    }

    @Test
    void thatNecessaryFieldsAreTurnedToLowercaseForDatabase() {
        
        request.setUsername("JoHndoe");
        when(userAccounts.save(any(UserAccount.class))).thenReturn(saved);
        authService.registerUser(request);
        verify(userAccounts, Mockito.times(1)).save(userAccountCaptor.capture());
        assertThat(userAccountCaptor.getValue().getFirstName()).isEqualTo("john");
        assertThat(userAccountCaptor.getValue().getLastName()).isEqualTo("doe");
        assertThat(userAccountCaptor.getValue().getUsername()).isEqualTo("johndoe");
        assertThat(userAccountCaptor.getValue().getEmail()).isEqualTo("johndoe@gmail.com");
    }

    @Test
    void thatPasswordIsHashedBeforeSaving() {
        
        when(userAccounts.save(any(UserAccount.class))).thenReturn(saved);
        authService.registerUser(request);
        verify(userAccounts, Mockito.times(1)).save(userAccountCaptor.capture());
        assertThat(userAccountCaptor.getValue().getPassword()).isNotEqualTo("Password123");
    }

    @Test
    void thatUsernameIsUnique() {
        when(userAccounts.existsByUsername(any())).thenReturn(true);
        assertThatThrownBy(() -> authService.registerUser(request)).isInstanceOf(UsernameAlreadyExistException.class);
        verify(userAccounts, Mockito.times(0)).save(any(UserAccount.class));
    }

    @Test
    void thatEmailIsUnique() {
        when(userAccounts.existsByEmail(any())).thenReturn(true);
        assertThatThrownBy(() -> authService.registerUser(request)).isInstanceOf(EmailAlreadyExistException.class);
        verify(userAccounts, Mockito.times(0)).save(any(UserAccount.class));
    }

    @Test
    void thatMethodSavesAUserAccount() {
        
        when(userAccounts.save(Mockito.any(UserAccount.class))).thenReturn(Mockito.mock(UserAccount.class));
        RegisterUserResponse register = authService.registerUser(request);
        verify(userAccounts, Mockito.times(1)).save(Mockito.any(UserAccount.class));
    }

    @Test
    void thatMethodThrowsExceptionWhenAnNullFirstnameIsSent() {

        request.setFirstName(null);
        assertThatThrownBy(() -> authService.registerUser(request)).isInstanceOf(InvalidNameException.class);
    }

    @Test
    void thatMethodThrowsExceptionWhenANullLastNameIsSent() {
        request.setLastName(null);
        assertThatThrownBy(() -> authService.registerUser(request)).isInstanceOf(InvalidNameException.class);
    }

    @Test
    void thatMethodThrowsExceptionWhenANullDateOfBirthIsSent() {
        request.setDateOfBirth(null);
        assertThatThrownBy(() -> authService.registerUser(request)).isInstanceOf(InvalidDateOfBirthException.class);
    }
//
//    @Test
//    void thatMethodThrowsExceptionWhenANullProfilePictureIsSent(){
//        request.setProfilePicture(null);
//        assertThatThrownBy(()-> authService.registerUser(request)).isInstanceOf(InvalidProfilePictureException.class);
//    }


    @Test
    void thatMethodThrowsExceptionWhenANullEmailIsSent() {
        request.setEmail(null);
        assertThatThrownBy(() -> authService.registerUser(request)).isInstanceOf(InvalidEmailException.class);
    }

    @Test
    void thatMethodThrowsExceptionWhenANullUsernameIsSent() {
        request.setUsername(null);
        assertThatThrownBy(() -> authService.registerUser(request)).isInstanceOf(InvalidUsernameException.class);
    }


    @Test
    void thatMethodThrowsExceptionWhenANullPasswordIsSent() {
        request.setPassword(null);
        assertThatThrownBy(() -> authService.registerUser(request)).isInstanceOf(InvalidPasswordException.class);
    }

    @Test
    void thatMethodThrowsExceptionWhenANullGenderIsSent() {
        request.setGender(null);
        assertThatThrownBy(() -> authService.registerUser(request)).isInstanceOf(InvalidGenderException.class);
    }

    @Test
    void thatMethodPasswordMustBeMoreThanSixDigits() {
        request.setPassword("Passw");
        assertThatThrownBy(() -> authService.registerUser(request)).isInstanceOf(InvalidPasswordException.class);
    }

    @Test
    void thatExceptionIsThrownIfPasswordDoesntHaveAUpperCaseCharacter() {
        request.setPassword("password123");
        assertThatThrownBy(() -> authService.registerUser(request)).isInstanceOf(InvalidPasswordException.class);
    }

    @Test
    void thatExceptionIsThrownIfPasswordDoesntHaveANumberCharacter() {
        request.setPassword("Password");
        assertThatThrownBy(() -> authService.registerUser(request)).isInstanceOf(InvalidPasswordException.class);
    }

    @Test
    void thatExceptionIsThrownIfPasswordConsistOfOnlySpace() {
        request.setPassword("       ");
        assertThatThrownBy(() -> authService.registerUser(request)).isInstanceOf(InvalidPasswordException.class);
    }

    @Test
    void thatExceptionIsThrownIfUsernameIsLessThan4Characters() {
        request.setUsername("jon");
        assertThatThrownBy(() -> authService.registerUser(request)).isInstanceOf(InvalidUsernameException.class);
    }

    @Test
    void thatExceptionIsThrownWhenAUsernameWithAnInvalidCharacter() {
        
        request.setUsername("john*onestar");
        assertThatThrownBy(() -> authService.registerUser(request)).isInstanceOf(InvalidUsernameException.class);
        request.setUsername("john==onestar");
        assertThatThrownBy(() -> authService.registerUser(request)).isInstanceOf(InvalidUsernameException.class);
        request.setUsername("john#onestar");
        assertThatThrownBy(() -> authService.registerUser(request)).isInstanceOf(InvalidUsernameException.class);
        request.setUsername("john$%5onestar");
        assertThatThrownBy(() -> authService.registerUser(request)).isInstanceOf(InvalidUsernameException.class);
        request.setUsername("#johnonestar");
        assertThatThrownBy(() -> authService.registerUser(request)).isInstanceOf(InvalidUsernameException.class);
        request.setUsername("john_boj");
        when(userAccounts.save(any(UserAccount.class))).thenReturn(saved);
        assertDoesNotThrow(() -> authService.registerUser(request));
    }


    @Test
    void thatUserMustBeAbove12YearsOfAge() {
        request.setDateOfBirth(LocalDate.now().minusYears(11));
        assertThatThrownBy(() -> authService.registerUser(request)).isInstanceOf(InvalidDateOfBirthException.class);
        request.setDateOfBirth(LocalDate.now().minusYears(12));
        assertThatThrownBy(() -> authService.registerUser(request)).isInstanceOf(InvalidDateOfBirthException.class);
    }

    @Test
    void thatUserMustBeBelow100YearsOfAge() {
        request.setDateOfBirth(LocalDate.now().minusYears(110));
        assertThatThrownBy(() -> authService.registerUser(request)).isInstanceOf(InvalidDateOfBirthException.class);
    }


//    @Test
//    void thatOnlyImageFileAreAccepted(){
//        byte[] pdfSignature = new byte[]{ 0x25, 0x50, 0x44, 0x46, 0x2D };
//        MockMultipartFile pdfFile = new MockMultipartFile(
//                "profilePicture",
//                "contract.pdf",
//                "application/pdf",
//                pdfSignature
//        );
//        request.setProfilePicture(pdfFile);
//        assertThatThrownBy(()->  authService.registerUser(request)).isInstanceOf(InvalidProfilePictureException.class);
//    }

    @Test
    void thatFirstnameCannotConsistOfDigits() {
        request.setFirstName("John1");
        assertThatThrownBy(() -> authService.registerUser(request)).isInstanceOf(InvalidNameException.class);
    }

    @Test
    void thatLastnameCannotConsistOfDigits() {
        request.setLastName("John2");
        assertThatThrownBy(() -> authService.registerUser(request)).isInstanceOf(InvalidNameException.class);
    }

    @Test
    void thatLastnameCanContainHyphen() {
        
        request.setLastName("Adeniyi-Oso");
        when(userAccounts.save(any(UserAccount.class))).thenReturn(saved);
        assertDoesNotThrow(() -> authService.registerUser(request));
    }

    @Test
    void testThatGenderCannotBeNull() {

        request.setGender(null);

        assertThatThrownBy(() -> authService.registerUser(request)).isInstanceOf(InvalidGenderException.class);
    }

    @Test
    void thatGenderCannotBeEmpty() {
        request.setGender(" ");
        assertThatThrownBy(() -> authService.registerUser(request)).isInstanceOf(InvalidGenderException.class);
    }

    @Test
    void thatEmailMustContainAtSign() {
        request.setEmail("bolajidurodolagmail.com");
        assertThatThrownBy(() -> authService.registerUser(request)).isInstanceOf(InvalidEmailException.class);
    }

    @Test
    void thatEmailCannotContainDoubleAtSign() {
        request.setEmail("bolajidurodola@@gmail.com");
        assertThatThrownBy(() -> authService.registerUser(request)).isInstanceOf(InvalidEmailException.class);
    }

    @Test
    void thatEmailCannotBeEmpty() {
        request.setEmail("bolajidurodola@gmail");
        assertThatThrownBy(() -> authService.registerUser(request)).isInstanceOf(InvalidEmailException.class);
    }

    @Test
    void thatLastnameIsUpdateToLowercaseBeforeBeenSavedInTheDatabase() {
        
        when(userAccounts.save(any(UserAccount.class))).thenReturn(saved);
        ArgumentCaptor<UserAccount> captor = ArgumentCaptor.forClass(UserAccount.class);
        authService.registerUser(request);
        verify(userAccounts).save(captor.capture());
        UserAccount user = captor.getValue();
        assertThat(user.getLastName()).isEqualTo("doe");
    }

    @Test
    void thatFirstnameIsUpdateToLowercaseBeforeBeenSavedInTheDatabase() {
        when(userAccounts.save(any(UserAccount.class))).thenReturn(saved);
        ArgumentCaptor<UserAccount> captor = ArgumentCaptor.forClass(UserAccount.class);
        authService.registerUser(request);
        verify(userAccounts).save(captor.capture());
        UserAccount user = captor.getValue();
        assertThat(user.getFirstName()).isEqualTo("john");
    }

    @Test
    void thatUsernameIsUpdateToLowercaseBeforeBeenSavedInTheDatabase() {
        
        when(userAccounts.save(any(UserAccount.class))).thenReturn(saved);
        request.setUsername("JOHN");
        ArgumentCaptor<UserAccount> captor = ArgumentCaptor.forClass(UserAccount.class);
        authService.registerUser(request);
        verify(userAccounts).save(captor.capture());
        UserAccount user = captor.getValue();
        assertThat(user.getUsername()).isLowerCase();
    }

    @Test
    void thatEmailIsUpdateToLowercaseBeforeBeenSavedInTheDatabase() {
        
        when(userAccounts.save(any(UserAccount.class))).thenReturn(saved);
        request.setEmail("BOLAJIdurodola@gmail.com");
        ArgumentCaptor<UserAccount> captor = ArgumentCaptor.forClass(UserAccount.class);
        authService.registerUser(request);
        verify(userAccounts).save(captor.capture());
        UserAccount user = captor.getValue();
        assertThat(user.getEmail()).isLowerCase();
    }

    @Test
    void thatPasswordIsHashed() {
        
        when(passwordEncoder.encode(any(String.class))).thenReturn("hashed_value_123");
        when(userAccounts.save(any(UserAccount.class))).thenReturn(saved);
        authService.registerUser(request);
        verify(userAccounts).save(userAccountCaptor.capture());
        UserAccount user = userAccountCaptor.getValue();
        assertThat(user.getPassword()).isNotEqualTo(request.getPassword());
        assertThat(user.getPassword()).isNotNull();
        assertThat(user.getPassword().length()).isNotEqualTo(10);
    }


}