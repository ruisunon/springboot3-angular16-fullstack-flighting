package io.rxs.fligh.reactive.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

public record UserInfo (String username, String email){

  public static String UNKNOWN_EMAIL = "Unknown";
  public static String UNNAMED = "Unnamed";
  public UserInfo {
    Objects.requireNonNull(username);
    Objects.requireNonNull(email);
  }

  public UserInfo(String name) {
    this(name, UNKNOWN_EMAIL);
  }

  public static UserInfo unnamed(String email) {
    return new UserInfo(UNNAMED, email);
  }
}
