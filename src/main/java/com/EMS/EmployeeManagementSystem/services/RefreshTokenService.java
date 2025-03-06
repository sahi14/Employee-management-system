package com.EMS.EmployeeManagementSystem.services;


import com.EMS.EmployeeManagementSystem.entity.RefreshToken;
import com.EMS.EmployeeManagementSystem.entity.User;
import com.EMS.EmployeeManagementSystem.repository.RefreshTokenRepository;
import com.EMS.EmployeeManagementSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired private JwtService jwtService;

    public RefreshToken createRefreshToken(Long id) {
        System.out.println("email : "+ id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        refreshTokenRepository.deleteByUser(user);
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(jwtService.generateRefreshToken(user.getEmail()));
        refreshToken.setExpiryDate(Instant.now().plusSeconds(86400)); // Token expires in 1 day

        RefreshToken savedToken = refreshTokenRepository.save(refreshToken);
        System.out.println("New Refresh Token saved: " + savedToken.getToken());
        return savedToken;

       // return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public boolean validateRefreshToken(String token) {
        Optional<RefreshToken> refreshToken = findByToken(token);
        return refreshToken.isPresent() && isValid(refreshToken.get());
    }

    public boolean isValid(RefreshToken token) {
        return token != null && token.getExpiryDate().isAfter(Instant.now());
    }

//    public boolean isValid(RefreshToken token) {
//        return token.getExpiryDate().isAfter(Instant.now());
//    }

    @Transactional
    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }
}