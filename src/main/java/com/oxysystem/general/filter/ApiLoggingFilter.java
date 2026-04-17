package com.oxysystem.general.filter;

import com.oxysystem.general.model.tenant.ApiLog;
import com.oxysystem.general.repository.tenant.ApiLogRepository;
import com.oxysystem.general.wrapper.CachedBodyHttpServletRequest;
import com.oxysystem.general.wrapper.CachedBodyHttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

public class ApiLoggingFilter extends OncePerRequestFilter {
    private final ApiLogRepository apiLogRepository;
    private final static int MAX_RESPONSE_BODY_LENGTH = 10000;

    public ApiLoggingFilter(ApiLogRepository apiLogRepository) {
        this.apiLogRepository = apiLogRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        LocalDateTime timestamp = LocalDateTime.now();
        String endpoint = request.getRequestURI();

        // Wrap request to allow multiple reads
        CachedBodyHttpServletRequest wrappedRequest = new CachedBodyHttpServletRequest(request);
        String requestBody = wrappedRequest.getRequestBody(); // Read from cached body

        // Wrap response to capture the body
        CachedBodyHttpServletResponse wrappedResponse = new CachedBodyHttpServletResponse(response);

        String requestURI = request.getServletPath();
        if (requestURI.startsWith("/v3/api-docs")
                || requestURI.startsWith("/swagger-ui")
                || requestURI.startsWith("/swagger-resources")
                || requestURI.startsWith("/webjars")
                || requestURI.startsWith("/api/export")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (requestURI.startsWith("/api/grabmart-upload-item")) {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
            return;
        }
        // Proceed with the request
        filterChain.doFilter(wrappedRequest, wrappedResponse);

        String responseBody = wrappedResponse.getResponseBody();
        if(responseBody.length() > MAX_RESPONSE_BODY_LENGTH){
            responseBody = responseBody.substring(0, MAX_RESPONSE_BODY_LENGTH) + "...";
        }

        ApiLog log = new ApiLog();
        log.setTimestamp(timestamp);
        log.setReqName(endpoint);
        log.setReqHeader("");
        log.setReqBody(requestBody);
        log.setResBody(responseBody);

        apiLogRepository.save(log);

        wrappedResponse.copyBodyToResponse();
    }
}
