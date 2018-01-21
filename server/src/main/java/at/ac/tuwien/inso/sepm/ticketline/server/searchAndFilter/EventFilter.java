package at.ac.tuwien.inso.sepm.ticketline.server.searchAndFilter;


import java.time.LocalDateTime;
import java.util.HashMap;

public class EventFilter {

    private String title;
    private String description;
    private Long priceFrom;
    private Long priceTo;
    private String date;
    private Long startTimeLowerBound;
    private Long startTimeUpperBound;
    private Long durationLowerBound;
    private Long durationUpperBound;
    private Boolean seats;
    private Boolean noSeats;
    private LocalDateTime upcoming;
    private LocalDateTime past;
    private String category;


    public EventFilter(HashMap<String, String> parameters) {
        if (parameters.containsKey("title")) {
            this.title = parameters.get("title");
        }
        if (parameters.containsKey("description")) {
            this.description = parameters.get("description");
        }
        if (parameters.containsKey("priceFrom")) {
            this.priceFrom = Long.parseLong(parameters.get("priceFrom"));
        }
        if (parameters.containsKey("priceTo")) {
            this.priceTo = Long.parseLong(parameters.get("priceTo"));
        }
        if (parameters.containsKey("eventDate")) {
            date = parameters.get("eventDate");

            System.out.println("Date in filter : " + this.date);
        }
        if (parameters.containsKey("timeOfStart")) {
            System.out.println(parameters.get("timeOfStart"));

            Long lowerBound = Long.parseLong(parameters.get("timeOfStart"));
            System.out.println("Event has time of start...");
            lowerBound -= 30;
            if (lowerBound < 0) {
                lowerBound += 24L*60L;
            }
            this.startTimeLowerBound = lowerBound;
            System.out.println("Filter Time low: " + lowerBound);

            Long upperBound = Long.parseLong(parameters.get("timeOfStart"));
            upperBound += 30;
            if (upperBound > 24L * 60L) {// ist länger als 24 sdt
                upperBound -= 24L * 60L;
            }
            System.out.println("Filter Time up: " + upperBound);
            this.startTimeUpperBound = upperBound;
        }
        if (parameters.containsKey("duration")) {
            System.out.println("Duration: " + parameters.get("duration"));
            Long lowerBound = Long.parseLong(parameters.get("duration"));
            lowerBound -= 30;
            if (lowerBound < 0) {
                lowerBound = 1L;
            }
            System.out.println("Dur low: " + lowerBound);
            this.durationLowerBound = lowerBound;

            long upperBound = Long.parseLong(parameters.get("duration"));
            upperBound += 30;
            if (upperBound >= 24 * 60) {
                upperBound = 24L * 60L;
            }
            this.durationLowerBound = upperBound;
            System.out.println("Dur up: " + upperBound);

        }
        if (parameters.containsKey("seats")) {
            this.seats = true;
        }
        if (parameters.containsKey("noSeats")) {
            this.noSeats = true;
        }
        if (parameters.containsKey("upcoming")) {
            this.upcoming = LocalDateTime.now().plusMinutes(30);
        }
        if (parameters.containsKey("past")) {
            this.past = LocalDateTime.now();
        }
        if (parameters.containsKey("category")) {
            this.category = parameters.get("category");
        }
    }

    private String minutesToTime(Long minutes) {
        Long hours = minutes / 60;
        Long minutesLeft = minutes % 60;
        System.out.println("Time in Filter: " + hours + " : " + minutesLeft);
        String time = String.format("%02d:%02d", hours, minutesLeft);
        return time;

    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getPriceFrom() {
        return priceFrom;
    }

    public void setPriceFrom(Long priceFrom) {
        this.priceFrom = priceFrom;
    }

    public Long getPriceTo() {
        return priceTo;
    }

    public void setPriceTo(Long priceTo) {
        this.priceTo = priceTo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getStartTimeLowerBound() {
        return startTimeLowerBound;
    }

    public void setStartTimeLowerBound(Long startTimeLowerBound) {
        this.startTimeLowerBound = startTimeLowerBound;
    }

    public Long getStartTimeUpperBound() {
        return startTimeUpperBound;
    }

    public void setStartTimeUpperBound(Long startTimeUpperBound) {
        this.startTimeUpperBound = startTimeUpperBound;
    }

    public Long getDurationLowerBound() {
        return durationLowerBound;
    }

    public void setDurationLowerBound(Long durationLowerBound) {
        this.durationLowerBound = durationLowerBound;
    }

    public Long getDurationUpperBound() {
        return durationUpperBound;
    }

    public void setDurationUpperBound(Long durationUpperBound) {
        this.durationUpperBound = durationUpperBound;
    }

    public Boolean getSeats() {
        return seats;
    }

    public void setSeats(Boolean seats) {
        this.seats = seats;
    }

    public Boolean getNoSeats() {
        return noSeats;
    }

    public void setNoSeats(Boolean noSeats) {
        this.noSeats = noSeats;
    }

    public LocalDateTime getUpcoming() {
        return upcoming;
    }

    public void setUpcoming(LocalDateTime upcoming) {
        this.upcoming = upcoming;
    }

    public LocalDateTime getPast() {
        return past;
    }

    public void setPast(LocalDateTime past) {
        this.past = past;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
