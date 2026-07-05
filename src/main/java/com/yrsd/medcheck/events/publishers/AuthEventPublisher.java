package com.yrsd.medcheck.events.publishers;

import com.yrsd.medcheck.events.UserRegisteredEvent;

public interface AuthEventPublisher {
    void WelcomeEmailEvent(UserRegisteredEvent  event);
}
