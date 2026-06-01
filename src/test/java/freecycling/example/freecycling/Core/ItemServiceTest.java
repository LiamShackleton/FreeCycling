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

import freecycling.example.freecycling.Interface.DTOs.ItemDTO;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @Mock
    ItemRepository itemRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    ItemService itemService;

    private Item testItem;
    private ItemDTO testItemDTO;
    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setUserId(1);
        testUser.setUserName("testuser");

        testItem = new Item();
        testItem.setItemId(1);
        testItem.setItemName("Old Bike");
        testItem.setItemDonor("1");
        testItem.setItemAvailability("available");
        testItem.setItemType("Sports");
        testItem.setItemDescription("A used bike");

        testItemDTO = new ItemDTO();
        testItemDTO.setItemName("Old Bike");
        testItemDTO.setItemRecipient(null);
        testItemDTO.setItemImageUrl("http://example.com/bike.jpg");
        testItemDTO.setItemAvailability("available");
        testItemDTO.setItemType("Sports");
        testItemDTO.setItemDescription("A used bike");
    }

    // Test save - valid user
    @Test
    public void testSaveValidUser() {
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        String result = itemService.save(testItemDTO, "1");
        verify(itemRepository, times(1)).save(any(Item.class));
        assertNotEquals("Bad Id", result);
    }

    // Test save - invalid user returns "Bad Id"
    @Test
    public void testSaveInvalidUser() {
        when(userRepository.findById(999)).thenReturn(Optional.empty());
        String result = itemService.save(testItemDTO, "999");
        assertEquals("Bad Id", result);
        verify(itemRepository, never()).save(any());
    }

    // Test donor posts an item to be donated
    @Test
    public void testDonorPostsItem() {
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        String result = itemService.save(testItemDTO, "1");
        
        assertNotNull(result);
        assertNotEquals("Bad Id", result);
        
        verify(itemRepository).save(argThat(item ->
            item.getItemName().equals("Old Bike") &&
            item.getItemDonor().equals("1") &&
            item.getItemRecipient() == null &&
            item.getItemImageUrl().equals("http://example.com/bike.jpg") &&
            item.getItemAvailability().equals("available") &&
            item.getItemType().equals("Sports") &&
            item.getItemDescription().equals("A used bike")
        ));
    }

    // Test save maps fields correctly
    @Test
    public void testSaveMapsFields() {
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        itemService.save(testItemDTO, "1");
        verify(itemRepository).save(argThat(item ->
            item.getItemName().equals("Old Bike") &&
            item.getItemDonor().equals("1") &&
            item.getItemAvailability().equals("available") &&
            item.getItemType().equals("Sports")
        ));
    }

    // Test saveRecipient - item exists
    @Test
    public void testSaveRecipientExists() {
        when(itemRepository.findById(1)).thenReturn(Optional.of(testItem));
        itemService.saveRecipient("1", "2");
        verify(itemRepository).save(argThat(item ->
            item.getItemRecipient().equals("2")
        ));
    }

    // Test saveRecipient - item does not exist
    @Test
    public void testSaveRecipientNotFound() {
        when(itemRepository.findById(999)).thenReturn(Optional.empty());
        itemService.saveRecipient("999", "2");
        verify(itemRepository, never()).save(any());
    }

    // Test updateAvailability - item exists
    @Test
    public void testUpdateAvailabilityExists() {
        when(itemRepository.findById(1)).thenReturn(Optional.of(testItem));
        itemService.updateAvailability("1", "Given Away");
        verify(itemRepository).save(argThat(item ->
            item.getItemAvailability().equals("Given Away")
        ));
    }

    // Test updateAvailability - item does not exist
    @Test
    public void testUpdateAvailabilityNotFound() {
        when(itemRepository.findById(999)).thenReturn(Optional.empty());
        itemService.updateAvailability("999", "Given Away");
        verify(itemRepository, never()).save(any());
    }

    // Test findById - item exists
    @Test
    public void testFindByIdExists() {
        when(itemRepository.findById(1)).thenReturn(Optional.of(testItem));
        Item result = itemService.findById("1");
        assertNotNull(result);
        assertEquals("Old Bike", result.getItemName());
    }

    // Test findById - item does not exist
    @Test
    public void testFindByIdNotFound() {
        when(itemRepository.findById(999)).thenReturn(Optional.empty());
        Item result = itemService.findById("999");
        assertNull(result);
    }

    // Test findById - invalid id string returns null
    @Test
    public void testFindByIdInvalidString() {
        Item result = itemService.findById("notanumber");
        assertNull(result);
    }

    // Test findByUser
    @Test
    public void testFindByUser() {
        List<Item> items = new ArrayList<>();
        items.add(testItem);
        when(itemRepository.findByItemDonor("1")).thenReturn(items);
        List<Item> results = itemService.findByUser("1");
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("Old Bike", results.get(0).getItemName());
    }

    // Test donor posts an item that shows up in list of items being offered
    @Test
    public void testDonorPostsItemShowsInAvailableList() {
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> {
            Item savedItem = invocation.getArgument(0);
            savedItem.setItemId(100);
            return savedItem;
        });
        
        // Donor posts an item
        String itemId = itemService.save(testItemDTO, "1");
        assertNotNull(itemId);
        assertNotEquals("Bad Id", itemId);
        
        // Verify the item shows up in available list
        List<Item> items = new ArrayList<>();
        items.add(testItem);
        when(itemRepository.findByItemAvailability("available")).thenReturn(items);
        List<Item> results = itemService.findAvailable();
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("Old Bike", results.get(0).getItemName());
    }

    // Test findAvailable
    @Test
    public void testFindAvailable() {
        List<Item> items = new ArrayList<>();
        items.add(testItem);
        when(itemRepository.findByItemAvailability("available")).thenReturn(items);
        List<Item> results = itemService.findAvailable();
        assertNotNull(results);
        assertEquals(1, results.size());
    }

    // Test findAvailableByType
    @Test
    public void testFindAvailableByType() {
        List<Item> items = new ArrayList<>();
        items.add(testItem);
        when(itemRepository.findByItemAvailabilityAndItemType("available", "Sports")).thenReturn(items);
        List<Item> results = itemService.findAvailableByType("Sports");
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("Sports", results.get(0).getItemType());
    }

    // Test findAll
    @Test
    public void testFindAll() {
        List<Item> items = new ArrayList<>();
        items.add(testItem);
        when(itemRepository.findAll()).thenReturn(items);
        List<Item> results = itemService.findAll();
        assertNotNull(results);
        assertEquals(1, results.size());
    }
}