package com.endava.endabank.security.jwtAuthentication;

import com.endava.endabank.security.utils.JwtManage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static java.util.Arrays.stream;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtManage jwtManage;

    public JwtRequestFilter(JwtManage jwtManage) {
        this.jwtManage = jwtManage;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/api/v1/login") ||
                (request.getServletPath().equals("/api/v1/users") && request.getMethod().equals(HttpMethod.POST.name()))) {
            filterChain.doFilter(request, response);
        } else {
            String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authorization != null && authorization.startsWith("Bearer ")) {
                try {
                    Object[] dataJwt = jwtManage.verifyToken(authorization);
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    stream((String[]) dataJwt[1]).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(dataJwt[0], null, authorities);
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                } catch (Exception e) {
                    SecurityContextHolder.clearContext();
                    throw new Error(e);
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }

    }
}
