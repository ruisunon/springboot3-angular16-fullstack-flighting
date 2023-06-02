package io.rxs.flights.infra.security;

import io.rxs.flights.infra.security.jwt.JwtTokenHelper;
import io.rxs.flights.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.springframework.util.StringUtils.hasText;

@Component
@Log4j2
@RequiredArgsConstructor
public class AuthJwtTokenFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain) throws ServletException, IOException {

        // Get authorization header and validate
        var authToken = getJwtAccessToken(request.getHeader(HttpHeaders.AUTHORIZATION));

        String jwtToken;

        if (authToken.isEmpty()) {
            chain.doFilter(request, response);
            return;
        } else jwtToken = authToken.get();

        // Get user identity and set it on the spring security context
        var userDetails = this.userRepository
                .findByUsernameIgnoreCase(JwtTokenHelper.getUsernameFrom(jwtToken))
                .orElse(null);

        var authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null,
                userDetails == null ? List.of() : userDetails.getAuthorities()
        );

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);
    }

    /**
     * This method take a header, and check if it contains authorization value,
     * if it is exists then validate this token.
     *
     * @param header that could contain authorization header
     * @return JWT token if exists and valid.
     */
    private Optional<String> getJwtAccessToken(String header) {
        //check header value is exists
        if (hasText(header) && header.startsWith(JwtTokenHelper.tokenPrefix())) {
            // Get jwt token and validate
            var token = header.split(" ")[1].trim();
            if (JwtTokenHelper.validate(token))
                return Optional.of(token);
        }
        return Optional.empty();
    }
}

