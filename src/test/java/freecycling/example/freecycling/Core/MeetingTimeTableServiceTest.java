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

import freecycling.example.freecycling.Interface.DTOs.MeetingTimeDTO;

@ExtendWith(MockitoExtension.class)
public class MeetingTimeTableServiceTest {

    @Mock
    MeetingTimeTableRepository meetingTimeTableRepository;

    @Mock
    ItemService itemService;

    @InjectMocks
    MeetingTimeTableService meetingTimeTableService;

    private MeetingTimeTable testMeeting;
    private MeetingTimeDTO testMeetingDTO;

    @BeforeEach
    public void setUp() {
        testMeeting = new MeetingTimeTable();
        testMeeting.setIdMeetingTimeTable(1);
        testMeeting.setMeetingSite("Main Library");
        testMeeting.setMeetingTime("2026-05-20 10:00:00");
        testMeeting.setAvailability("Available");
        testMeeting.setMeetingState(null);
        testMeeting.setItemId(1);

        testMeetingDTO = new MeetingTimeDTO();
        testMeetingDTO.setMeetingSite("Main Library");
        testMeetingDTO.setMeetingTime("2026-05-20 10:00:00");
        testMeetingDTO.setItemId(1);
    }

    // Test save
    @Test
    public void testSave() {
        meetingTimeTableService.save(testMeetingDTO);
        verify(meetingTimeTableRepository, times(1)).save(any(MeetingTimeTable.class));
    }

    // Test save sets availability to Available and itemId to null
    @Test
    public void testSaveSetsAvailability() {
        meetingTimeTableService.save(testMeetingDTO);
        verify(meetingTimeTableRepository).save(argThat(meeting ->
            meeting.getAvailability().equals("Available") &&
            meeting.getItemId() == null
        ));
    }

    // Test findById - meeting exists
    @Test
    public void testFindByIdExists() {
        when(meetingTimeTableRepository.findById(1)).thenReturn(Optional.of(testMeeting));
        MeetingTimeTable result = meetingTimeTableService.findById(1);
        assertNotNull(result);
        assertEquals("Main Library", result.getMeetingSite());
    }

    // Test findById - meeting does not exist
    @Test
    public void testFindByIdNotFound() {
        when(meetingTimeTableRepository.findById(999)).thenReturn(Optional.empty());
        MeetingTimeTable result = meetingTimeTableService.findById(999);
        assertNull(result);
    }

    // Test findAll
    @Test
    public void testFindAll() {
        List<MeetingTimeTable> meetings = new ArrayList<>();
        meetings.add(testMeeting);
        when(meetingTimeTableRepository.findAll()).thenReturn(meetings);
        List<MeetingTimeTable> results = meetingTimeTableService.findAll();
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("Main Library", results.get(0).getMeetingSite());
    }

    // Test findBySite
    @Test
    public void testFindBySite() {
        List<MeetingTimeTable> meetings = new ArrayList<>();
        meetings.add(testMeeting);
        when(meetingTimeTableRepository.findByMeetingSite("Main Library")).thenReturn(meetings);
        List<MeetingTimeTable> results = meetingTimeTableService.findBySite("Main Library");
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("Main Library", results.get(0).getMeetingSite());
    }

    // Test findByDate
    @Test
    public void testFindByDate() {
        List<MeetingTimeTable> meetings = new ArrayList<>();
        meetings.add(testMeeting);
        when(meetingTimeTableRepository.findByMeetingTimeContaining("2026-05-20")).thenReturn(meetings);
        List<MeetingTimeTable> results = meetingTimeTableService.findByDate("2026-05-20");
        assertNotNull(results);
        assertEquals(1, results.size());
    }

    // Test findAvailableTimes
    @Test
    public void testFindAvailableTimes() {
        List<MeetingTimeTable> meetings = new ArrayList<>();
        meetings.add(testMeeting);
        when(meetingTimeTableRepository
            .findByMeetingSiteAndMeetingTimeContainingAndAvailability(
                "Main Library", "2026-05-20", "Available"))
            .thenReturn(meetings);
        List<MeetingTimeTable> results = meetingTimeTableService
            .findAvailableTimes("Main Library", "2026-05-20");
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("Available", results.get(0).getAvailability());
    }

    // Test bookMeeting
    @Test
    public void testBookMeeting() {
        when(meetingTimeTableRepository.findById(1)).thenReturn(Optional.of(testMeeting));
        meetingTimeTableService.bookMeeting(1, 1);
        verify(meetingTimeTableRepository, times(1)).save(argThat(meeting ->
            meeting.getAvailability().equals("Unavailable") &&
            meeting.getMeetingState().equals("Pending")
        ));
    }

    // Test updateMeetingState - not completed
    @Test
    public void testUpdateMeetingStateNotCompleted() {
        when(meetingTimeTableRepository.findById(1)).thenReturn(Optional.of(testMeeting));
        meetingTimeTableService.updateMeetingState(1, "Pending");
        verify(meetingTimeTableRepository, times(1)).save(argThat(meeting ->
            meeting.getMeetingState().equals("Pending")
        ));
    }

    // Test updateMeetingState - completed updates item availability
    @Test
    public void testUpdateMeetingStateCompleted() {
        when(meetingTimeTableRepository.findById(1)).thenReturn(Optional.of(testMeeting));
        meetingTimeTableService.updateMeetingState(1, "Completed");
        verify(itemService, times(1)).updateAvailability("1", "Given Away");
        verify(meetingTimeTableRepository, times(1)).save(argThat(meeting ->
            meeting.getMeetingState().equals("Completed")
        ));
    }

    // Test updateMeetingState - meeting not found
    @Test
    public void testUpdateMeetingStateNotFound() {
        when(meetingTimeTableRepository.findById(999)).thenReturn(Optional.empty());
        meetingTimeTableService.updateMeetingState(999, "Completed");
        verify(meetingTimeTableRepository, never()).save(any());
        verify(itemService, never()).updateAvailability(any(), any());
    }
}