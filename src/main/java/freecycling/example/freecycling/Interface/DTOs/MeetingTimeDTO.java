package freecycling.example.freecycling.Interface.DTOs;

import freecycling.example.freecycling.Core.MeetingTimeTable;

public class MeetingTimeDTO {
    
    private int idMeetingTimeTable;
    
    private String meetingSite;
    
    private String meetingTime;
    
    private String availability;
    
    private String meetingState;
    
    private Integer itemId;

    public MeetingTimeDTO() {
    }

    public MeetingTimeDTO(int idMeetingTimeTable, String meetingSite, String meetingTime, 
            String availability, String meetingState, Integer itemId) {
        this.idMeetingTimeTable = idMeetingTimeTable;
        this.meetingSite = meetingSite;
        this.meetingTime = meetingTime;
        this.availability = availability;
        this.meetingState = meetingState;
        this.itemId = itemId;
    }

    public MeetingTimeDTO(MeetingTimeTable meetingTimeTable) {
        this.idMeetingTimeTable = meetingTimeTable.getIdMeetingTimeTable();
        this.meetingSite = meetingTimeTable.getMeetingSite();
        this.meetingTime = meetingTimeTable.getMeetingTime();
        this.availability = meetingTimeTable.getAvailability();
        this.meetingState = meetingTimeTable.getMeetingState();
        this.itemId = meetingTimeTable.getItemId();
    }

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

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }
}