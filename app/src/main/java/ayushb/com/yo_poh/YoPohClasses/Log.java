package ayushb.com.yo_poh.YoPohClasses;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ayushb on 28/9/15.
 */
public class Log implements Serializable {
    String ticketNumber;
    long dateCreated;
    ArrayList<Long> dateModified = new ArrayList<>();

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public ArrayList<Long> getDateModified() {
        return dateModified;
    }

    public void setDateModified(ArrayList<Long> dateModified) {
        this.dateModified = dateModified;
    }
}
