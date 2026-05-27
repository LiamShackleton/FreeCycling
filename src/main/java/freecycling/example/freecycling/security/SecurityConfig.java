package freecycling.example.freecycling.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    JwtAuthFilter jwtAuthFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(management -> management
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
<<<<<<< Updated upstream
            .authorizeHttpRequests(authorize -> authorize
                // Public endpoints - no login required
                .requestMatchers(HttpMethod.POST, "/users", "/users/login").permitAll()
                .requestMatchers(HttpMethod.GET, "/transfersites", "/transfersites/{id}").permitAll()
                .requestMatchers(HttpMethod.GET, "/meetings/available").permitAll()
                .requestMatchers(HttpMethod.GET, "/items", "/items/{id}").permitAll()
                .requestMatchers(HttpMethod.GET, "/swagger-ui/*", "/v3/api-docs", "/v3/api-docs/*").permitAll()
                // Everything else requires authentication
=======
        .authorizeHttpRequests(authorize -> authorize.requestMatchers(HttpMethod.POST, "/users", "/users/login").permitAll()
        		.requestMatchers(HttpMethod.GET, "/items", "/items/getAvailable").permitAll()
                .requestMatchers(HttpMethod.GET,"/swagger-ui/*","/v3/api-docs","/v3/api-docs/*").permitAll()
>>>>>>> Stashed changes
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}