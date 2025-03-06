package com.EMS.EmployeeManagementSystem.controller;

import com.EMS.EmployeeManagementSystem.dto.*;
import com.EMS.EmployeeManagementSystem.entity.User;
import com.EMS.EmployeeManagementSystem.services.JwtService;
import com.EMS.EmployeeManagementSystem.services.LoginHistoryService;
import com.EMS.EmployeeManagementSystem.services.NotificationsService;
import com.EMS.EmployeeManagementSystem.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private NotificationsService notificationsService;
    @Autowired
    private LoginHistoryService loginHistoryService;
    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequestDto request) {
        if (request.getUserDto() == null || request.getPassword() == null || request.getPassword().isBlank()) {
            return ResponseEntity.badRequest().body("Invalid request: User details and password are required.");
        }

        UserResponseDto registeredUser = userService.registerUser(request.getUserDto(), request.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequestDto loginRequest) {
        try {
            return userService.verifyAndRecordLogin(loginRequest.getEmail(), loginRequest.getPassword());
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", e.getMessage()));
        }
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAllUsers")
    public List<UserResponseDto> getAllUsers()
    {
        return userService.getAllUsers();
    }

    @PostMapping("/update")
    public UserDto updateUserDetails(@RequestParam Long id, @RequestBody UserDto userDto) {
        return userService.updateUserDetails(id, userDto);
    }

    @PostMapping("/deactivate")
    public String deactivateUser(@RequestParam Long id) {
        userService.deactivateUser(id);
        return "User deactivated successfully.";
    }

    @PostMapping("/approve")
    public UserDto approveUser(@RequestParam Long id) {
        UserDto approvedUser = userService.approveUserRequest(id);

        // Send approval notification
        notificationsService.sendEmail(approvedUser.getEmail(), "Account Approved",
                "Your account has been approved! You can now log in.");

        return approvedUser;
    }

    @PostMapping("/record/{userId}")
    public ResponseEntity<String> recordLogin(@PathVariable Long userId) {
        try {
            loginHistoryService.recordLogin(userId);
            return ResponseEntity.ok("Login recorded successfully for user ID: " + userId);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with ID: " + userId);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LoginHistoryDto>> getLoginHistoryForUser(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long userId) {

        String token = authorizationHeader.startsWith("Bearer ") ?
                authorizationHeader.substring(7) : authorizationHeader;

        String email = jwtService.extractUserName(token);

        User user = userService.getUserByEmail(email);

        if (user != null && user.getId().equals(userId)) {
            try {
                List<LoginHistoryDto> loginHistoryDtos = loginHistoryService.getLoginHistoryForUser(userId);
                return ResponseEntity.ok(loginHistoryDtos);
            } catch (RuntimeException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDto> getUserProfile(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
        String email = jwtService.extractUserName(token);

        User user = userService.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Convert User entity to UserDto
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setRoleName(user.getRole().getRoleName());
        userDto.setStatus(user.getStatus());

        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/update-profile")
    public ResponseEntity<UserDto> updateUserProfile(@RequestParam Long id, @RequestBody UserDto userDto) {
        UserDto updatedUser = userService.updateUserProfile(id, userDto);
        return ResponseEntity.ok(updatedUser);
    }






    @GetMapping("/csrf")
    public CsrfToken getToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute("_csrf");
    }
}
