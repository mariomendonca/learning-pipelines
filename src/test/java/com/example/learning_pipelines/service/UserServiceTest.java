package com.example.learning_pipelines.service;

import com.example.learning_pipelines.dto.UserRequestDTO;
import com.example.learning_pipelines.dto.UserResponseDTO;
import com.example.learning_pipelines.entity.User;
import com.example.learning_pipelines.exception.DuplicateEmailException;
import com.example.learning_pipelines.exception.ResourceNotFoundException;
import com.example.learning_pipelines.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Unit Tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private UserRequestDTO testUserRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("John Doe");
        testUser.setEmail("john@example.com");
        testUser.setCreatedAt(LocalDateTime.now());
        testUser.setUpdatedAt(LocalDateTime.now());

        testUserRequest = new UserRequestDTO();
        testUserRequest.setName("John Doe");
        testUserRequest.setEmail("john@example.com");
    }

    @Test
    @DisplayName("Should return all users successfully")
    void getAllUsers_Success() {
        // Arrange
        User user2 = new User();
        user2.setId(2L);
        user2.setName("Jane Smith");
        user2.setEmail("jane@example.com");
        user2.setCreatedAt(LocalDateTime.now());
        user2.setUpdatedAt(LocalDateTime.now());

        when(userRepository.findAll()).thenReturn(Arrays.asList(testUser, user2));

        // Act
        List<UserResponseDTO> result = userService.getAllUsers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("Jane Smith", result.get(1).getName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no users exist")
    void getAllUsers_EmptyList() {
        // Arrange
        when(userRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<UserResponseDTO> result = userService.getAllUsers();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return user by id successfully")
    void getUserById_Success() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // Act
        UserResponseDTO result = userService.getUserById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals("john@example.com", result.getEmail());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when user not found by id")
    void getUserById_NotFound() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> userService.getUserById(999L)
        );

        assertTrue(exception.getMessage().contains("User not found with id: 999"));
        verify(userRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should create user successfully")
    void createUser_Success() {
        // Arrange
        when(userRepository.existsByEmail(testUserRequest.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        UserResponseDTO result = userService.createUser(testUserRequest);

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("john@example.com", result.getEmail());
        verify(userRepository, times(1)).existsByEmail(testUserRequest.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw DuplicateEmailException when email already exists")
    void createUser_DuplicateEmail() {
        // Arrange
        when(userRepository.existsByEmail(testUserRequest.getEmail())).thenReturn(true);

        // Act & Assert
        DuplicateEmailException exception = assertThrows(
            DuplicateEmailException.class,
            () -> userService.createUser(testUserRequest)
        );

        assertTrue(exception.getMessage().contains("Email already exists"));
        verify(userRepository, times(1)).existsByEmail(testUserRequest.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should update user successfully")
    void updateUser_Success() {
        // Arrange
        UserRequestDTO updateRequest = new UserRequestDTO();
        updateRequest.setName("John Updated");
        updateRequest.setEmail("john.updated@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.existsByEmail(updateRequest.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        UserResponseDTO result = userService.updateUser(1L, updateRequest);

        // Assert
        assertNotNull(result);
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).existsByEmail(updateRequest.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should update user with same email successfully")
    void updateUser_SameEmail_Success() {
        // Arrange
        UserRequestDTO updateRequest = new UserRequestDTO();
        updateRequest.setName("John Updated");
        updateRequest.setEmail("john@example.com"); // Same email

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        UserResponseDTO result = userService.updateUser(1L, updateRequest);

        // Assert
        assertNotNull(result);
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).existsByEmail(anyString());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when updating non-existent user")
    void updateUser_NotFound() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> userService.updateUser(999L, testUserRequest)
        );

        assertTrue(exception.getMessage().contains("User not found with id: 999"));
        verify(userRepository, times(1)).findById(999L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw DuplicateEmailException when updating with existing email")
    void updateUser_DuplicateEmail() {
        // Arrange
        UserRequestDTO updateRequest = new UserRequestDTO();
        updateRequest.setName("John Updated");
        updateRequest.setEmail("another@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.existsByEmail(updateRequest.getEmail())).thenReturn(true);

        // Act & Assert
        DuplicateEmailException exception = assertThrows(
            DuplicateEmailException.class,
            () -> userService.updateUser(1L, updateRequest)
        );

        assertTrue(exception.getMessage().contains("Email already exists"));
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).existsByEmail(updateRequest.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should delete user successfully")
    void deleteUser_Success() {
        // Arrange
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        // Act
        assertDoesNotThrow(() -> userService.deleteUser(1L));

        // Assert
        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting non-existent user")
    void deleteUser_NotFound() {
        // Arrange
        when(userRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> userService.deleteUser(999L)
        );

        assertTrue(exception.getMessage().contains("User not found with id: 999"));
        verify(userRepository, times(1)).existsById(999L);
        verify(userRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Should return true")
    void shouldReturnTrue() {
        assertTrue(true);
    }
}

