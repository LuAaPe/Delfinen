package domain;

import java.time.LocalDate;
import java.util.ArrayList;
/**
 * Klassen repræsenterer en konkurrencesvømmer.

 * Denne klasse udvider member og tilføjer:
 * - en liste med træningsresultater
 * - en liste med stævneresultater

 * CompetitiveSwimmer bruges af træneren i systemet
 * til at administrere resultater og sortering.
 */
public class CompetitiveSwimmer extends Member{
    /** Liste over alle træningsresultater for svømmeren.*/
    private final ArrayList<TrainingResult> trainingResults = new ArrayList<>();
    /** Liste over alle stævneresultater for svømmeren.*/
    private final ArrayList<CompetitionResult> competitionResults = new ArrayList<>();
    /**
     * Konstruktør for en konkurrencesvømmer.
     * Alle basis-informationer håndteres af superklassen Member.
     *
     * @param firstName     Fornavn
     * @param surName       Efternavn
     * @param phoneNumber   Telefonnummer
     * @param birthDate     Fødselsdato
     * @param isActive      Aktiv/passiv medlem
     * @param isPaid        Betalt/ikke betalt
     */
    public CompetitiveSwimmer(String firstName, String surName, String phoneNumber, LocalDate birthDate, boolean isActive, boolean isPaid) {
        // Kalder Members konstruktør (super)
        super(firstName, surName, phoneNumber, birthDate, isActive, isPaid);
        setIsCompetitive(true);
    }

    // - - - RESULTAT METODER - - - //

    /**
     * Tilføjer et træningsresultat til svømmeren.
     */
    public void addTrainingResult(TrainingResult trainingResult){
        trainingResults.add(trainingResult);
    }

    /**
     * Tilføjer et stævneresultat til svømmeren.
     */
    public void addCompetitionResult(CompetitionResult result){
        competitionResults.add(result);
    }

    /**
     * Returnerer listen med træningsresultater.
     * Bruges bl.a. til sortering i Menu.
     */
    public ArrayList<TrainingResult> getTrainingResults() {
        return trainingResults;
    }

    /**
     * Returnerer listen med stævneresultater.
     */
    public ArrayList<CompetitionResult> getCompetitionResults(){
        return competitionResults;
    }

    /**
     * Finder svømmerens bedste (hurtigste) resultat i en bestemt disciplin.

     * Metoden kigger både i:
     * - træningsresultater
     * - stævneresultater

     * Den gennemgår ALLE resultater i den givne disciplin og returnerer det
     * resultat som har den laveste tid (hurtigst).
     *
     */
    public Result getBestResultForDiscipline(Discipline discipline) {
        Result best = null;

        // Gennemgå ALLE træningsresultater
        for (Result result : trainingResults) {
                if(result.getDiscipline() == discipline){
                    if (best == null || result.getTimeMilliSeconds() < best.getTimeMilliSeconds()){
                        best = result;
                    }
                }
        }

        // Gennemgå ALLE stævneresultater
        for (CompetitionResult competitionResult : competitionResults){
            if (competitionResult.getDiscipline() == discipline) {
                    if (best == null || competitionResult.getTimeMilliSeconds() < best.getTimeMilliSeconds()){
                        best = competitionResult;
                    }
            }
        }
        return best;
    }
}