package com.EMS.EmployeeManagementSystem.controller;

import com.EMS.EmployeeManagementSystem.CustomUserDetails;
import com.EMS.EmployeeManagementSystem.entity.Credentials;
import com.EMS.EmployeeManagementSystem.entity.RefreshToken;
import com.EMS.EmployeeManagementSystem.entity.User;
import com.EMS.EmployeeManagementSystem.services.JwtService;
import com.EMS.EmployeeManagementSystem.services.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/refresh-token")
public class RefreshController {

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        if (refreshToken == null || refreshToken.isEmpty()) {
            return ResponseEntity.badRequest().body("Refresh Token is required");
        }

        // Validate the refresh token
        Optional<RefreshToken> storedToken = refreshTokenService.findByToken(refreshToken);
        if (storedToken.isEmpty() || !refreshTokenService.isValid(storedToken.get())) {
            return ResponseEntity.status(403).body("Invalid or expired refresh token");
        }

        // Extract user details and credentials
        User user = storedToken.get().getUser();
        Credentials credentials = user.getCredentials();

        // Generate new access token using CustomUserDetails
        String newAccessToken = jwtService.generateToken(new CustomUserDetails(user, credentials));

        // Return new access token
        Map<String, String> response = new HashMap<>();
        response.put("accessToken", newAccessToken);

        return ResponseEntity.ok(response);
    }







//    @PostMapping("/generate/{userId}")
//    public ResponseEntity<Map<String, Object>> generateRefreshToken(@PathVariable Long userId) {
//        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userId);
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("token", refreshToken.getToken());
//        response.put("expiryDate", refreshToken.getExpiryDate());
//
//        return ResponseEntity.ok(response);
//    }


}
