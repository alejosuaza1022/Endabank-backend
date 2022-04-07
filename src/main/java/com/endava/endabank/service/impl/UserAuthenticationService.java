package com.endava.endabank.service.impl;

import com.endava.endabank.constants.Strings;
import com.endava.endabank.dao.UserDao;
import com.endava.endabank.model.Role;
import com.endava.endabank.model.User;
import com.endava.endabank.security.UserAuthentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;


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
        Role role = user.getRole();
        boolean isApproved = user.getIsApproved() != null && user.getIsApproved();
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.getName()));
        return new UserAuthentication(
                user.getEmail(), user.getPassword(),
                authorities, user.getId(),isApproved );
    }

}
