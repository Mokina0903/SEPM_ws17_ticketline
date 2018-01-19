package at.ac.tuwien.inso.sepm.ticketline.rest.event;

import java.time.LocalDateTime;

public class StatistikRequest {

    LocalDateTime beginOfMonth;
    LocalDateTime endOfMonth;

    public StatistikRequest(LocalDateTime beginOfMonth, LocalDateTime endOfMonth) {
        this.beginOfMonth = beginOfMonth;
        this.endOfMonth = endOfMonth;
    }

    public LocalDateTime getBeginOfMonth() {
        return beginOfMonth;
    }

    public void setBeginOfMonth(LocalDateTime beginOfMonth) {
        this.beginOfMonth = beginOfMonth;
    }

    public LocalDateTime getEndOfMonth() {
        return endOfMonth;
    }

    public void setEndOfMonth(LocalDateTime endOfMonth) {
        this.endOfMonth = endOfMonth;
    }
}
