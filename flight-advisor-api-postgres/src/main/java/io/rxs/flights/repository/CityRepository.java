package io.rxs.flights.repository;

import io.rxs.flights.domain.City;
import io.rxs.flights.domain.Country;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// FIXME Cache is not working properly
@Repository
@CacheConfig(cacheNames = "cities")
public interface CityRepository extends CrudRepository<City, Long> {
    @Cacheable
    Optional<City> findByCountryAndNameIgnoreCaseIsLike(Country country, String name);
    
    /**
     * Return the city if existed, else save and return it.
     *
     * @param name        of the country.
     * @param description of the city
     * @param country     of the city.
     * @return the found or saved country.
     */
    default City findOrSaveBy(Country country, String name, String description) {
        return findByCountryAndNameIgnoreCaseIsLike(country, name.trim())
                   .orElseGet(() -> save(new City(name, description, country)));
    }
    
    @Cacheable
    default City findOrSaveBy(Country country, String name) {
        return findOrSaveBy(country, name, null);
    }
    
    
    /**
     * Search cities by name, it is case-insensitive search.
     *
     * @param name to search city by.
     * @return list of found cities, or empty list if not found.
     */
    List<City> findByNameIgnoreCaseIsLike(String name);
}
