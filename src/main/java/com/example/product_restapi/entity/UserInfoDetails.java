package com.example.product_restapi.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserInfoDetails implements UserDetails{

    private String name;
    private String password;
    private List<GrantedAuthority> authorities;

    // Constructor to convert UserInfo entity to UserDetails-compatible format for Spring Security
    public UserInfoDetails(UserInfo userInfo){
        name = userInfo.getUsername();
        password = userInfo.getPassword();
        // Convert comma-separated roles from DB to Spring Security Roles with "ROLE_" prefix
        authorities = Arrays.stream(userInfo.getRoles().split(","))
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.trim()))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return authorities;
    }
    @Override
    public String getUsername(){
        return name;
    }
    @Override
    public String getPassword(){
        return password;
    }
    @Override
    public boolean isAccountNonExpired(){
        return true;
    }
    @Override
    public boolean isAccountNonLocked(){
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }
    @Override
    public boolean isEnabled(){
        return true;
    }
}