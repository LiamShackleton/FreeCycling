package freecycling.example.freecycling.Interface;

import freecycling.example.freecycling.Core.MeetingTimeTable;
import freecycling.example.freecycling.Core.MeetingTimeTableDAO;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/meetings")
@CrossOrigin(origins = "*")
public class MeetingTimeTableController {

    private MeetingTimeTableDAO dao;

    public MeetingTimeTableController(MeetingTimeTableDAO dao) {
        this.dao = dao;
    }

    // POST - register a new meeting time
    // Called with POST /meetings
    @PostMapping
    public ResponseEntity<String> save(@RequestBody MeetingTimeTable meeting) {
        if (meeting.getMeetingSite().isBlank() || meeting.getMeetingTime().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Meeting site and time cannot be empty");
        }
        dao.save(meeting);
        return ResponseEntity.ok().body("Meeting time saved");
    }

    // GET - find a meeting time by id
    // Called with GET /meetings/5
    @GetMapping("/{id}")
    public ResponseEntity<MeetingTimeTable> getMeetingById(@PathVariable int id) {
        MeetingTimeTable result = dao.findById(id);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok().body(result);
    }

    // GET - get all meeting times
    // Called with GET /meetings
    @GetMapping
    public ResponseEntity<List<MeetingTimeTable>> getAllMeetings() {
        List<MeetingTimeTable> results = dao.findAll();
        return ResponseEntity.ok().body(results);
    }

    // GET - get meeting times by site
    // Called with GET /meetings/site/SomeSite
    @GetMapping("/site/{site}")
    public ResponseEntity<List<MeetingTimeTable>> getMeetingsBySite(@PathVariable String site) {
        List<MeetingTimeTable> results = dao.findBySite(site);
        return ResponseEntity.ok().body(results);
    }

    // PATCH - update meeting state by id
    // Called with PATCH /meetings/state/{id}
    @PatchMapping("/state/{id}")
    public ResponseEntity<String> updateMeetingState(@PathVariable int id, @RequestBody String meetingState) {
        if (meetingState == null || meetingState.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Meeting state cannot be empty");
        }
        dao.updateMeetingState(id, meetingState);
        return ResponseEntity.ok().body("Meeting state updated");
    }
}