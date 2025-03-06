package com.EMS.EmployeeManagementSystem.services;

import com.EMS.EmployeeManagementSystem.CustomUserDetails;
import com.EMS.EmployeeManagementSystem.entity.Credentials;
import com.EMS.EmployeeManagementSystem.entity.User;
import com.EMS.EmployeeManagementSystem.repository.CredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
@Service
@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    public CredentialsRepository credentialsRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Credentials credentials = credentialsRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        User user = credentials.getUser();
        if (user == null) {
            throw new UsernameNotFoundException("User details not found for email: " + username);
        }
        return new CustomUserDetails(user, credentials);
    }
}
