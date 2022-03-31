package com.endava.endabank.security.jwtAuthentication;

import com.endava.endabank.constants.Routes;
import com.endava.endabank.dto.user.UserPrincipalSecurity;
import com.endava.endabank.models.User;
import com.endava.endabank.security.utils.JwtManage;
import com.endava.endabank.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
import java.util.Collection;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private final UserService userService;
    private final JwtManage jwtManage;
    private final ModelMapper modelMapper;

    public JwtRequestFilter(UserService userService, JwtManage jwtManage, ModelMapper modelMapper) {
        this.userService = userService;
        this.jwtManage = jwtManage;
        this.modelMapper = modelMapper;
    }



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals(Routes.API_ROUTE + Routes.LOGIN_ROUTE) ||
                (request.getServletPath().equals(Routes.API_ROUTE + Routes.USERS_ROUTE) && request.getMethod().equals(HttpMethod.POST.name()))) {
            filterChain.doFilter(request, response);
        } else {
            String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authorization != null && authorization.startsWith("Bearer ")) {
                try {
                    Integer userId = jwtManage.verifyToken(authorization);
                    UsernamePasswordAuthenticationToken authenticationToken = userService.getUsernamePasswordToken(userId);
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
