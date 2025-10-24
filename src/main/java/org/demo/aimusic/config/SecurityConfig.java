package org.demo.aimusic.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity // Enable Spring Security's web security support
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true) // Enable method-level security (e.g., @PreAuthorize, @Secured)
@RequiredArgsConstructor
public class SecurityConfig {
}
