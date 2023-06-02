package io.rxs.flights.api.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public record AirportResponse(@JsonInclude(NON_NULL)
                          @JsonProperty Long id,
                              @JsonProperty String airport,
                              @JsonProperty String city,
                              @JsonProperty String country,
                              @JsonInclude(NON_NULL)
                          @JsonProperty String iata,
                              @JsonInclude(NON_NULL)
                          @JsonProperty String icao) {
}



