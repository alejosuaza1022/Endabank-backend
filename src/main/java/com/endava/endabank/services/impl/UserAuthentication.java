package com.endava.endabank.services.impl;

import com.endava.endabank.dao.UserDao;
import com.endava.endabank.models.Permission;
import com.endava.endabank.models.Role;
import com.endava.endabank.models.User;
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
public class UserAuthentication implements UserDetailsService {
    private final UserDao userDao;


    public UserAuthentication(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOpt = userDao.findByEmail(username);
        User user = userOpt.orElseThrow(() -> new UsernameNotFoundException("User not found in the database"));
        return new org.springframework.security.core.userdetails.
                User(user.getEmail(), user.getPassword(),
                getAuthorities(user));
    }

    private List<SimpleGrantedAuthority> getAuthorities(User user) {
        Role role = user.getRole();
        Set<Permission> permissions = role.getPermissions();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.getName()));
        permissions.forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getName())));
        return authorities;

    }
}
