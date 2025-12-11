package controller;

import data.ResultFileHandler;
import domain.*;
import util.MemberNotFoundException;
import util.NotCompetitiveSwimmerException;
import util.SwimmerBestResultComparator;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * ResultController håndterer al logik relateret til svømmeresultater.
 * <p>
 * Controllerns ansvar:
 * - at tilføje træningsresultater
 * - at tilføje stævneresultater
 * - at indlæse alle resultater fra fil ved programstart
 * - at gemme alle resultater i fil når noget ændres
 * - at finde en konkurrencesvømmer ud fra telefonnummer
 */
public class ResultController {
    /**
     * Håndtering af indlæsning og lagring af resultater i "Results.txt".
     */
    private final ResultFileHandler fileHandler = new ResultFileHandler("Results.txt");
    /**
     * Reference til Database så controllern kan finde medlemmer.
     */
    private final Klubben klubben;

    /**
     * Konstruktør der modtager en database, så vi kan søge efter medlemmer.
     */
    public ResultController(Klubben klubben) {
        this.klubben = klubben;
    }

    /**
     * Tilføjer et træningsresultat til en konkurrencesvømmer.
     * <p>
     * 1. Find medlem via telefonnummer
     * 2. Tjek at medlem findes
     * 3. Tjek at medlem er konkurrencesvømmer (instanceof)
     * 4. Tilføj resultat
     * 5. Gem resultater i filen
     */
    public void addTrainingResult(String phone, Discipline discipline,
                                  int timeMilliSeconds, LocalDate date) {

        CompetitiveSwimmer competitiveSwimmer = getCompetitiveSwimmer(phone);

        // Tilføj resultatet
        competitiveSwimmer.addTrainingResult(new TrainingResult(discipline, timeMilliSeconds, date));
        saveResults(); // Gem i fil
    }

    /**
     * Tilføjer et stævneresultat til en konkurrencesvømmer.
     */
    public void addCompetitionResult(String phone, Discipline discipline, int timeMilliSeconds, LocalDate date, String eventName, int placement) {

        CompetitiveSwimmer competitiveSwimmer = getCompetitiveSwimmer(phone);

        //Tilføj stævneresultat
        competitiveSwimmer.addCompetitionResult(new CompetitionResult(discipline, timeMilliSeconds, date, eventName, placement));
        saveResults(); // Gem i fil
    }

    /**
     * Gemmer ALLE resultater for ALLE konkurrencesvømmere
     * ved at sende medlemslisten til ResultFileHandler.
     */
    public void saveResults() {
        fileHandler.saveAllResults(klubben.getAllMembers());
    }

    /**
     * Indlæser ALLE resultater og tilføjer dem til de rigtige
     * CompetitiveSwimmer objekter ved programstart.
     */
    public void loadResults() {
        fileHandler.loadAllResults(klubben.getAllMembers());
    }

    /**
     * Finder og returnerer en konkurrencesvømmer ud fra telefonnummer.
     * Smider fejl hvis:
     * - medlem ikke findes
     * - medlem findes, men ikke er konkurrencesvømmer
     */
    public CompetitiveSwimmer getCompetitiveSwimmer(String phone) {
        Member member = klubben.findByPhoneNr(phone);

        if (!(member instanceof CompetitiveSwimmer competitiveSwimmer)) {
            throw new NotCompetitiveSwimmerException("Medlem er ikke konkurrencesvømmer.");
        }

        return competitiveSwimmer;
    }

    /**
     * Finder top-5 hurtigste konkurrencesvømmere i en bestemt disciplin,
     * opdelt efter aldersgruppe (junior/senior).
     * <p>
     * Processen er:
     * 1. Gennemgå alle medlemmer i databasen
     * 2. Vælg kun konkurrencesvømmere
     * 3. Sorter dem efter bedste resultat i den valgte disciplin
     * 4. Returner de første fem svømmere (eller færre, hvis der ikke
     * findes fem med tider)
     *
     * @param discipline Disciplinen den sorteres efter
     * @param junior     true = junior, false = senior
     * @return enArrayList med top 5 svømmere
     */
    public ArrayList<CompetitiveSwimmer> getTop5(Discipline discipline, boolean junior) {
        ArrayList<CompetitiveSwimmer> list = new ArrayList<>();

        for (Member member : klubben.getAllMembers()) {
            if (member instanceof CompetitiveSwimmer swimmer &&
                    swimmer.getBestResultForDiscipline(discipline) != null) {

                if (junior && swimmer.getIsJunior()) {
                    list.add(swimmer);
                } else if (!junior && !swimmer.getIsJunior()) {
                    list.add(swimmer);
                }
            }
        }
        // 3. Sorter svømmere efter bedste tid
        list.sort(new SwimmerBestResultComparator(discipline));

        // 4. Returner en midlertidig liste med top 5
        int limit = Math.min(5, list.size());
        return new ArrayList<>(list.subList(0, limit));
    }
    /**
     * Fjerner alle gemte resultater for et medlem ud fra telefonnummer.
     * Bruges når et medlem slettes, så resultater ikke efterlades i filen.
     */
    public void removeResultsForMember(String phone) {
        fileHandler.removeResultsForMember(phone);
    }
}

