package at.ac.tuwien.inso.sepm.ticketline.server.searchAndFilter;


import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;

public class EventFilter {

    private String title;
    private String description;
    private long priceFrom = 0;
    private long priceTo = Long.MAX_VALUE;
    private LocalDate date;
    private LocalTime timeStart;
    private LocalTime timeEnd;
    private long duration;

    public EventFilter(HashMap<String, String> parameters) {
        System.out.println("HAs Parameter key? " + parameters.containsKey("title"));
        if (parameters.containsKey("title")){
            System.out.println("Has title **************************");
            this.title = parameters.get("title");
        }
        System.out.println("_____ in EventFilter:" +parameters.values());
        //todo

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

    public LocalTime getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(LocalTime timeStart) {
        this.timeStart = timeStart;
    }

    public LocalTime getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(LocalTime timeEnd) {
        this.timeEnd = timeEnd;
    }

    public long getDuration() {
        return timeEnd.getMinute() - timeStart.getMinute();
    }

    public void setDuration(long duration) {
        this.duration = Duration.between(timeEnd, timeStart).getSeconds();
    }
}
