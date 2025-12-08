package domain;

import java.time.LocalDate;

/**
 * CompetitionResult repræsenterer et stævneresultat for en konkurrencesvømmer.
 *
 * Klassen arver fra Result, som indeholder:
 * - disciplin
 * - tid i millisekunder
 * - dato for resultatet
 *
 * CompetitionResult tilføjer to ekstra oplysninger:
 * - eventName, navnet på stævnet
 * - placement, hvilken placering svømmeren fik (1, 2, 3, . . .)
 */
public class CompetitionResult extends Result{
    /** Navnet på stævnet hvor resultatet blev opnået.*/
    private final String eventName;
    /** Svømmerens placering i løbet.*/
    private final int placement;

    /**
     * Konstruktør der opretter et stævneresultat.
     */
    public CompetitionResult(Discipline discipline, int timeMilliSeconds, LocalDate date, String eventName, int placement) {
        // Kalder konstruktøren i superklassen Result
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
