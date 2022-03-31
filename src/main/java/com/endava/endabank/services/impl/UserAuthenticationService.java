package com.endava.endabank.services.impl;

import com.endava.endabank.constants.Strings;
import com.endava.endabank.dao.UserDao;
import com.endava.endabank.models.Permission;
import com.endava.endabank.models.Role;
import com.endava.endabank.models.User;
import com.endava.endabank.security.UserAuthentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
public class UserAuthenticationService implements UserDetailsService {
    private final UserDao userDao;


    public UserAuthenticationService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOpt = userDao.findByEmail(username);
        User user = userOpt.orElseThrow(() -> new UsernameNotFoundException(Strings.USER_NOT_FOUND));
//        return new org.springframework.security.core.userdetails.
//                User(user.getEmail(), user.getPassword(),
//                getAuthorities(user));
        return new UserAuthentication(
                user.getEmail(), user.getPassword(),
                new ArrayList<>(), user.getId());
    }

}
