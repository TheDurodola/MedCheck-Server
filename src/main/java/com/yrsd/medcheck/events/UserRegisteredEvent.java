package com.yrsd.medcheck.events;

public record UserRegisteredEvent(String firstName, String lastnName, String email) {
}
