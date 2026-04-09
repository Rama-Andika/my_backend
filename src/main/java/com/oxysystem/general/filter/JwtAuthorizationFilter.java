package com.oxysystem.general.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oxysystem.general.exception.ResourceUnauthorizedException;
import com.oxysystem.general.response.FailedResponse;
import com.oxysystem.general.service.CustomUserDetailsService;
import com.oxysystem.general.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;



public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final ObjectMapper mapper;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, ObjectMapper mapper, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.mapper = mapper;
        this.userDetailsService = userDetailsService;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = header.substring(7);
            Claims claims = jwtUtil.parse(token);

            UserDetails userDetails;

            String username = claims.getSubject();
            if(username.contains("grab")){
                userDetails = new User("anonymousUser", "", Collections.emptyList());
            }else{
                userDetails = userDetailsService.loadUserByUsername(username);
            }
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);


        }catch (Exception e){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;

//            FailedResponse<?> failedResponse = new FailedResponse<>("Authentication error", e.getMessage());
//            response.setStatus(HttpStatus.UNAUTHORIZED.value());
//            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//
//            mapper.writeValue(response.getWriter(), failedResponse);

        }

        filterChain.doFilter(request, response);
    }
}
