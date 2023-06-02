package io.rxs.flights.repository;

import lombok.NonNull;
import io.rxs.flights.domain.Route;
import io.rxs.flights.domain.RoutePK;
import io.rxs.flights.domain.vo.RouteView;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
@CacheConfig(cacheNames = "travels")
public interface RouteRepository extends CrudRepository<Route, RoutePK> {
    
    @Query(value = """
        SELECT new io.rxs.flights.domain.vo.RouteView
            (routePK.source, routePK.destination, price)
        FROM Route
        ORDER BY routePK.source ASC
        """)
    Set<RouteView> getAll();
    
    @Query(value = """
        SELECT sum(price)
        FROM Route
        WHERE routePK IN (:routePKs)
        """)
    double getTripCost(Iterable<RoutePK> routePKs);
    
    List<Route> findAllByRoutePKIn(Collection<RoutePK> routePK);
    
    @CacheEvict(key = "#p0.routePK.source + #p0.routePK.destination")
    @Override
    <S extends Route> @NonNull S save(@NonNull S s);
    
    @CacheEvict(allEntries = true)
    @Override
    <S extends Route> @NonNull Iterable<S> saveAll(@NonNull Iterable<S> iterable);
}
