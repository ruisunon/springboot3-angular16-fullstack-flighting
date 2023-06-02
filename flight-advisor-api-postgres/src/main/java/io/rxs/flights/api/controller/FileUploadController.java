package io.rxs.flights.api.controller;

import com.opencsv.bean.BeanVerifier;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import io.rxs.flights.domain.Country;
import io.rxs.flights.infra.mapper.AirportMapper;
import io.rxs.flights.infra.mapper.RouteMapper;
import io.rxs.flights.repository.AirportRepository;
import io.rxs.flights.repository.CityRepository;
import io.rxs.flights.repository.CountryRepository;
import io.rxs.flights.repository.RouteRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import io.rxs.flights.api.model.upload.airport.AirportDto;
import io.rxs.flights.api.model.upload.airport.verifer.AirportBeanVerifier;
import io.rxs.flights.api.model.upload.route.RouteDto;
import io.rxs.flights.api.model.upload.route.verifer.RouteBeanVerifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

/**
 * Files Upload controller used to handle all data feeding the system like airports and routes.
 *
 * @author Mohamed Taman
 * @version 1.0
 */

// TODO: Improve performance of the file processing.
@Log4j2
@Tag(name = "Files Upload Management",
        description = """
                A set of authorized file management APIs, used to feed the system with data
                files like airports and routes.""")
@SecurityRequirement(name = "bearer-key")
@RestController
@RequestMapping(value = "upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
@RequiredArgsConstructor
public class FileUploadController {

    private final RouteRepository routeRepository;
    private final AirportRepository airportRepository;
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final RouteMapper routeMapper;
    private final AirportMapper airportMapper;

    @Operation(summary = "Upload file that contains flights airports.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "OK; File is uploaded and parsed successfully.",
                            content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "400",
                            description = "Bad Request; File is not valid.",
                            content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "417",
                            description = "Expectation Failed; Error while parsing or processing the file.",
                            content = @Content(schema = @Schema(implementation = String.class)))})
    @PostMapping("airports")
    public ResponseEntity<String> uploadAirports(@Parameter(name = "file", required = true,
            description = "Select airport file to upload.") @RequestPart("file") MultipartFile file) {

        if (file.isEmpty())
            return new ResponseEntity<>("Please upload a valid file.", BAD_REQUEST);
        else {
            try {
                // convert `CsvToBean` object to list of airports
                List<AirportDto> airports = parseCsvContent(file.getInputStream(),
                        AirportDto.class, new AirportBeanVerifier());
                    
                /*
                 Save All Airports to database after:
                    1. Add countries and cities to database.
                    2. Add new added city and country ids to dto.
                    3. Map Airport Dto to Airport model.
                    4. Return all airports as list to be saved to DB.
                 */
                this.airportRepository.saveAll(
                        airports
                                .stream()// Save countries and cities to database
                                .map(airportDto -> {

                                    Country country = this.countryRepository
                                            .findOrSaveBy(airportDto.getCountry());

                                    airportDto.setCountryId(country.getId());

                                    airportDto.setCityId(this.cityRepository
                                            .findOrSaveBy(country, airportDto.getCity())
                                            .getId());

                                    // then converting to
                                    return this.airportMapper.toModel(airportDto);
                                })
                                // Then collect them to list
                                .toList());

            } catch (Exception ex) {
                log.error("Error while reading airports file.", ex);
                return new ResponseEntity<>("Error while reading or processing airport file.",
                        EXPECTATION_FAILED);
            }
        }

        return new ResponseEntity<>("Airports file uploaded successfully.", OK);
    }

    @Operation(summary = "Upload file that contains flights routes.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "OK; File is uploaded and parsed successfully.",
                            content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "400",
                            description = "Bad Request; File is not valid.",
                            content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "417",
                            description = "Expectation Failed; Error while parsing or processing the file.",
                            content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "424",
                            description = "Failed Dependency; System doesn't have any airports.",
                            content = @Content(schema = @Schema(implementation = String.class)))})
    @PostMapping("routes")
    public ResponseEntity<String> uploadRoutes(@Parameter(name = "file", required = true,
            description = "Select flights routes file to upload.")
                                               @RequestPart("file") MultipartFile file) {

        if (file.isEmpty())
            return new ResponseEntity<>("Please upload a valid file.", BAD_REQUEST);
        else if (this.airportRepository.count() == 0) {
            return new ResponseEntity<>("""
                    "Can't upload flight routes, system doesn't has Airports defined.
                    Please upload airport file, to feed the system with airports."
                    """, FAILED_DEPENDENCY);
        } else {

            // parse CSV file to create a list of `RouteDto` objects
            try {

                // convert `CsvToBean` object to list of routeDto
                List<RouteDto> routes = parseCsvContent(file.getInputStream(),
                        RouteDto.class, new RouteBeanVerifier());
                
                /*
                 1. Convert to Route model.
                 2. Save routes in DB.
                 */
                this.routeRepository
                        .saveAll(routes
                                .stream()
                                // filter routes doesn't exists
                                .filter(dto ->
                                        this.airportRepository
                                                .findById(dto.getSrcAirportId()).isPresent() &&
                                                this.airportRepository.findById(dto.getDestAirportId()).isPresent())
                                // converting to
                                .map(this.routeMapper::toModel)
                                // Then collect them to list
                                .toList());

            } catch (Exception ex) {
                log.error("Error while reading routes file.", ex);
                return new ResponseEntity<>("Error while reading or processing routes file.",
                        EXPECTATION_FAILED);
            }
        }

        return new ResponseEntity<>("Routes file uploaded successfully.", OK);
    }

    /**
     * This method is used to convert CSV content from any input stream to a bean list of type T.
     *
     * @param content      csv content from input stream.
     * @param clazz        the bean type class.
     * @param beanVerifier to verify and exclude unwanted data for a specific bean type.
     * @param <T>          is the bean type.
     * @return List of bean type T.
     * @throws IOException If any data is invalid or problem parsing the content of the input
     *                     stream.
     * @since v1.0
     */
    private <T> List<T> parseCsvContent(InputStream content, Class<T> clazz,
                                        BeanVerifier<T> beanVerifier) throws IOException {

        List<T> dtoList;
        // parse CSV file to create a list of `TypeDto` objects
        try (Reader reader = new BufferedReader(new InputStreamReader(content))) {
            // create csv bean reader
            CsvToBean<T> csvToBeans = new CsvToBeanBuilder<T>(reader)
                    .withType(clazz)
                    .withVerifier(beanVerifier)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            // convert `CsvToBean` object to a dto list
            dtoList = csvToBeans.parse();
        }
        return dtoList;
    }
}
