package com.edu.flysixbackend.config;

import com.edu.flysixbackend.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class CustomAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (request.getServletPath().startsWith("/auth")
                || request.getServletPath().equalsIgnoreCase("/payment/webhook")) {
            filterChain.doFilter(request, response);

        } else {

            String token = request.getHeader("AUTHORIZATION");
            boolean isError = false;
            String errorMsg = "";
            int errorStatus = 0;

            if (token != null) {

                try {
                    Authentication authentication = jwtUtil.authorizeWithToken(token);
                    log.info("Authentication successful via the token");
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    filterChain.doFilter(request, response);

                } catch (Exception ex){

                    log.error(ex.getMessage());
                    response.setHeader("error", ex.getMessage());
                    isError = true;
                    errorMsg = ex.getMessage();
                }
            } else {
                log.error("Bad Authorization header");
                response.setHeader("error", "Bad Authorization header");
                isError = true;
                errorMsg = "Bad Authorization header";

            }

            if (isError){
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                errorStatus = HttpStatus.UNAUTHORIZED.value();
                Map<String, String> error = new HashMap<>();

                error.put("error", errorMsg);
                error.put("status", Integer.toString(errorStatus));

                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        }
    }
}
