package ayushb.com.yo_poh.YoPohClasses;

import java.io.Serializable;

/**
 * Created by ayushb on 28/9/15.
 */
public class Company implements Serializable {
    String companyName;
    String companyDivision;
    String companyId;

    public String getCompanyDivision() {
        return companyDivision;
    }

    public void setCompanyDivision(String companyDivision) {
        this.companyDivision = companyDivision;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
