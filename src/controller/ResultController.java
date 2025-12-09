package controller;

import data.MemberFileHandler;
import data.ResultFileHandler;
import domain.*;
import util.MemberNotFoundException;
import util.NotCompetitiveSwimmerException;
import util.SwimmerBestResultComparator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * ResultController håndterer al logik relateret til svømmeresultater.
 *
 * Controllerns ansvar:
 * - at tilføje træningsresultater
 * - at tilføje stævneresultater
 * - at indlæse alle resultater fra fil ved programstart
 * - at gemme alle resultater i fil når noget ændres
 * - at finde en konkurrencesvømmer ud fra telefonnummer
 *
 */
public class ResultController {
    // TODO: getTop5(discipline, junior/senior)
    /** Håndtering af indlæsning og lagring af resultater i "Results.txt".*/
    private final ResultFileHandler fileHandler = new ResultFileHandler("Results.txt");
    /** Reference til Database så controllern kan finde medlemmer.*/
    private final Database database;

    /**
     * Konstruktør der modtager en database, så vi kan søge efter medlemmer.
     */
    public ResultController(Database database){
        this.database = database;
    }

    /**
     * Tilføjer et træningsresultat til en konkurrencesvømmer.
     *
     * 1. Find medlem via telefonnummer
     * 2. Tjek at medlem findes
     * 3. Tjek at medlem er konkurrencesvømmer (instanceof)
     * 4. Tilføj resultat
     * 5. Gem resultater i filen
     */
    public void addTrainingResult(String phone, Discipline discipline,
                                  int timeMilliSeconds, LocalDate date){
        Member member = database.findByPhoneNr(phone);

        if(member == null) {
            throw new MemberNotFoundException("Telefonnummer findes ikke.");
        }

        // Tjek om medlemmet er en konkurrencesvømmer
        if(!(member instanceof CompetitiveSwimmer competitiveSwimmer)){
            throw new NotCompetitiveSwimmerException("Medlem er ikke konkurrencesvømmer.");
        }

        // Tilføj resultatet
        competitiveSwimmer.addTrainingResult(new TrainingResult(discipline, timeMilliSeconds, date));
        saveResults(); // Gem i fil
    }

    /**
     * Tilføjer et stævneresultat til en konkurrencesvømmer.
     */
    public void addCompetitionResult(String phone, Discipline discipline, int timeMilliSeconds, LocalDate date, String eventName, int placement){
        Member member = database.findByPhoneNr(phone);

        if (member == null){
            throw new MemberNotFoundException("Telefonnummer findes ikke.");
        }

        // Tjek om medlemmet er en konkurrencesvømmer
        if(!(member instanceof CompetitiveSwimmer competitiveSwimmer)){
            throw new NotCompetitiveSwimmerException("Medlem er ikke konkurrencesvømmer.");
        }

        //Tilføj stævneresultat
        competitiveSwimmer.addCompetitionResult(new CompetitionResult(discipline, timeMilliSeconds, date, eventName, placement));
        saveResults(); // Gem i fil
    }

    /**
     * Gemmer ALLE resultater for ALLE konkurrencesvømmere
     * ved at sende medlemslisten til ResultFileHandler.
     */
    public void saveResults(){
        fileHandler.saveAllResults(database.getAllMembers());
    }

    /**
     * Indlæser ALLE resultater og tilføjer dem til de rigtige
     * CompetitiveSwimmer objekter ved programstart.
     */
    public void loadResults(){
        fileHandler.loadAllResults(database.getAllMembers());
    }

    /**
     * Finder og returnerer en konkurrencesvømmer ud fra telefonnummer.
     * Smider fejl hvis:
     * - medlem ikke findes
     * - medlem findes, men ikke er konkurrencesvømmer
     */
    public CompetitiveSwimmer getCompetitiveSwimmer(String phone){
        Member member = database.findByPhoneNr(phone);

        if (member == null){
            throw new MemberNotFoundException("Telefonnummeret findes ikke.");
        }

        if (!(member instanceof CompetitiveSwimmer competitiveSwimmer)){
            throw new NotCompetitiveSwimmerException("Medlem er ikke konkurrencesvømmer.");
        }

        return competitiveSwimmer;
    }

    /**
     * Finder top-5 hurtigste konkurrencesvømmere i en bestemt disciplin,
     * opdelt efter aldersgruppe (junior/senior).
     *
     * Processen er:
     * 1. Gennemgå alle medlemmer i databasen
     * 2. Vælg kun konkurrencesvømmere
     * 3. Sorter dem efter bedste resultat i den valgte disciplin
     * 4. Returner de første fem svømmere (eller færre, hvis der ikke
     * findes fem med tider)
     *
     *
     * @param discipline        Disciplinen den sorteres efter
     * @param junior            true = junior, false = senior
     *
     * @return                  enArrayList med top 5 svømmere
     */
    public ArrayList<CompetitiveSwimmer> getTop5(Discipline discipline, boolean junior){
        ArrayList<CompetitiveSwimmer> list = new ArrayList<>();

        // 1. Find alle relevante svømmere
        for (Member member: database.getAllMembers()){
            if (member instanceof CompetitiveSwimmer swimmer){
                boolean isJunior = swimmer.getAge() < 18;

                // spring svømmere over i forkert aldersgruppe
                if (isJunior != junior) continue;

                // svømmeren SKAL have en tid i disciplinen
                if (swimmer.getBestResultForDiscipline(discipline) != null){
                    list.add(swimmer);
                }
            }
        }

        // 2. Sorter svømmere efter bedste tid
        list.sort(new SwimmerBestResultComparator(discipline));

        // 3. Returner top 5
        if(list.size() > 5){
            return new ArrayList<>(list.subList(0,5));
        }
        else {
            return list;
        }
    }
}
