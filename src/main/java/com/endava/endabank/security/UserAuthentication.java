package com.endava.endabank.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class UserAuthentication extends User {
    private Integer id;
    private boolean isApproved;

    public UserAuthentication(String username, String password,
                              Collection<? extends GrantedAuthority> authorities,
                              Integer id, boolean isApproved) {
        super(username, password, authorities);
        this.id = id;
        this.isApproved = isApproved;
    }

    public Integer getId() {
        return id;
    }

    public boolean getIsApproved() {
        return isApproved;
    }

    public UserAuthentication(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }
}
