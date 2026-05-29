package freecycling.example.freecycling.Interface;

import freecycling.example.freecycling.Core.MeetingTimeTable;
import freecycling.example.freecycling.Core.MeetingTimeTableService;
import freecycling.example.freecycling.Interface.DTOs.MeetingTimeDTO;
import freecycling.example.freecycling.security.FreecyclingUserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/meetings")
@CrossOrigin(origins = "*")
public class MeetingTimeTableController {

    @Autowired
    private MeetingTimeTableService service;

    // POST - save a new meeting time (protected)
    // Called with POST /meetings
    @PostMapping
    public ResponseEntity<String> save(
            Authentication authentication,
            @RequestBody MeetingTimeDTO meetingDTO) {
        FreecyclingUserDetails details = (FreecyclingUserDetails) authentication.getPrincipal();
        int userId = Integer.parseInt(details.getUsername());
        if (meetingDTO.getMeetingSite().isBlank() || meetingDTO.getMeetingTime().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Meeting site and time cannot be empty");
        }
        service.save(meetingDTO);
        return ResponseEntity.ok().body("Meeting time saved");
    }

    // GET - find a meeting by id (protected)
    // Called with GET /meetings/5
    @GetMapping("/{id}")
    public ResponseEntity<MeetingTimeDTO> getMeetingById(@PathVariable int id) {
        MeetingTimeTable result = service.findById(id);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok().body(new MeetingTimeDTO(result));
    }

    // GET - get all meetings (protected)
    // Called with GET /meetings
    @GetMapping
    public ResponseEntity<List<MeetingTimeDTO>> getAllMeetings() {
        List<MeetingTimeDTO> results = service.findAll()
            .stream()
            .map(MeetingTimeDTO::new)
            .collect(Collectors.toList());
        return ResponseEntity.ok().body(results);
    }

    // GET - find meetings by site (protected)
    // Called with GET /meetings/site/Main Library
    @GetMapping("/site/{site}")
    public ResponseEntity<List<MeetingTimeDTO>> getMeetingsBySite(@PathVariable String site) {
        List<MeetingTimeDTO> results = service.findBySite(site)
            .stream()
            .map(MeetingTimeDTO::new)
            .collect(Collectors.toList());
        return ResponseEntity.ok().body(results);
    }

    // GET - find meetings by date (protected)
    // Called with GET /meetings/date/2026-05-01
    @GetMapping("/date/{date}")
    public ResponseEntity<List<MeetingTimeDTO>> getMeetingsByDate(@PathVariable String date) {
        List<MeetingTimeDTO> results = service.findByDate(date)
            .stream()
            .map(MeetingTimeDTO::new)
            .collect(Collectors.toList());
        return ResponseEntity.ok().body(results);
    }

    // GET - find available meeting times by site and date (public)
    // Called with GET /meetings/available?site=Main Library&date=2026-05-01
    @GetMapping("/available")
    public ResponseEntity<List<MeetingTimeDTO>> getAvailableTimes(
            @RequestParam String site,
            @RequestParam String date) {
        List<MeetingTimeDTO> results = service.findAvailableTimes(site, date)
            .stream()
            .map(MeetingTimeDTO::new)
            .collect(Collectors.toList());
        return ResponseEntity.ok().body(results);
    }

    // POST - book a meeting slot (protected)
    // Called with POST /meetings/book/2?itemId=1
    @PostMapping("/book/{id}")
    public ResponseEntity<String> bookMeeting(
            Authentication authentication,
            @PathVariable int id,
            @RequestParam int itemId) {
        FreecyclingUserDetails details = (FreecyclingUserDetails) authentication.getPrincipal();
        int userId = Integer.parseInt(details.getUsername());
        MeetingTimeTable result = service.findById(id);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Meeting not found");
        }
        if (result.getAvailability().equals("Unavailable")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Meeting slot is already booked");
        }
        service.bookMeeting(id, itemId);
        return ResponseEntity.ok().body("Meeting booked successfully");
    }

    // PATCH - update meeting state by id (protected)
    // Called with PATCH /meetings/state/5
    @PatchMapping("/state/{id}")
    public ResponseEntity<String> updateMeetingState(
            Authentication authentication,
            @PathVariable int id,
            @RequestBody String meetingState) {
        FreecyclingUserDetails details = (FreecyclingUserDetails) authentication.getPrincipal();
        int userId = Integer.parseInt(details.getUsername());
        if (meetingState == null || meetingState.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Meeting state cannot be empty");
        }
        service.updateMeetingState(id, meetingState);
        return ResponseEntity.ok().body("Meeting state updated");
    }
}