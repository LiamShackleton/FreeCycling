package freecycling.example.freecycling.Core;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MeetingTimeTableService {

    @Autowired
    MeetingTimeTableRepository meetingTimeTableRepository;

    public void save(MeetingTimeTable meeting) {
        // Set default values when creating a new meeting slot
        meeting.setAvailability("Available");
        meeting.setMeetingState(null);
        meetingTimeTableRepository.save(meeting);
    }

    public MeetingTimeTable findById(int id) {
        Optional<MeetingTimeTable> maybeMeeting = meetingTimeTableRepository.findById(id);
        if (!maybeMeeting.isPresent())
            return null;
        return maybeMeeting.get();
    }

    public List<MeetingTimeTable> findAll() {
        return meetingTimeTableRepository.findAll();
    }

    public List<MeetingTimeTable> findBySite(String site) {
        return meetingTimeTableRepository.findByMeetingSite(site);
    }

    public List<MeetingTimeTable> findByDate(String date) {
        return meetingTimeTableRepository.findByMeetingTimeContaining(date);
    }

    public List<MeetingTimeTable> findAvailableTimes(String site, String date) {
        return meetingTimeTableRepository
            .findByMeetingSiteAndMeetingTimeContainingAndAvailability(site, date, "Available");
    }

    // Books a meeting slot - sets availability to Unavailable and state to Pending
    public void bookMeeting(int id, int itemId) {
        Optional<MeetingTimeTable> maybeMeeting = meetingTimeTableRepository.findById(id);
        if (maybeMeeting.isPresent()) {
            MeetingTimeTable meeting = maybeMeeting.get();
            meeting.setAvailability("Unavailable");
            meeting.setMeetingState("Pending");
            meeting.setItemId(itemId);
            meetingTimeTableRepository.save(meeting);
        }
    }

    // Updates meeting state and marks as completed if state is Completed
    public void updateMeetingState(int id, String meetingState) {
        Optional<MeetingTimeTable> maybeMeeting = meetingTimeTableRepository.findById(id);
        if (maybeMeeting.isPresent()) {
            MeetingTimeTable meeting = maybeMeeting.get();
            meeting.setMeetingState(meetingState);
            if (meetingState.equals("Completed")) {
                meeting.setAvailability("Unavailable");
            }
            meetingTimeTableRepository.save(meeting);
        }
    }
}