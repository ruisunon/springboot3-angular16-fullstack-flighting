package io.rxs.flights.domain;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;

/**
 * @author Mohamed Taman
 * @version 1.0
 **/
@Entity
@Table(catalog = "FLIGHTDB", schema = "PUBLIC")
@Getter
@Setter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class Airport implements Serializable {
    
    @Serial
    private static final long serialVersionUID = 6913599167936010779L;

    @Id
    @Basic(optional = false)
    @Column(name = "AIRPORT_ID", nullable = false)
    private Long airportId;

    @NonNull
    @Basic(optional = false)
    @Column(nullable = false)
    private String name;

    @NonNull
    @Basic(optional = false)
    @Column(name = "CITY", nullable = false, length = 100)
    private String cityName;

    @NonNull
    @Basic(optional = false)
    @Column(name = "COUNTRY", nullable = false, length = 100)
    private String countryName;

    @Column(length = 3)
    private String iata;

    @Column(length = 4)
    private String icao;

    @NonNull
    @Basic(optional = false)
    @Column(nullable = false, precision = 12, scale = 6)
    private BigDecimal latitude;

    @NonNull
    @Basic(optional = false)
    @Column(nullable = false, precision = 12, scale = 6)
    private BigDecimal longitude;

    @NonNull
    private Integer altitude;

    @NonNull
    private Float timezone;

    @NonNull
    @Enumerated(STRING)
    @Basic(optional = false)
    @Column(nullable = false)
    private Dst dst;

    @NonNull
    @Column(length = 50)
    private String tz;

    @NonNull
    @Basic(optional = false)
    @Column(nullable = false, length = 50)
    private String type;

    @NonNull
    @Basic(optional = false)
    @Column(name = "DATA_SOURCE", nullable = false)
    private String dataSource;

    @OneToMany(mappedBy = "destinationAirport", fetch = LAZY)
    @ToString.Exclude
    private List<Route> destinationRoutes;

    @OneToMany(mappedBy = "sourceAirport", fetch = LAZY)
    @ToString.Exclude
    private List<Route> sourceRoutes;

    @JoinColumn(name = "CITY_ID", referencedColumnName = "ID", nullable = false)
    @ManyToOne(optional = false, fetch = LAZY)
    @ToString.Exclude
    private City city;

    @JoinColumn(name = "COUNTRY_ID", referencedColumnName = "ID", nullable = false)
    @ManyToOne(optional = false, fetch = LAZY)
    @ToString.Exclude
    private Country country;
    
    public Airport(Long airportId) {
        this.airportId = airportId;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        
        return Objects.equals(this.airportId, ((Airport) o).airportId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.latitude, this.longitude, this.altitude);
    }
    
    public enum Dst {
        E, A, S, O, Z, N, U
    }
}
