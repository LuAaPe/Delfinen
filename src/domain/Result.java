package domain;

import java.time.LocalDate;

/**
 * Result repræsenterer et simpelt svømmeresultat for en konkurrencesvømmer.
 *
 * Klassen indeholder:
 * - disciplin
 * - tid i millisekunder
 * - dato for resultatet
 *
 * Klassen er en "databeholder", som CompetitionResult arver fra.
 */
public class Result {
    /** Svømmedisciplin: butterfly, craw, bryst, osv. */
    private final Discipline discipline;
    /** Svømmetiden målt i millisekunder (bruges til at sortere og sammenligne).*/
    private final int timeMilliSeconds;
    /** Datoen hvor resultatet blev opnået.*/
    private final LocalDate date;

    /**
     * Constructor der opretter et almindeligt træningsresultat.
     */
    public Result(Discipline discipline, int timeMilliSeconds, LocalDate date){
        this.discipline = discipline;
        this.timeMilliSeconds = timeMilliSeconds;
        this.date = date;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public int getTimeMilliSeconds() {
        return timeMilliSeconds;
    }

    public LocalDate getDate() {
        return date;
    }

    /**
     * Formaterer tiden i et læsbart format:
     * MM:SS.mmm
     *
     * Eksempel:
     * timeMilleSeconds = 48300 --> "0:48.300"
     */
    public String getFormattedTime(){
        int totalMilliSeconds = timeMilliSeconds;
        int minutes = totalMilliSeconds / 60000;
        int seconds = (totalMilliSeconds % 60000) / 1000;
        int milliSeconds = totalMilliSeconds % 1000;

        return String.format("%d:%02d.%03d", minutes, seconds, milliSeconds);
    }
}



