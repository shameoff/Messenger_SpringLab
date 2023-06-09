package ru.shameoff.jessenger.common.security;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ru.shameoff.jessenger.common.security.props.SecurityProps;

import java.util.Arrays;
import java.util.Objects;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableConfigurationProperties(SecurityProps.class)
public class SecurityConfig {
    private final SecurityProps securityProps;

    /**
     * TODO Настроить CORS корректным образом
     * Конфигурация, разрешающая отправлять любые запросы с любого адреса.
     * Приемлемо только во время разработки
     * @return
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Настроить разрешение CORS для эндпоинта /v3/api-docs
        return source;
    }

    /**
     * Цепочка для фильтрации запросов с JWT.
     * Ловит все запросы, которые начинаются на jwt-rootPath (указан в конфиге),
     * и применяет к ним фильтр для запросов с JWT,
     * исключая запросы, которые мы перечислили в конфиге jwtToken-permitAll
     */
    @Bean
    public SecurityFilterChain filterChainJwt(HttpSecurity http) throws Exception {
        http = http
                .cors()
                .and()
                .requestMatcher(createCustomReqMatcher(
                        securityProps.getJwtTokenProps().getRootPath(),
                        securityProps.getJwtTokenProps().getPermitAll()))
                .addFilterBefore(
                        new JwtTokenFilter(securityProps.getJwtTokenProps().getSecret()),
                        UsernamePasswordAuthenticationFilter.class
                )

                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();
        return http.build();
    }

    /**
     * <p>Цепочка для фильтрации интеграционных запросов </p>
     * <p>
     * Она ловит все запросы, которые начинаются на integration-rootPath (указан в конфиге)
     * и применяет к ним фильтр интеграций
     */
    @SneakyThrows
    @Bean
    public SecurityFilterChain filterChainIntegration(HttpSecurity http) {
        http = http
                .cors()
                .and()
                .requestMatcher(createCustomReqMatcher(securityProps.getIntegrationsProps().getRootPath()))
                .addFilterBefore(
                        new IntegrationFilter(securityProps.getIntegrationsProps().getApiKey()),
                        UsernamePasswordAuthenticationFilter.class
                )
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();
        return http.build();
    }

    /**
     * Метод для проверки пути сервлета, проверяет, что сервлет вообще есть, начинается c /root-path и отсутствует в permit-all
     *
     * @param rootPath   паттерн для заданного пути
     * @param ignorePath паттерн(ы) для игнорируемых путей
     * @return {@link RequestMatcher}
     */
    private RequestMatcher createCustomReqMatcher(String rootPath, String... ignorePath) {
        return req -> {
            var result = Objects.nonNull(req.getServletPath())
                    && req.getServletPath().startsWith(rootPath)
                    && Arrays.stream(ignorePath).noneMatch(item -> req.getServletPath().startsWith(item));
            return result;
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
