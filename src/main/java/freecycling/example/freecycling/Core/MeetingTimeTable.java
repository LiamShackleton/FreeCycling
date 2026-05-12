package freecycling.example.freecycling.Core;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "MeetingTimeTable")
public class MeetingTimeTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idMeetingTimeTable;
    private String meetingSite;
    private String meetingTime;
    private String availability;
    private String meetingState;
    private int itemId;

    public MeetingTimeTable() {}

    public int getIdMeetingTimeTable() {
        return idMeetingTimeTable;
    }

    public void setIdMeetingTimeTable(int idMeetingTimeTable) {
        this.idMeetingTimeTable = idMeetingTimeTable;
    }

    public String getMeetingSite() {
        return meetingSite;
    }

    public void setMeetingSite(String meetingSite) {
        this.meetingSite = meetingSite;
    }

    public String getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(String meetingTime) {
        this.meetingTime = meetingTime;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getMeetingState() {
        return meetingState;
    }

    public void setMeetingState(String meetingState) {
        this.meetingState = meetingState;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
}