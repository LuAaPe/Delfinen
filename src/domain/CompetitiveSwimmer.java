package domain;

import java.time.LocalDate;
import java.util.ArrayList;
/**
 * Klassen repræsenterer en konkurrencesvømmer.
 *
 * Denne klasse udvider member og tilføjer:
 * - en liste med træningsresultater
 * - en liste med stævneresultater
 *
 * CompetitiveSwimmer bruges af træneren i systemet
 * til at administrere resultater og sortering.
 */
public class CompetitiveSwimmer extends Member{
    /** Liste over alle træningsresultater for svømmeren.*/
    private ArrayList<Result> trainingResults = new ArrayList<>();
    /** Liste over alle stævneresultater for svømmeren.*/
    private ArrayList<CompetitionResult> competitionResults = new ArrayList<>();

    /**
     * Konstruktør for en konkurrencesvømmer.
     * Alle basis-informationer håndteres af superklassen Member.
     *
     * @param firstName     Fornavn
     * @param surName       Efternavn
     * @param phoneNumber   Telefonnummer
     * @param birthDate     Fødselsdato
     * @param isCompetitive Skal altid være true for denne type
     * @param isActive      Aktiv/passiv medlem
     * @param isPaid        Betalt/ikke betalt
     */
    public CompetitiveSwimmer(String firstName, String surName, String phoneNumber, LocalDate birthDate, boolean isCompetitive, boolean isActive, boolean isPaid) {
        // Kalder Members konstruktør (super)
        super(firstName, surName, phoneNumber, birthDate, isCompetitive, isActive, isPaid);
    }

    // - - - RESULTAT METODER - - - //

    /**
     * Tilføjer et træningsresultat til svømmeren.
     */
    public void addTrainingResult(Result result){
        trainingResults.add(result);
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
    public ArrayList<Result> getTrainingResults() {
        return trainingResults;
    }

    /**
     * Returnerer listen med stævneresultater.
     */
    public ArrayList<CompetitionResult> getCompetitionResults(){
        return competitionResults;
    }

}