package at.ac.tuwien.inso.sepm.ticketline.server.searchAndFilter;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;

public class EventFilter {

    private String title;
    private String description;
    private Long priceFrom;
    private Long priceTo;
    private LocalDate date;
    private Long startTimeLowerBound;
    private Long startTimeUpperBound;
    private LocalTime durationLowerBound;
    private LocalTime durationUpperBound;
    private Boolean seats;
    private Boolean noSeats;
    private Boolean upcoming;
    private Boolean past;


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
        if (parameters.containsKey("timeOfStart")) {
            Long lowerBound = Long.parseLong(parameters.get("timeOfStart"));
            System.out.println("Event has time of start...");
            lowerBound -= 30;
            if (lowerBound < 0) {
                lowerBound += 24 * 60;
            }
            this.startTimeLowerBound = lowerBound;
          /*  LocalTime lowerBound = minutesToTime(startTimeLowerBound);
            this.startTimeLowerBound = lowerBound;*/
            System.out.println("Filter Time: " + lowerBound);

            Long upperBound = Long.parseLong(parameters.get("timeOfStart"));
            upperBound += 30;
            if (upperBound >= 24 * 60) {
                upperBound -= 24 * 60;
            }

            //LocalTime upperBound = minutesToTime(startTimeUpperBound);
            this.startTimeLowerBound = upperBound;
            System.out.println(upperBound);
        }
       /* if (parameters.containsKey("duration")) {
            long durationLowerBoundInMinutes = Long.parseLong(parameters.get("duration"));
            durationLowerBoundInMinutes -= 30;
            if (durationLowerBoundInMinutes < 0) {
                durationLowerBoundInMinutes += 24 * 60;
            }
            LocalTime lowerBound = minutesToTime(durationLowerBoundInMinutes);
            this.durationLowerBound = lowerBound;

            long durationUpperBoundInMinutes = Long.parseLong(parameters.get("duration"));
            durationUpperBoundInMinutes += 30;
            if (durationUpperBoundInMinutes >= 24 * 60) {
                durationUpperBoundInMinutes -= 24 * 60;
            }
            LocalTime upperBound = minutesToTime(durationUpperBoundInMinutes);
            this.durationLowerBound = upperBound;
        }*/
        if (parameters.containsKey("seats")) {
            this.seats = true;
        }
        if (parameters.containsKey("noSeats")) {
            this.noSeats = true;
        }
        if (parameters.containsKey("upcoming")) {
            this.upcoming = true;
        }
        if (parameters.containsKey("past")) {
            this.past = true;
        }

        //todo

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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
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

    public LocalTime getDurationLowerBound() {
        return durationLowerBound;
    }

    public void setDurationLowerBound(LocalTime durationLowerBound) {
        this.durationLowerBound = durationLowerBound;
    }

    public LocalTime getDurationUpperBound() {
        return durationUpperBound;
    }

    public void setDurationUpperBound(LocalTime durationUpperBound) {
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

    public Boolean getUpcoming() {
        return upcoming;
    }

    public void setUpcoming(Boolean upcoming) {
        this.upcoming = upcoming;
    }

    public Boolean getPast() {
        return past;
    }

    public void setPast(Boolean past) {
        this.past = past;
    }
}
