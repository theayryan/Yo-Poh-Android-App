package ayushb.com.yo_poh.YoPohClasses;

import java.io.Serializable;

/**
 * Created by ayushb on 28/9/15.
 */
public class Complaint implements Serializable {
    private static final long serialVersionUID = 7526472295622776147L;
    String companyId;
    String productId;
    String customerId;
    long ticketNumber;
    long dateCreated;
    long dateResolved;
    String productName;
    String channel;
    String customerName;

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public long getDateResolved() {
        return dateResolved;
    }

    public void setDateResolved(long dateResolved) {
        this.dateResolved = dateResolved;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public long getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(long ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getChannel() {
        return channel;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getProductName() {
        return productName;
    }
}
