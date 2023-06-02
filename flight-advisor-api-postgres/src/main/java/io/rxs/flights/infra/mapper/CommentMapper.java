package io.rxs.flights.infra.mapper;

import io.rxs.flights.domain.City;
import io.rxs.flights.domain.Comment;
import io.rxs.flights.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import io.rxs.flights.api.model.response.CommentResponse;
import io.rxs.flights.api.model.request.CommentUpSrtRequest;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring", imports = LocalDateTime.class)
public interface CommentMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "city", source = "city")
    Comment toModel(CommentUpSrtRequest request, User user, City city);
    
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", expression = "java( LocalDateTime.now() )")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "city", source = "city")
    @Mapping(target = "id", source = "commentId")
    Comment toUpdatedModel(CommentUpSrtRequest request, long commentId, User user, City city);
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "by", source = "user.fullName")
    CommentResponse toView(Comment comment);
    
    List<CommentResponse> toViews(List<Comment> comment);
}
