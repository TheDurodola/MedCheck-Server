package com.yrsd.medcheck.events.consumers;

import com.yrsd.medcheck.events.UserRegisteredEvent;

public interface AuthEventConsumer {
    void listen(UserRegisteredEvent event);
}
