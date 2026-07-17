package com.erm.backend.configuration;

import com.erm.backend.security.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig implements WebMvcConfigurer {

    @Autowired(required = false)
    private JwtInterceptor jwtInterceptor;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeHttpRequests()
            .anyRequest().permitAll();
        return http.build();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (jwtInterceptor != null) {
            registry.addInterceptor(jwtInterceptor)
                    .addPathPatterns("/**")
                    .excludePathPatterns("/doctor/login", "/patient/login");
        }
    }
}
