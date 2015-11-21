package ayushb.com.yo_poh.YoPohClasses;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ayushb on 28/9/15.
 */
public class Product implements Serializable{
    String productId;
    String productName;
    String category;
    String price;
    String companyId;
    ArrayList<String> imageUrls = new ArrayList<>();

    public ArrayList<String> getImageUrls() {
        return imageUrls;
    }

    public String getCategory() {
        return category;
    }

    public String getPrice() {
        return price;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setImageUrls(ArrayList<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyId() {
        return companyId;
    }
}
