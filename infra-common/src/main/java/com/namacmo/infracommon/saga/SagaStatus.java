package com.namacmo.infracommon.saga;

public enum SagaStatus {
    STARTED, FAILED, SUCCEEDED, PROCESSING, COMPENSATING, COMPENSATED
}
