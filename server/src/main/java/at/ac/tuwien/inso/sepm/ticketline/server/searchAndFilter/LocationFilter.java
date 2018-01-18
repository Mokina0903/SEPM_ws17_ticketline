package at.ac.tuwien.inso.sepm.ticketline.server.searchAndFilter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LocationFilter {

    private String description;
    private String country;
    private String street;
    private int zip = -1;

    public LocationFilter(HashMap<String, String> parameters) {
        Iterator it = parameters.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println("print Map in locFilter: "+pair.getKey() + " = " + pair.getValue());
        }

        if (parameters.containsKey("description")) {
            this.description = parameters.get("description");
        }
        if (parameters.containsKey("country")) {
            this.country = parameters.get("country");
        }
        if (parameters.containsKey("street")) {
            this.street = parameters.get("street");
        }
        if (parameters.containsKey("plz")) {
                this.zip = Integer.parseInt(parameters.get("plz"));
                System.out.println("ZIP of loc = " + zip);
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }
}
