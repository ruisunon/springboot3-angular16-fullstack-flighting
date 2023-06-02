package io.rxs.fligh.reactive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableWebFlux
public class FlightingApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlightingApplication.class, args);
	}

}
