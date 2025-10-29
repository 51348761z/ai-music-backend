package org.demo.aimusic.module.auth.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.aimusic.common.constants.SecurityConstant;
import org.demo.aimusic.common.util.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final JwtUtil jwtUtil;
  private final UserDetailsService userDetailsService;

  private static final String AUTHORIZATION_HEADER = "Authorization";
  private static final String BEARER_PREFIX = "Bearer ";

  List<RequestMatcher> publicMatchers =
      Arrays.stream(SecurityConstant.LOGIN_WHITELIST)
          .map(url -> PathPatternRequestMatcher.withDefaults().matcher(url))
          .collect(Collectors.toList());

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {

    if (isPublicUrl(request)) {
      log.trace("Request to public URL [{}], skipping JWT filter.", request.getRequestURL());
      log.info("Request to public URL [{}], skipping JWT filter.", request.getRequestURL());
      filterChain.doFilter(request, response);
      return;
    }

    final String authHeader = request.getHeader(AUTHORIZATION_HEADER);
    final String jwt;
    final String userEmail; // using email as the username identifier
    log.info("Incoming request to URI: {}", request.getRequestURI());
    log.info("Authorization Header: {}", authHeader);

    // 1. Check if Authorization header exists and starts with specific prefix
    if (!StringUtils.hasText(authHeader) || !authHeader.startsWith(BEARER_PREFIX)) {
      filterChain.doFilter(request, response);
      return;
    }

    // 2. Extract JWT token
    jwt = authHeader.substring(BEARER_PREFIX.length());

    try {
      // 3. Extract user email from token
      userEmail = jwtUtil.extractUsername(jwt);

      // 4. Check if email is extracted and user is not already authenticate
      if (StringUtils.hasText(userEmail)
          && SecurityContextHolder.getContext().getAuthentication() == null) {

        // 5. Load UserDetails from the database using UserDetailService
        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

        // 6. Validate the token against the loaded UserDetails
        if (jwtUtil.validateToken(jwt, userDetails)) {
          // 7. If token is valid, create an Authentication object
          UsernamePasswordAuthenticationToken authToken =
              new UsernamePasswordAuthenticationToken(
                  userDetails,
                  null, // Credentials are null for JWT authentication
                  userDetails.getAuthorities() // User roles/permissions
                  );
          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          // 8. Set the Authentication object in the SecurityContextHolder
          SecurityContextHolder.getContext().setAuthentication(authToken);
          log.debug("User '{}' authenticated successfully via JWT", userEmail);
        } else {
          log.warn("JWT token validation failed for user '{}'", userEmail);
        }
      }
    } catch (ExpiredJwtException e) {
      log.warn("JWT token expired for user '{}'", e.getMessage());
    } catch (JwtException e) {
      log.error("Error parsing or validating JWT token: {}", e.getMessage());
    } catch (Exception e) {
      log.error("Could not set user authentication in security context", e);
    }

    // 9. Continue the filter chain
    filterChain.doFilter(request, response);
  }

  private boolean isPublicUrl(HttpServletRequest request) {
    return publicMatchers.stream().anyMatch(matcher -> matcher.matches(request));
  }
}
