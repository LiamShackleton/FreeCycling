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

    public void updateMeetingState(int id, String meetingState) {
        Optional<MeetingTimeTable> maybeMeeting = meetingTimeTableRepository.findById(id);
        if (maybeMeeting.isPresent()) {
            MeetingTimeTable meeting = maybeMeeting.get();
            meeting.setMeetingState(meetingState);
            meetingTimeTableRepository.save(meeting);
        }
    }
}