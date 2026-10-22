package com.airesume.analyzer.service;

import com.airesume.analyzer.dto.PasswordChangeRequest;
import com.airesume.analyzer.dto.UpdateProfileRequest;
import com.airesume.analyzer.dto.UserDto;
import com.airesume.analyzer.entity.User;
import com.airesume.analyzer.exception.ResourceNotFoundException;
import com.airesume.analyzer.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User(
                "alex.morgan@example.com",
                "current_hashed_password",
                "Alex Morgan",
                "Junior Java Full Stack Developer"
        );
        testUser.setId(1L);
    }

    @Test
    @DisplayName("Should successfully return current user profile")
    void testGetCurrentUser_Success() {
        when(userRepository.findByEmail("alex.morgan@example.com")).thenReturn(Optional.of(testUser));

        UserDto userDto = userService.getCurrentUser("alex.morgan@example.com");

        assertNotNull(userDto);
        assertEquals("alex.morgan@example.com", userDto.getEmail());
        assertEquals("Alex Morgan", userDto.getFullName());
        assertEquals("Junior Java Full Stack Developer", userDto.getTargetRole());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when user email does not exist")
    void testGetCurrentUser_NotFound_ThrowsException() {
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getCurrentUser("unknown@example.com"));
    }

    @Test
    @DisplayName("Should successfully update full name and target role")
    void testUpdateProfile_Success() {
        when(userRepository.findByEmail("alex.morgan@example.com")).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        UpdateProfileRequest request = new UpdateProfileRequest("Alex Morgan Jr.", "Senior Java Engineer");
        UserDto updated = userService.updateProfile("alex.morgan@example.com", request);

        assertNotNull(updated);
        assertEquals("Alex Morgan Jr.", testUser.getFullName());
        assertEquals("Senior Java Engineer", testUser.getTargetRole());
        verify(userRepository).save(testUser);
    }

    @Test
    @DisplayName("Should change password when current password is valid")
    void testChangePassword_Success() {
        when(userRepository.findByEmail("alex.morgan@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("CurrentPassword123!", "current_hashed_password")).thenReturn(true);
        when(passwordEncoder.encode("NewPassword123!")).thenReturn("new_encoded_hash");

        PasswordChangeRequest request = new PasswordChangeRequest("CurrentPassword123!", "NewPassword123!");
        assertDoesNotThrow(() -> userService.changePassword("alex.morgan@example.com", request));

        assertEquals("new_encoded_hash", testUser.getPasswordHash());
        verify(userRepository).save(testUser);
    }

    @Test
    @DisplayName("Should throw BadCredentialsException when current password does not match")
    void testChangePassword_InvalidCurrentPassword_ThrowsException() {
        when(userRepository.findByEmail("alex.morgan@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("WrongPassword!", "current_hashed_password")).thenReturn(false);

        PasswordChangeRequest request = new PasswordChangeRequest("WrongPassword!", "NewPassword123!");
        assertThrows(BadCredentialsException.class, () -> userService.changePassword("alex.morgan@example.com", request));

        verify(userRepository, never()).save(any(User.class));
    }
}
