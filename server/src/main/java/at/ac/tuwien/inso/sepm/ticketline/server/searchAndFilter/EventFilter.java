package at.ac.tuwien.inso.sepm.ticketline.server.searchAndFilter;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;

public class EventFilter {

    private String title;
    private String description;
    private long priceFrom = 0;
    private long priceTo = Long.MAX_VALUE;
    private LocalDate date;
    private LocalTime startTimeLowerBound;
    private LocalTime startTimeUpperBound;
    private LocalTime durationLowerBound;
    private LocalTime durationUpperBound;

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
            long earlyStartInMinutes = Long.parseLong(parameters.get("timeOfStart"));
            System.out.println("Event has time of start...");
            earlyStartInMinutes -= 30;
            if (earlyStartInMinutes < 0) {
                earlyStartInMinutes += 24 * 60;
            }
            LocalTime lowerBound = minutesToTime(earlyStartInMinutes);
            this.startTimeLowerBound = lowerBound;
            System.out.println(lowerBound);

            long lateStartInMinutes = Long.parseLong(parameters.get("timeOfStart"));
            lateStartInMinutes += 30;
            if (lateStartInMinutes >= 24 * 60) {
                lateStartInMinutes -= 24 * 60;
            }

            LocalTime upperBound = minutesToTime(lateStartInMinutes);
            this.startTimeLowerBound = upperBound;
            System.out.println(upperBound);
        }
        if (parameters.containsKey("duration")) {
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
            this.durationLowerBound = upperBound;        }

        //todo

    }

    private LocalTime minutesToTime(long minutes) {
        long hours = minutes / 60;
        long minutesLeft = minutes % 60;
        System.out.println("Time in Filter: " +hours + " : " + minutesLeft);
        String time = String.format("%02d:%02d", hours, minutesLeft);

        return LocalTime.parse(time);

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

    public long getPriceFrom() {
        return priceFrom;
    }

    public void setPriceFrom(long priceFrom) {
        this.priceFrom = priceFrom;
    }

    public long getPriceTo() {
        return priceTo;
    }

    public void setPriceTo(long priceTo) {
        this.priceTo = priceTo;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTimeLowerBound() {
        return startTimeLowerBound;
    }

    public void setStartTimeLowerBound(LocalTime startTimeLowerBound) {
        this.startTimeLowerBound = startTimeLowerBound;
    }

    public LocalTime getStartTimeUpperBound() {
        return startTimeUpperBound;
    }

    public void setStartTimeUpperBound(LocalTime startTimeUpperBound) {
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
}
