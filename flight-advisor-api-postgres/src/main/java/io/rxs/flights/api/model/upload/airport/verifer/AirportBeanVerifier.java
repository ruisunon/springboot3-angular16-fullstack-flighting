package io.rxs.flights.api.model.upload.airport.verifer;

import com.opencsv.bean.BeanVerifier;
import io.rxs.flights.api.model.upload.airport.AirportDto;

public class AirportBeanVerifier implements BeanVerifier<AirportDto> {
    @Override
    public boolean verifyBean(AirportDto airport) {
        return airport.getAirportId() != 0 &&
                   !airport.getCity().isBlank();
    }
}