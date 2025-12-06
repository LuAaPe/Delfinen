package domain;

import java.time.LocalDate;

public class CompetitionResult extends Result{
    private final String eventName;
    private final int placement;

    public CompetitionResult(Discipline discipline, int timeMilliSeconds, LocalDate date, String eventName, int placement) {
        super(discipline, timeMilliSeconds, date);
        this.eventName = eventName;
        this.placement = placement;
    }

    public String getEventName() {
        return eventName;
    }

    public int getPlacement() {
        return placement;
    }
}
