package io.rxs.flights.api.model.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public record CityResponse(@JsonProperty long id,
                           @JsonProperty String name,
                           @JsonProperty String country,
                           @JsonProperty String description,
                           @JsonInclude(NON_NULL)
                       @JsonProperty List<CommentResponse> comments) {
}
