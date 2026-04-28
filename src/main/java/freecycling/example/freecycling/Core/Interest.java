package freecycling.example.freecycling.Core;

import java.sql.Timestamp;

public class Interest {
    private int idInterestTable;
    private String user;
    private String item;
    private Timestamp timeStamp;
    private String note;

    public Interest() {
    }

    public int getIdInterestTable() {
        return idInterestTable;
    }

    public void setIdInterestTable(int idInterestTable) {
        this.idInterestTable = idInterestTable;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
