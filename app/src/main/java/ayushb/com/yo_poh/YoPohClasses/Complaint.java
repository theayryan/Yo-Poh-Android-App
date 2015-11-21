package ayushb.com.yo_poh.YoPohClasses;

import java.io.Serializable;

/**
 * Created by ayushb on 28/9/15.
 */
public class Complaint implements Serializable{
    private static final long serialVersionUID = 7526472295622776147L;
    String companyId;
    String productId;
    String customerId;
    long ticketNumber;
    long dateCreated;
    long dateResolved;

    public long getDateCreated() {
        return dateCreated;
    }

    public long getDateResolved() {
        return dateResolved;
    }

    public String getCompanyId() {
        return companyId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getProductId() {
        return productId;
    }

    public long getTicketNumber() {
        return ticketNumber;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setDateResolved(long dateResolved) {
        this.dateResolved = dateResolved;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setTicketNumber(long ticketNumber) {
        this.ticketNumber = ticketNumber;
    }
}
