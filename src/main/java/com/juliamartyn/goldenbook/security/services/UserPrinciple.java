package com.juliamartyn.goldenbook.security.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.juliamartyn.goldenbook.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class UserPrinciple implements UserDetails {

	private Long id;
    private String username;

    @JsonIgnore
    private String password;

    private String email;

    private Collection<? extends GrantedAuthority> authorities;

    public UserPrinciple(Long id, String username, String password,
                         String email, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.authorities = authorities;
    }

    public static UserPrinciple build(User user) {

        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getName()));

        return new UserPrinciple(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                authorities
        );
    }

    public Long getId(){
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        UserPrinciple user = (UserPrinciple) o;
        return Objects.equals(id, user.id);
    }

    public String getEmail() {
        return email;
    }
}