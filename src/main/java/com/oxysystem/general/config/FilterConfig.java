package com.oxysystem.general.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oxysystem.general.filter.ApiLoggingFilter;
import com.oxysystem.general.filter.JwtAuthorizationFilter;
import com.oxysystem.general.repository.db1.ApiLogRepository;
import com.oxysystem.general.service.CustomUserDetailsService;
import com.oxysystem.general.util.JwtUtil;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;


@Configuration
public class FilterConfig {

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter(
            JwtUtil jwtUtil,
            ObjectMapper mapper,
            CustomUserDetailsService userDetailsService) {
        return new JwtAuthorizationFilter(jwtUtil, mapper, userDetailsService);
    }

    @Bean
    public ApiLoggingFilter apiLoggingFilter(ApiLogRepository apiLogRepository) {
        return new ApiLoggingFilter(apiLogRepository);
    }

    @Bean
    public FilterRegistrationBean<JwtAuthorizationFilter> jwtFilterRegistration(JwtAuthorizationFilter filter) {
        FilterRegistrationBean<JwtAuthorizationFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(filter);
        registration.setAsyncSupported(true); // ✅ Penting
        registration.addUrlPatterns("/*");
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public FilterRegistrationBean<ApiLoggingFilter> apiLoggingFilterRegistration(ApiLoggingFilter filter) {
        FilterRegistrationBean<ApiLoggingFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(filter);
        registration.setAsyncSupported(true); // ✅ Penting
        registration.addUrlPatterns("/*");
        registration.setOrder(2);
        return registration;
    }

    // Jika kamu masih menggunakan Spring Security:
    @Bean(name = "customSecurityFilterChainRegistration")
    public FilterRegistrationBean<DelegatingFilterProxy> securityFilterChainRegistration() {
        FilterRegistrationBean<DelegatingFilterProxy> registration = new FilterRegistrationBean<>();
        DelegatingFilterProxy proxy = new DelegatingFilterProxy("springSecurityFilterChain");
        registration.setFilter(proxy);
        registration.setAsyncSupported(true); // ✅ Wajib untuk StreamingResponseBody
        registration.addUrlPatterns("/*");
        registration.setOrder(0); // Biasanya paling awal
        return registration;
    }
}

