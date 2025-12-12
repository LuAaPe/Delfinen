package domain;

import java.time.LocalDate;

/**
 * TrainingResult repræsenterer et træningsresultat for en konkurrencesvømmer.
 * Klassen nedarver fra Result, som er det samme som et almindeligt resultat.
 * Klassen tilføjer ingen ekstra felter eller metoder,
 * men eksisterer for at holde trænings- og stævneresultater adskilt,
 * så man kan arbejde med dem hver for sig.
 */
public class TrainingResult extends Result{

    /**
     * Opretter et nyt træningsresultat
     * Alle værdier sendes videre til superklassen(Result),
     * som håndterer selve lagringen af data.
     */
    public TrainingResult(Discipline discipline, int timeMilliSeconds, LocalDate date){
        super(discipline,timeMilliSeconds,date);
    }
}
