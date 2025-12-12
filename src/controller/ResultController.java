package controller;

import data.ResultFileHandler;
import domain.*;
import util.NotCompetitiveSwimmerException;
import util.SwimmerBestResultComparator;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * ResultController håndterer al logik relateret til svømmeresultater.
 * Controllern fungerer som et mellemled mellem UI (trænermenuen),
 * Klubben(medlemsdata), og ResultFilehandler(fil-håndtering).
 * Controllerns ansvar:
 * - Tilføje træningsresultater
 * - Tilføje stævneresultater
 * - Indlæse resultater fra fil ved programstart
 * - Gemme resultater når noget ændres
 * - Kontrollere om et telefonnummer tilhører en konkurrencesvømmer
 * - Udvælge top-5 svømmere i en disciplin (junior/senior)
 */
public class ResultController {
    /**
     * Håndtering af indlæsning og lagring af resultater i "Results.txt".
     */
    private final ResultFileHandler fileHandler = new ResultFileHandler("Results.txt");
    /**
     * Reference til Klubben, så controllern kan finde medlemmer
     * og tilgå deres resultater.
     */
    private final Klubben klubben;

    /**
     * Opretter en ResultController.
     */
    public ResultController(Klubben klubben) {
        this.klubben = klubben;
    }

    /**
     * Tilføjer et træningsresultat til en konkurrencesvømmer.
     * <p>
     * 1. Find medlem via telefonnummer
     * 2. Tjek at medlem er konkurrencesvømmer (instanceof)
     * 3. Opret et TrainingResult-objekt
     * 4. Tilføj det til svømmerens liste
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
     * Minder om addTrainingResult, men her:
     * - Bruges CompetitionResult (med stævnenavn og placering)
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
     * ResultFileHandler søger selv frem til de rigtige medlemmer,
     * så deres resultater bliver knyttet til de eksisterende objekter.
     */
    public void loadResults() {
        fileHandler.loadAllResults(klubben.getAllMembers());
    }

    /**
     * Finder et medlem ud fra telefonnummer og sikrer,
     * at det er en konkurrencesvømmer.
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
     * 1. Gennemgå alle medlemmer
     * 2. Filtrér dem der:
     *      - er konkurrencesvømmere
     *      - har et resultat i den ønskede disciplin
     *      - tilhører den rigtige aldersgruppe (junior/senior)
     * 3. Sortér ved hjælp af SwimmerBestResultComparator
     * 4. Returnér de første 5 (eller færre, hvis der ikke er 5)
     * Bruges i træner-menuen.
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

