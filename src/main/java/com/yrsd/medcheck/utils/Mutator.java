package com.yrsd.medcheck.utils;

import com.yrsd.medcheck.data.models.UserAccount;
import com.yrsd.medcheck.data.models.enums.AccountStatus;
import com.yrsd.medcheck.data.models.enums.Role;
import com.yrsd.medcheck.dtos.requests.RegisterUserRequest;

public class Mutator {
    public static void mutate(RegisterUserRequest request) {
        request.setUsername(request.getUsername().toLowerCase());
        request.setFirstName(request.getFirstName().toLowerCase());
        request.setLastName(request.getLastName().toLowerCase());
        request.setEmail(request.getEmail().toLowerCase());
        request.setMiddleName(request.getMiddleName().toLowerCase());
    }

    public static void mutate(UserAccount userAccount) {
        if (userAccount.getRole().equals(Role.CONSUMER)) {
            userAccount.setAccountStatus(AccountStatus.ACTIVE);
        }
        else {
            userAccount.setAccountStatus(AccountStatus.INACTIVE);
        }
        userAccount.setProfilePictureUrl("""
                https://res.cloudinary.com/ds1mdqmb9/image/upload/v1768808219/Twitter_default_profile_400x400_pwdjbz.png""");
    }
}
