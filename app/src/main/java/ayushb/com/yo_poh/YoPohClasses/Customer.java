package ayushb.com.yo_poh.YoPohClasses;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ayushb on 28/9/15.
 */
public class Customer implements Serializable {
    private static final long serialVersionUID = 7526472295622776147L;
    String name;
    String address;
    String phoneNum;
    String customerId;
    String emailId;
    ArrayList<Product> products = new ArrayList<>();

    public Customer(){}

    public Customer(String name, String address, String phoneNum, String customerId, String emailId){
        this.name = name;
        this.address = address;
        this.customerId = customerId;
        this.phoneNum = phoneNum;
        this.emailId = emailId;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public String getAddress() {
        return address;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }
}
