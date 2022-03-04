package com.edu.flysixbackend.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.edu.flysixbackend.dto.AuthUserDto;
import com.edu.flysixbackend.dto.ResponseDto;
import com.edu.flysixbackend.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private final AuthenticationManager authenticationManager;
    private final Algorithm algorithm = Algorithm.HMAC256("FlySixSecured!".getBytes());

    public JwtUtil(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public ResponseDto authenticate(User user) {
        Authentication authentication = this.attemptAuthentication(user);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = this.successfulAuthentication(authentication);
        Object ob = authentication.getDetails();
        AuthUserDto loggedUser = (AuthUserDto) authentication.getPrincipal();

        return new ResponseDto(loggedUser, token, HttpStatus.OK);
    }

    private String successfulAuthentication(Authentication authentication) {
        AuthUserDto authUserDto = (AuthUserDto) authentication.getPrincipal();

        if (authentication.isAuthenticated()) {
            return JWT.create()
                    .withSubject(authUserDto.getUsername())
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 100))
                    .withClaim("role", authUserDto.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                    .sign(algorithm);
        }

        return null;
    }

    public Authentication authorizeWithToken(String token) {
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        DecodedJWT decodedJwt = jwtVerifier.verify(token);
        String username = decodedJwt.getSubject();
        String role = decodedJwt.getClaim("role").asArray(String.class)[0];
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role));

        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }

    private Authentication attemptAuthentication(User user) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        return authenticationManager.authenticate(authToken);
    }
}
