package freecycling.example.freecycling.Core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MeetingTimeTableRepository extends JpaRepository<MeetingTimeTable, Integer> {
    List<MeetingTimeTable> findByMeetingSite(String MeetingSite);
    List<MeetingTimeTable> findByMeetingTimeContaining(String date);
}