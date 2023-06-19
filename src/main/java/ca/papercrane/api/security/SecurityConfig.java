package ca.papercrane.api.security;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * The application security configuration.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * A list of end-points in which do not require user authentication to access.
     */
    private final List<String> UNAUTHORIZED_ENDPOINTS = List.of("/api/v1/login/**");

    /**
     * The filter that authenticates the JWT tokens.
     */
    private final AuthenticationFilter authenticationFilter;

    /**
     * The provider that performs authentication.
     */
    private final AuthenticationProvider authenticationProvider;

    /**
     * The handler that performs a users logout.
     */
    private final LogoutHandler logoutHandler;

    /**
     * Configures the security filter chain for the application.
     * Sets up authentication and authorization rules, as well as session management and logout handling.
     *
     * @param http The HttpSecurity object used to configure the filter chain.
     * @return The built SecurityFilterChain object.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf().disable();

        http.cors().configurationSource(corsConfigurationSource());

        http.authorizeHttpRequests().requestMatchers(UNAUTHORIZED_ENDPOINTS.toArray(new String[0])).permitAll().anyRequest().authenticated();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authenticationProvider(authenticationProvider);

        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http.logout().logoutUrl("/api/v1/logout").addLogoutHandler(logoutHandler).logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext());

        return http.build();
    }

    /**
     * Configures the CORS settings for the application.
     *
     * @return The registered configuration object.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        val configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);

        val source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);

        return source;
    }


}