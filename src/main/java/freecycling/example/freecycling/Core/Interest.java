package freecycling.example.freecycling.Core;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "InterestTable")
public class Interest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idInterestTable")
    private int idInterestTable;
    
    @Column(name = "User", nullable = false, length = 45)
    private String user;
    
    @Column(name = "Item", nullable = false, length = 45)
    private String item;
    
    @Column(name = "TimeStamp")
    private Timestamp timeStamp;
    
    @Column(name = "Note", length = 45)
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