package io.rxs.flights.service;

import io.rxs.flights.repository.AirportRepository;
import io.rxs.flights.repository.CityRepository;
import io.rxs.flights.repository.CommentRepository;
import io.rxs.flights.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import io.rxs.flights.api.model.request.CommentUpSrtRequest;
import io.rxs.flights.api.model.request.CreateCityRequest;
import io.rxs.flights.api.model.request.SearchAirportRequest;
import io.rxs.flights.api.model.request.SearchCityRequest;
import io.rxs.flights.api.model.response.AirportResponse;
import io.rxs.flights.api.model.response.CityResponse;
import io.rxs.flights.api.model.response.CommentResponse;
import io.rxs.flights.domain.City;
import io.rxs.flights.domain.Comment;
import io.rxs.flights.domain.Country;
import io.rxs.flights.domain.User;
import io.rxs.flights.infra.exception.NotAllowedException;
import io.rxs.flights.infra.exception.NotFoundException;
import io.rxs.flights.infra.mapper.AirportMapper;
import io.rxs.flights.infra.mapper.CityMapper;
import io.rxs.flights.infra.mapper.CommentMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Objects.isNull;

@RequiredArgsConstructor
@Log4j2
@Service
public class CityMgmtService {

    private static final String LIKE = "%";
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final CityMapper cityMapper;
    private final AirportRepository airportRepository;
    private final AirportMapper airportMapper;


    @Transactional
    public CityResponse addCity(CreateCityRequest cityRequest) {

        Country country;

        if (cityRequest.countryId() != 0)
            country =
                    this.countryRepository
                            .findById(cityRequest.countryId())
                            .orElseThrow(() -> new NotFoundException(Country.class,
                                    cityRequest.countryId()));
        else
            country = this.countryRepository.findOrSaveBy(cityRequest.country());


        City city = this.cityRepository.findOrSaveBy(country, cityRequest.name(),
                cityRequest.description());
        
        /*
        When we create a new city it doesn't have any comments,
        so don't show empty comments.
         */
        city.setComments(null);

        return this.cityMapper.toView(city);
    }

    @Transactional(readOnly = true)
    public List<CityResponse> searchCities(SearchCityRequest request, int cLimit) {

        var searchWord = LIKE
                .concat(isNull(request.name()) ?
                        "" :
                        request.name().strip())
                .concat(LIKE);

        if (cLimit > 0)
            return this.cityMapper.toViews(this.cityRepository
                    .findByNameIgnoreCaseIsLike(searchWord)
                    .stream()
                    .peek(city -> city
                            .setComments(this.commentRepository
                                    .findByCity(city, PageRequest.of(0, cLimit))))
                    .toList());
        else
            return this.cityMapper.toViews(this.cityRepository.findByNameIgnoreCaseIsLike(searchWord));

    }

    // Airport management

    public List<AirportResponse> searchAirports(SearchAirportRequest request, long cityId) {

        String searchWord = isNull(request.name()) ? "" : request.name();
        searchWord = LIKE.concat(searchWord).concat(LIKE);

        return this.airportMapper
                .toView(this.airportRepository
                        .findAirportsByCityAndNameIgnoreCaseIsLike(
                                new City(cityId), searchWord));
    }

    // City Comments management

    @Transactional
    public CommentResponse addComment(User user, int cityId,
                                      CommentUpSrtRequest request) {
        // Chick if the city is already exists
        City city = getCityIfExists(cityId);

        return this.commentMapper
                .toView(this.commentRepository
                        .save(this.commentMapper
                                .toModel(request, user, city)));
    }

    @Transactional
    public void updateComment(User user, long cityId, long commentId,
                              CommentUpSrtRequest request) {
        // Chick if the city is already exists
        City city = getCityIfExists(cityId);

        // If comment is exist then proceed
        this.commentRepository.findById(commentId)
                // Check if the comment is associated with given city
                .flatMap(comment ->
                        this.commentRepository.findByIdAndCity(comment.getId(), city))
                /* If exist check if user is allowed to update it */
                .map(found ->
                        this.commentRepository
                                .findByIdAndCityAndUser(found.getId(), city, user)
                                // If user is not allowed throw exception
                                .orElseThrow(() ->
                                        new NotAllowedException(Comment.class, found.getId(), "Update")))
                .ifPresent(comment -> this.commentMapper
                        .toView(this.commentRepository
                                .save(this.commentMapper
                                        .toUpdatedModel(request, comment.getId(), user, city))));
    }

    public void deleteComment(User user, long cityId, long commentId) {
        // Chick if the city is already exists
        City city = getCityIfExists(cityId);

        // In all cases found or not it will return 200 because delete is Idempotent
        // If comment is exist then proceed
        this.commentRepository.findById(commentId)
                // Check if the comment is associated with given city
                .flatMap(comment -> this.commentRepository.findByIdAndCity(comment.getId(), city))
                // If exist check if user is allowed to delete it
                .ifPresent(found -> this.commentRepository.delete(this.commentRepository
                        .findByIdAndCityAndUser(found.getId(), city, user)
                        // If user is not allowed throw exception
                        .orElseThrow(() -> new NotAllowedException(Comment.class, found.getId(), "Delete"))));
    }

    private City getCityIfExists(long cityId) {
        return this.cityRepository
                .findById(cityId)
                .orElseThrow(() -> new NotFoundException(City.class, cityId));
    }
}
