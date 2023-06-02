package io.rxs.flights.infra.mapper;

import io.rxs.flights.domain.Route;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import io.rxs.flights.api.model.upload.route.RouteDto;

@Mapper(componentModel = "spring")
public interface RouteMapper {
    
    @Mapping(source = "srcAirportCode", target = "routePK.source")
    @Mapping(source = "destAirportCode", target = "routePK.destination")
    @Mapping(source = "srcAirportId", target = "sourceAirport.airportId")
    @Mapping(source = "destAirportId", target = "destinationAirport.airportId")
    Route toModel(RouteDto routeDto);
}