package com.oxysystem.general.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oxysystem.general.filter.ApiLoggingFilter;
import com.oxysystem.general.filter.JwtAuthorizationFilter;
import com.oxysystem.general.filter.TenantFilter;
import com.oxysystem.general.repository.tenant.ApiLogRepository;
import com.oxysystem.general.service.CustomUserDetailsService;
import com.oxysystem.general.util.JwtUtil;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

@Configuration
public class FilterConfig {

    // --- BEAN DECLARATIONS ---
    @Bean
    public TenantFilter tenantFilter() {
        return new TenantFilter();
    }

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

    // --- REGISTRATIONS ---

    @Bean
    public FilterRegistrationBean<TenantFilter> tenantFilterRegistration(TenantFilter filter) {
        FilterRegistrationBean<TenantFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(filter);
        registration.setAsyncSupported(true);
        registration.addUrlPatterns("/*");
        registration.setOrder(1); // TenantFilter jalan PERTAMA
        return registration;
    }

    @Bean
    public FilterRegistrationBean<JwtAuthorizationFilter> jwtFilterRegistration(JwtAuthorizationFilter filter) {
        FilterRegistrationBean<JwtAuthorizationFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(filter);
        registration.setAsyncSupported(true);
        registration.addUrlPatterns("/*");
        // HAPUS setOrder jika filter ini dipanggil lagi lewat SecurityConfig .addFilterBefore()
        // Namun jika ingin didaftarkan di sini, gunakan Order 2.
        registration.setOrder(2);
        return registration;
    }

    @Bean
    public FilterRegistrationBean<ApiLoggingFilter> apiLoggingFilterRegistration(ApiLoggingFilter filter) {
        FilterRegistrationBean<ApiLoggingFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(filter);
        registration.setAsyncSupported(true);
        registration.addUrlPatterns("/*");
        registration.setOrder(3);
        return registration;
    }

    @Bean(name = "customSecurityFilterChainRegistration")
    public FilterRegistrationBean<DelegatingFilterProxy> securityFilterChainRegistration() {
        FilterRegistrationBean<DelegatingFilterProxy> registration = new FilterRegistrationBean<>();
        DelegatingFilterProxy proxy = new DelegatingFilterProxy("springSecurityFilterChain");
        registration.setFilter(proxy);
        registration.setAsyncSupported(true);
        registration.addUrlPatterns("/*");
        registration.setOrder(0); // Spring Security Filter Chain paling luar
        return registration;
    }
}