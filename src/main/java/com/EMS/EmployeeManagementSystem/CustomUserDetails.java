package com.EMS.EmployeeManagementSystem;

import com.EMS.EmployeeManagementSystem.entity.Credentials;
import com.EMS.EmployeeManagementSystem.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {
    private final User user;
    private final Credentials credentials;
    public CustomUserDetails(User user, Credentials credentials) {
        this.user = user;
        this.credentials = credentials;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (user.getRole() == null) {
            return Collections.emptyList();
        }

        String userRole = user.getRole().getRoleName();
        if (!userRole.startsWith("ROLE_")) {
            userRole = "ROLE_" + userRole;
        }
        return Collections.singletonList(new SimpleGrantedAuthority(userRole));
    }

    @Override
    public String getPassword() {
        return credentials.getPassword();  // Fetch from Credentials entity
    }

    @Override
    public String getUsername() {
        return credentials.getEmail();  // Fetch from Credentials entity
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
        return user.getStatus(); // Use status field from User entity
    }

    // Getters if needed
    public User getUser() {
        return user;
    }

    public Credentials getCredentials() {
        return credentials;
    }
}
