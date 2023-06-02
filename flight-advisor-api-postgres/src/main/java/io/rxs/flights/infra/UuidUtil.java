package io.rxs.flights.infra;

import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class UuidUtil {
    
    public UUID newUuid() {
        return UUID.randomUUID();
    }

    public UUID uuidFrom(String uuid) {
        return UUID.fromString(uuid);
    }
}
