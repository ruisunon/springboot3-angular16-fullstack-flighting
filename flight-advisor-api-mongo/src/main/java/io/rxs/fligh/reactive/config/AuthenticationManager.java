package io.rxs.fligh.reactive.config;

import io.rxs.fligh.reactive.auth.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * AuthenticationManager class
 * It is used in AuthenticationFilter.
 * */

@Component
@RequiredArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtTokenProvider jwtUtil;
    private final MapReactiveUserDetailsService userDetails;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getPrincipal().toString();
        try {
            String username = jwtUtil.getUsernameFromToken(token);
            Mono<UserDetails> userDetailsMono = userDetails.findByUsername(username);
            return userDetailsMono
                    .filter(Objects::nonNull)
                    .map(user -> new UsernamePasswordAuthenticationToken(user.getUsername(), token, user.getAuthorities()));
        } catch (Exception e) {
            return Mono.error(e);
        }
    }
}
