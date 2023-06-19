package ca.papercrane.api.security;

import ca.papercrane.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class AuthenticationConfig {

    private final UserRepository repository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Bean
    public PasswordEncoder passwordEncoder() {
        return passwordEncoder;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> repository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }

    @Bean
    public AuthenticationProvider loginAuthProvider() {
        val provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public ProviderManager providerManager() {
        return new ProviderManager(Collections.singletonList(loginAuthProvider()));
    }

}