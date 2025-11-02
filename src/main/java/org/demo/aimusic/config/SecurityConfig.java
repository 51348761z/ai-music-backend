package org.demo.aimusic.config;

import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.demo.aimusic.common.constants.SecurityConstant;
import org.demo.aimusic.module.auth.filter.JwtAuthenticationFilter;
import org.demo.aimusic.module.auth.handler.JwtAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Configures the security settings for the application. This class enables web security,
 * method-level security, and sets up JWT-based authentication, CORS policies, and public URL
 * endpoints.
 */
@Configuration
@EnableWebSecurity // Enable Spring Security's web security support
@EnableMethodSecurity(
    prePostEnabled = true,
    securedEnabled = true) // Enable method-level security (e.g., @PreAuthorize, @Secured)
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

  private static final String[] PUBLIC_URLS = SecurityConstant.PUBLIC_URLS;

  /**
   * Password encoder bean using BCrypt hashing algorithm.
   *
   * @return PasswordEncoder instance
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Exposes the AuthenticationManager as a Spring bean. This method retrieves the default
   * AuthenticationManager from Spring Security's AuthenticationConfiguration, which is then used by
   * the application to process authentication requests.
   *
   * @param authenticationConfiguration The configuration provided by Spring Security.
   * @return The configured AuthenticationManager instance.
   * @throws Exception if an error occurs while retrieving the AuthenticationManager.
   */
  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  /**
   * Configures the main security filter chain for the application. This method defines how HTTP
   * requests are secured, including disabling CSRF, setting up CORS, defining authorization rules
   * for different endpoints, configuring session management to be stateless, and adding the custom
   * JWT authentication filter.
   *
   * @param http The HttpSecurity object to be configured.
   * @return The configured SecurityFilterChain.
   * @throws Exception if an error occurs during the configuration.
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        // Disable CSRF as we are using JWT for authentication
        .csrf(AbstractHttpConfigurer::disable)
        // Configure CORS using the corsConfigurationSource bean
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        // Authorize requests
        .authorizeHttpRequests(
            authz -> authz.requestMatchers(PUBLIC_URLS).permitAll().anyRequest().authenticated())
        .exceptionHandling(
            exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  /**
   * Creates and configures the CORS (Cross-Origin Resource Sharing) settings. This bean defines
   * which origins, methods, and headers are allowed for cross-origin requests. It allows all
   * origins, common HTTP methods, and specific headers required for authentication and content
   * negotiation.
   *
   * @return A CorsConfigurationSource instance with the defined CORS policy.
   */
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOriginPatterns(
        Arrays.asList("http://localhost:3333", "http://localhost", "http://localhost:5176"));
    configuration.setAllowedMethods(
        Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
    configuration.setAllowedHeaders(
        Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin"));
    configuration.setAllowCredentials(true);
    configuration.setMaxAge(3600L);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
