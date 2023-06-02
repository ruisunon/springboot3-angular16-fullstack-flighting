package io.rxs.fligh.reactive.model;

import io.rxs.fligh.reactive.domain.UserInfo;

import java.time.LocalDateTime;

public interface PersistentEntity {

    String getId();

    void setId(String id);

    UserInfo getCreatedBy();

    void setCreatedBy(UserInfo username);

    UserInfo getLastModifiedBy();

    void setLastModifiedBy(UserInfo username);

    LocalDateTime getCreatedDate();

    void setCreatedDate(LocalDateTime createdDate);

    LocalDateTime getLastModifiedDate();

    void setLastModifiedDate(LocalDateTime lastModifiedDate);

}