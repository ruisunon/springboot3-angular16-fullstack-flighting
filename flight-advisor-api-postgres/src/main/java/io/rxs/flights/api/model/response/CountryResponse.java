package io.rxs.flights.api.model.response;


import com.fasterxml.jackson.annotation.JsonProperty;

public record CountryResponse(@JsonProperty long id,
                              @JsonProperty String name) {
}
