package io.rxs.flights.api.model.upload.route;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.processor.PreAssignmentProcessor;
import io.rxs.flights.api.model.upload.converter.ConvertUnwantedStringsToDefault;
import lombok.Data;

/**
 * @author Mohamed Taman
 * @version 1.0
 **/

@Data
public class RouteDto {
    
    @CsvBindByPosition(position = 0)
    private String airlineCode;
    
    @PreAssignmentProcessor(processor = ConvertUnwantedStringsToDefault.class, paramString = "0")
    @CsvBindByPosition(position = 1)
    private Long airlineId;
    
    @CsvBindByPosition(position = 2)
    private String srcAirportCode;
    
    @PreAssignmentProcessor(processor = ConvertUnwantedStringsToDefault.class, paramString = "0")
    @CsvBindByPosition(position = 3)
    private Long srcAirportId;
    
    @CsvBindByPosition(position = 4)
    private String destAirportCode;
    
    @PreAssignmentProcessor(processor = ConvertUnwantedStringsToDefault.class, paramString = "0")
    @CsvBindByPosition(position = 5)
    private Long destAirportId;
    
    @PreAssignmentProcessor(processor = ConvertUnwantedStringsToDefault.class, paramString = "N")
    @CsvBindByPosition(position = 6)
    private String codeShare;
    
    @CsvBindByPosition(position = 7)
    private Integer stops;
    
    @CsvBindByPosition(position = 8)
    private String equipment;
    
    @CsvBindByPosition(position = 9)
    private double price;
}