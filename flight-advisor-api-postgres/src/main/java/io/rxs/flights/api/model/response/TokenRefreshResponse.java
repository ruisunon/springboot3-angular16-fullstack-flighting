package io.rxs.flights.api.model.response;

public record TokenRefreshResponse(
        String accessToken,
        String refreshToken) {
}