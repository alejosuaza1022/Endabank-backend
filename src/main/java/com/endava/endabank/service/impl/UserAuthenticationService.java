package com.endava.endabank.service.impl;

import com.endava.endabank.constants.Strings;
import com.endava.endabank.dao.UserDao;
import com.endava.endabank.exceptions.customExceptions.BadDataException;
import com.endava.endabank.model.Role;
import com.endava.endabank.model.User;
import com.endava.endabank.security.UserAuthentication;
import com.endava.endabank.security.utils.JwtManage;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Service
@AllArgsConstructor
public class UserAuthenticationService implements UserDetailsService {
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOpt = userDao.findByEmail(username);
        User user = userOpt.orElseThrow(() -> new UsernameNotFoundException(Strings.USER_NOT_FOUND));
        Role role = user.getRole();
        boolean isApproved = user.getIsApproved() != null && user.getIsApproved();
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.getName()));
        return new UserAuthentication(
                user.getEmail(), user.getPassword(),
                authorities, user.getId(), isApproved);
    }

    public Map<String, Object> logInUser(Authentication authentication) throws BadDataException {
        UserAuthentication userAuthentication = (UserAuthentication) authentication.getPrincipal();
        String role = userAuthentication.getAuthorities().toArray()[0].toString();
        final String token = JwtManage.generateToken(userAuthentication.getId(),
                userAuthentication.getUsername(), Strings.SECRET_JWT);
        Map<String, Object> dataResponse = new HashMap<>();
        dataResponse.put("access_token", token);
        dataResponse.put("rol", role);
        dataResponse.put("isApproved", userAuthentication.getIsApproved());
        return dataResponse;
    }

}
