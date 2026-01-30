package com.yrsd.medcheck.utils;

import com.yrsd.medcheck.data.models.UserAccount;
import com.yrsd.medcheck.data.models.enums.AccountStatus;
import com.yrsd.medcheck.data.models.enums.Role;
import com.yrsd.medcheck.dtos.requests.RegisterUserRequest;
import com.yrsd.medcheck.exceptions.InvalidPhoneNumberException;
import org.jspecify.annotations.NonNull;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Mutator {

    public static String toSentenceCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = true;

        for (char c : input.toCharArray()) {
            if (Character.isLetter(c)) {
                if (capitalizeNext) {
                    result.append(Character.toUpperCase(c));
                    capitalizeNext = false;
                } else {
                    result.append(Character.toLowerCase(c));
                }
            } else {
                result.append(c);

                if (c == '.' || c == '!' || c == '?') {
                    capitalizeNext = true;
                }
            }
        }

        return result.toString();
    }

    public static String standardizePhoneNumber(String phone) {
        if (phone == null) return null;


        String digits = phone.replaceAll("\\D", "");


        if (digits.startsWith("0")) {

            digits = "234" + digits.substring(1);
        } else if (digits.startsWith("234")) {

        } else {

            if (digits.length() == 10) {
                digits = "234" + digits;
            }
        }


        if (digits.length() != 13) {
            throw new InvalidPhoneNumberException("Invalid number length after standardization: " + digits);
        }

        return digits;
    }

    public static String toTitleCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            } else {
                c = Character.toLowerCase(c);
            }
            titleCase.append(c);
        }

        return titleCase.toString();
    }

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

    public static @NonNull String formatInstant(Instant instant) {
        if (instant == null) return "N/A";

        ZoneId zoneId = ZoneId.of("Africa/Lagos");
        ZonedDateTime zdt = instant.atZone(zoneId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy hh:mm a");

        return zdt.format(formatter);
    }

    public static String removeWhitespace(String input) {
        if (input == null) return null;

        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (!Character.isWhitespace(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
