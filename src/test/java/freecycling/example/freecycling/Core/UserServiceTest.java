package freecycling.example.freecycling.Core;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import freecycling.example.freecycling.services.PasswordService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordService passwordService;

    @InjectMocks
    UserService userService;

    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setUserId(1);
        testUser.setUserName("testuser");
        testUser.setPass("hashedpassword");
        testUser.setEmail("test@mail.com");
    }

    // Test save
    @Test
    public void testSave() {
        when(passwordService.hashPassword("Password123")).thenReturn("hashedpassword");
        User user = new User();
        user.setUserName("testuser");
        user.setPass("Password123");
        user.setEmail("test@mail.com");
        userService.save(user);
        verify(userRepository, times(1)).save(user);
    }

    // Test findById - user exists
    @Test
    public void testFindByIdExists() {
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        User result = userService.findById(1);
        assertNotNull(result);
        assertEquals("testuser", result.getUserName());
    }

    // Test findById - user does not exist
    @Test
    public void testFindByIdNotFound() {
        when(userRepository.findById(999)).thenReturn(Optional.empty());
        User result = userService.findById(999);
        assertNull(result);
    }

    // Test findByUserName - user exists
    @Test
    public void testFindByUserNameExists() {
        when(userRepository.findByUserName("testuser")).thenReturn(testUser);
        User result = userService.findByUserName("testuser");
        assertNotNull(result);
        assertEquals("testuser", result.getUserName());
    }

    // Test findByUserName - user does not exist
    @Test
    public void testFindByUserNameNotFound() {
        when(userRepository.findByUserName("nobody")).thenReturn(null);
        User result = userService.findByUserName("nobody");
        assertNull(result);
    }

    // Test findAll
    @Test
    public void testFindAll() {
        List<User> users = new ArrayList<>();
        users.add(testUser);
        when(userRepository.findAll()).thenReturn(users);
        List<User> results = userService.findAll();
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("testuser", results.get(0).getUserName());
    }

    // Test verifyPassword - correct password
    @Test
    public void testVerifyPasswordCorrect() {
        when(passwordService.verifyHash("Password123", "hashedpassword")).thenReturn(true);
        boolean result = userService.verifyPassword("Password123", "hashedpassword");
        assertTrue(result);
    }

    // Test verifyPassword - wrong password
    @Test
    public void testVerifyPasswordWrong() {
        when(passwordService.verifyHash("wrongpassword", "hashedpassword")).thenReturn(false);
        boolean result = userService.verifyPassword("wrongpassword", "hashedpassword");
        assertFalse(result);
    }
}
