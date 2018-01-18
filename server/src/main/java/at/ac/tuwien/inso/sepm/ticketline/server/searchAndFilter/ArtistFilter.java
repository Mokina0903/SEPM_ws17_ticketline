package at.ac.tuwien.inso.sepm.ticketline.server.searchAndFilter;

import java.util.HashMap;

public class ArtistFilter {

    private String artistFirstName;
    private String artistLastName;

    public ArtistFilter(HashMap<String, String> parameters) {
        if (parameters.containsKey("artistFirstName")) {
            this.artistFirstName = parameters.get("artistFirstName");
        }
        if (parameters.containsKey("artistLastName")) {
            this.artistLastName = parameters.get("artistLastName");
        }
    }

    public String getArtistFirstName() {
        return artistFirstName;
    }

    public void setArtistFirstName(String artistFirstName) {
        this.artistFirstName = artistFirstName;
    }

    public String getArtistLastName() {
        return artistLastName;
    }

    public void setArtistLastName(String artistLastName) {
        this.artistLastName = artistLastName;
    }
}
