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

import freecycling.example.freecycling.Interface.DTOs.InterestDTO;

@ExtendWith(MockitoExtension.class)
public class InterestServiceTest {

    @Mock
    InterestRepository interestRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    ItemRepository itemRepository;

    @InjectMocks
    InterestService interestService;

    private Interest testInterest;
    private InterestDTO testInterestDTO;
    private User testUser;
    private Item testItem;

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setUserId(1);
        testUser.setUserName("testuser");

        testItem = new Item();
        testItem.setItemId(1);
        testItem.setItemName("Old Bike");

        testInterest = new Interest();
        testInterest.setIdInterestTable(1);
        testInterest.setUser("1");
        testInterest.setItem("1");
        testInterest.setTimeStamp("2026-05-20 10:00:00");
        testInterest.setNote("I would love this bike");

        testInterestDTO = new InterestDTO();
        testInterestDTO.setItem("1");
        testInterestDTO.setTimeStamp("2026-05-20 10:00:00");
        testInterestDTO.setNote("I would love this bike");
    }

    // Test save - valid user and item
    @Test
    public void testSaveValid() {
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        when(itemRepository.findById(1)).thenReturn(Optional.of(testItem));
        String result = interestService.save(testInterestDTO, "1");
        verify(interestRepository, times(1)).save(any(Interest.class));
        assertNotEquals("Bad Id", result);
        assertNotEquals("Bad Item Id", result);
    }

    // Test save - invalid user returns "Bad Id"
    @Test
    public void testSaveInvalidUser() {
        when(userRepository.findById(999)).thenReturn(Optional.empty());
        String result = interestService.save(testInterestDTO, "999");
        assertEquals("Bad Id", result);
        verify(interestRepository, never()).save(any());
    }

    // Test save - invalid item returns "Bad Item Id"
    @Test
    public void testSaveInvalidItem() {
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        when(itemRepository.findById(1)).thenReturn(Optional.empty());
        String result = interestService.save(testInterestDTO, "1");
        assertEquals("Bad Item Id", result);
        verify(interestRepository, never()).save(any());
    }

    // Test save maps fields correctly
    @Test
    public void testSaveMapsFields() {
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        when(itemRepository.findById(1)).thenReturn(Optional.of(testItem));
        interestService.save(testInterestDTO, "1");
        verify(interestRepository).save(argThat(interest ->
            interest.getUser().equals("1") &&
            interest.getItem().equals("1") &&
            interest.getNote().equals("I would love this bike")
        ));
    }

    // Test findById - interest exists
    @Test
    public void testFindByIdExists() {
        when(interestRepository.findById(1)).thenReturn(Optional.of(testInterest));
        Interest result = interestService.findById("1");
        assertNotNull(result);
        assertEquals("1", result.getUser());
    }

    // Test findById - interest does not exist
    @Test
    public void testFindByIdNotFound() {
        when(interestRepository.findById(999)).thenReturn(Optional.empty());
        Interest result = interestService.findById("999");
        assertNull(result);
    }

    // Test findById - invalid id string returns null
    @Test
    public void testFindByIdInvalidString() {
        Interest result = interestService.findById("notanumber");
        assertNull(result);
    }

    // Test findByUser
    @Test
    public void testFindByUser() {
        List<Interest> interests = new ArrayList<>();
        interests.add(testInterest);
        when(interestRepository.findByUser("1")).thenReturn(interests);
        List<Interest> results = interestService.findByUser("1");
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("1", results.get(0).getUser());
    }

    // Test findByItem
    @Test
    public void testFindByItem() {
        List<Interest> interests = new ArrayList<>();
        interests.add(testInterest);
        when(interestRepository.findByItem("1")).thenReturn(interests);
        List<Interest> results = interestService.findByItem("1");
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("1", results.get(0).getItem());
    }

    // Test findAll
    @Test
    public void testFindAll() {
        List<Interest> interests = new ArrayList<>();
        interests.add(testInterest);
        when(interestRepository.findAll()).thenReturn(interests);
        List<Interest> results = interestService.findAll();
        assertNotNull(results);
        assertEquals(1, results.size());
    }

    // Test findAvailable delegates to findAll
    @Test
    public void testFindAvailable() {
        List<Interest> interests = new ArrayList<>();
        interests.add(testInterest);
        when(interestRepository.findAll()).thenReturn(interests);
        List<Interest> results = interestService.findAvailable();
        assertNotNull(results);
        assertEquals(1, results.size());
    }
}