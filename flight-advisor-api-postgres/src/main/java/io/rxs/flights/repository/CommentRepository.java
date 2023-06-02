package io.rxs.flights.repository;

import io.rxs.flights.domain.City;
import io.rxs.flights.domain.Comment;
import io.rxs.flights.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    Optional<Comment> findByIdAndCity(Long id, City city);
    
    Optional<Comment> findByIdAndCityAndUser(Long id, City city, User user);
    
    // Try to return a map of the selected city as a key and value as list<comments>
    @Query("""
            SELECT c
            FROM Comment c
            WHERE c.city = :city
            ORDER BY c.createdAt DESC, c.updatedAt DESC
        """)
    List<Comment> findByCity(@Param("city") City city, Pageable pageable);
}
