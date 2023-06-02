package io.rxs.flights.infra.mapper;

import io.rxs.flights.domain.Country;
import org.mapstruct.Mapper;
import io.rxs.flights.api.model.response.CountryResponse;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface CountryMapper {
    
    CountryResponse toView(Country country);
    
    Set<CountryResponse> toViews(Set<Country> countries);
}