package com.juliamartyn.goldenbook.security.jwt;

import com.juliamartyn.goldenbook.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtAuthProvider tokenProvider;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
    								FilterChain filterChain) throws ServletException, IOException {
        getJwt(request).ifPresent(jwt -> {
            String username = tokenProvider.getUserNameFromJwtToken(jwt);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
                    null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        });

        filterChain.doFilter(request, response);
    }

    private Optional<String> getJwt(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null &&  authHeader.startsWith("Bearer ")) {
            authHeader = authHeader.replace("Bearer ","");
        }

        return Optional.ofNullable(authHeader);
    }
}
