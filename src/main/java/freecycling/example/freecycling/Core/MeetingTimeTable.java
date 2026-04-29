package freecycling.example.freecycling.Core;

public class MeetingTimeTable {

    private int idMeetingTimeTable;
    private String MeetingSite;
    private String MeetingTime;
    private String Availability;
    private String MeetingState;

    public MeetingTimeTable() {

}

public int getIdMeetingTimeTable() {
        return idMeetingTimeTable;
    }

    public void setIdMeetingTimeTable(int idMeetingTimeTable) {
    this.idMeetingTimeTable = idMeetingTimeTable;
}

    public String getMeetingSite() {
        return MeetingSite;
    }

    public void setMeetingSite(String MeetingSite) {
        this.MeetingSite = MeetingSite;
    }

    public String getMeetingTime() {
        return MeetingTime;
    }

    public void setMeetingTime(String MeetingTime) {
        this.MeetingTime = MeetingTime;
    }

    public String getAvailability() {
        return Availability;
    }

    public void setAvailability(String Availability) {
        this.Availability = Availability;
    }

    public String getMeetingState() {
        return MeetingState;
    }

    public void setMeetingState(String MeetingState) {
        this.MeetingState = MeetingState;
    }
    
}