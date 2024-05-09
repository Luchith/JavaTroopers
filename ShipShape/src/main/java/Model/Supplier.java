package Model;

import java.util.ArrayList;


public class Supplier {
    private String name;
    private String contactInfo;
    private ArrayList<String> supplyItems;

    public Supplier(String name, String contactInfo, ArrayList<String> supplyItems) {
        this.name = name;
        this.contactInfo = contactInfo;
        this.supplyItems = supplyItems;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public ArrayList<String> getSupplyItems() {
        return supplyItems;
    }

    public void setSupplyItems(ArrayList<String> supplyItems) {
        this.supplyItems = supplyItems;
    }
}
