package com.cognizant.labs.service;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface StreamPublisherService {

    @Output("personChannel")
    MessageChannel publish();
}
