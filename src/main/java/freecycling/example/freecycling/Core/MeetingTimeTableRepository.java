package freecycling.example.freecycling.Core;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MeetingTimeTableRepository extends JpaRepository<MeetingTimeTable, Integer> {
    List<MeetingTimeTable> findByMeetingSite(String meetingSite);
    List<MeetingTimeTable> findByMeetingTimeContaining(String date);
    List<MeetingTimeTable> findByMeetingSiteAndMeetingTimeContainingAndAvailability(
        String meetingSite, String date, String availability);
}