package com.EMS.EmployeeManagementSystem.services;

import com.EMS.EmployeeManagementSystem.CustomUserDetails;
import com.EMS.EmployeeManagementSystem.dto.UserDto;
import com.EMS.EmployeeManagementSystem.dto.UserResponseDto;
import com.EMS.EmployeeManagementSystem.entity.Credentials;
import com.EMS.EmployeeManagementSystem.entity.RefreshToken;
import com.EMS.EmployeeManagementSystem.entity.Role;
import com.EMS.EmployeeManagementSystem.entity.User;
import com.EMS.EmployeeManagementSystem.mapper.UserMapper;
import com.EMS.EmployeeManagementSystem.repository.CredentialsRepository;
import com.EMS.EmployeeManagementSystem.repository.RoleRepository;
import com.EMS.EmployeeManagementSystem.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CredentialsRepository credentialsRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private NotificationsService notificationsService;
    @Autowired
    private LoginHistoryService loginHistoryService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Value("${spring.mail.from}")
    private String fromEmail;

    // Register a new user
    @Transactional
    public UserResponseDto registerUser(UserDto userDto, String password) {
        if (userDto == null || StringUtils.isBlank(password)) {
            throw new RuntimeException("Invalid request: userDto or password is null/empty");
        }
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new RuntimeException("Email already registered: " + userDto.getEmail());
        }
        Role role = roleRepository.findByRoleName(userDto.getRoleName())
                .orElseThrow(() -> new RuntimeException("Role not found: " + userDto.getRoleName()));

        User user = UserMapper.mapToUser(userDto, role);
        user.setStatus(false);

        Credentials credentials = new Credentials();
        credentials.setEmail(userDto.getEmail());
        credentials.setPassword(passwordEncoder.encode(password));
        credentials.setUser(user);
        user.setCredentials(credentials);

        user = userRepository.save(user);

        String emailBody = "A new user (" + userDto.getEmail() + ") has registered and is waiting for approval.\n\n"
                + "Please approve at the earliest. You can approve by clicking the link below:\n"
                + "http://localhost:5173/approve-user?id=" + userDto.getId();


        notificationsService.sendEmail(fromEmail, "New user registration request",
                emailBody);

        return UserMapper.mapToUserResponseDto(user);
    }

    public ResponseEntity<Map<String, String>> verifyAndRecordLogin(String email, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid email or password");
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        loginHistoryService.recordLogin(user.getId());

        // Generate new refresh token (deletes old one first)
        RefreshToken storedToken = refreshTokenService.createRefreshToken(user.getId());
        String refreshToken = storedToken.getToken();

        // **Generate new access token**
        String accessToken = jwtService.generateToken(new CustomUserDetails(user, user.getCredentials()));

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", jwtService.generateToken(new CustomUserDetails(user, user.getCredentials())));
        tokens.put("refresh_token", jwtService.generateRefreshToken(user.getEmail()));
        tokens.put("role", user.getRole().getRoleName());
        tokens.put("UserId", String.valueOf(user.getId()));

        return ResponseEntity.ok(tokens);
    }

    // Retrieve user by email
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    // Get all users
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::mapToUserResponseDto)
                .collect(Collectors.toList());
    }

    // Update user details
    public UserDto updateUserDetails(Long id, UserDto userDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No such user"));

        existingUser.setFirstName(userDto.getFirstName());
        existingUser.setLastName(userDto.getLastName());
        existingUser.setStatus(userDto.getStatus());

        Role role = roleRepository.findByRoleName(userDto.getRoleName())
                .orElseThrow(() -> new RuntimeException("Role not found: " + userDto.getRoleName()));
        existingUser.setRole(role);

        return UserMapper.mapToUserDto(userRepository.save(existingUser));
    }

    // Deactivate a user
    public void deactivateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        user.setStatus(false);
        userRepository.save(user);
    }

    // Approve a user
    public UserDto approveUserRequest(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        user.setStatus(true);
        userRepository.save(user);

        notificationsService.sendEmail(user.getEmail(), "Registration Approved",
                "Your account has been approved. You can now log in.");

        return UserMapper.mapToUserDto(user);
    }

    // Scheduler: Auto-approve users at midnight
    @Scheduled(cron = "0 56 10 * * ?")
    //@Scheduled(cron = "* * * * *")
    public void autoApprovePendingUsers() {
        List<User> pendingUsers = userRepository.findPendingApprovalUsers();
        pendingUsers.forEach(user -> user.setStatus(true));

        userRepository.saveAll(pendingUsers);

        pendingUsers.forEach(user -> notificationsService.sendEmail(user.getEmail(),
                "Auto approval notification", "Your account has been approved. You can now log in."));
    }

    @Transactional
    public UserDto updateUserProfile(Long id, UserDto userDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

        // Update only first name, last name, and email
        existingUser.setFirstName(userDto.getFirstName());
        existingUser.setLastName(userDto.getLastName());
        existingUser.setEmail(userDto.getEmail());

        User updatedUser = userRepository.save(existingUser);
        return UserMapper.mapToUserDto(updatedUser);
    }
}
