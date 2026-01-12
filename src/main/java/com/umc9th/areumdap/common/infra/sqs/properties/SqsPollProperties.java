package com.umc9th.areumdap.common.infra.sqs.properties;

public interface SqsPollProperties {
    int maxMessages();
    int waitTimeSeconds();
}

