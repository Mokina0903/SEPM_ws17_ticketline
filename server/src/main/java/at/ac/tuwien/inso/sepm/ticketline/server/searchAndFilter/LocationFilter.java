package at.ac.tuwien.inso.sepm.ticketline.server.searchAndFilter;

import java.util.HashMap;

public class LocationFilter {

    private String description;
    private String country;
    private String city;
    private String street;
    private Integer zip;

    public LocationFilter(HashMap<String, String> parameters) {
        if (parameters.containsKey("descriptionEvent")) {
            this.description = parameters.get("descriptionEvent");
        }
        if (parameters.containsKey("country")) {
            this.country = parameters.get("country");
        }
        if (parameters.containsKey("street")) {
            this.street = parameters.get("street");
        }
        if (parameters.containsKey("zip")) {
            this.zip = Integer.parseInt(parameters.get("zip"));
        }
        if (parameters.containsKey("city")) {
            this.city = parameters.get("city");
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getZip() {
        return zip;
    }

    public void setZip(Integer zip) {
        this.zip = zip;
    }
}
