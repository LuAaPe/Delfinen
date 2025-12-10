package ui;

import controller.MemberController;
import controller.ResultController;
import domain.*;
import util.MemberNotFoundException;
import util.NotCompetitiveSwimmerException;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Scanner;

public class TrainerMenu {

    private final Scanner input;
    private final InputHelper inputHelper;
    private final ResultInput resultInput;
    private final ResultController resultController;
    private final MemberController memberController;

    public TrainerMenu(Scanner input, InputHelper inputHelper, ResultInput resultInput, ResultController resultController, MemberController memberController) {
        this.input = input;
        this.inputHelper = inputHelper;
        this.resultInput = resultInput;
        this.resultController = resultController;
        this.memberController = memberController;
    }

    /**
     * Menu for træner
     * - tilføj træningsresultat
     * - tilføj stævneresultat
     * - top 5 (senere)
     * - se alle konkurrencesvømmere
     * - se en svømmers resultater
     * flyttet til trinaer menu
     */
    public void show(){
        while(true){
            System.out.println("   TRÆNER:");
            System.out.println("""
                    ╔══════════════════════════╗
                    ║1. Tilføj træningsresultat║
                    ╚══════════════════════════╝
                    """);
            System.out.println("""
                    ╔════════════════════════╗
                    ║2. Tilføj stævneresultat║
                    ╚════════════════════════╝
                    """);
            System.out.println("3. Se top 5");
            System.out.println("""
                    ╔═════════════════════════════════════╗
                    ║4. Vis liste over konkurrencesvømmere║
                    ╚═════════════════════════════════════╝
                    """);
            System.out.println("""
                    ╔════════════════════════════╗
                    ║5. Vis en medlems resultater║
                    ╚════════════════════════════╝
                    """);
            System.out.println("""
                    ╔═══════════════╗
                    ║6. TILBAGE     ║
                    ╚═══════════════╝
                    """);
            System.out.println("""
                    ╔═══════════════╗
                    ║7. AFSLUT      ║
                    ╚═══════════════╝
                    """);

            try {
                System.out.print(": ");
                int trainerChoice = inputHelper.readInt();
                switch (trainerChoice){
                    case 1:
                        addTrainingResult();
                        break;
                    case 2:
                        addCompetitionResult();
                        break;
                    case 3:
                        // top 5
                        break;
                    case 4:
                        viewAllCompetitiveMembers();
                        break;
                    case 5:
                        viewMemberResuls();
                        break;
                    case 6:
                        return;
                    case 7:
                        // exit
                        input.close();
                        System.exit(0);
                        break;
                    default:
                        break;
                }
            }
            //TODO
            //Hvad skal der fanges her?
            catch (Exception e){
                System.out.println(e);
            }
        }
    }

    /**
     * Tilføjer et træningsresultat til en konkurrencesvømmer
     */
    private void addTrainingResult(){
        try {
            System.out.println("Indtast telefonnummer på svømmer (Tast 0 for at gå tilbage)\n: ");
            String phone = input.nextLine(); // TODO make more robust
            if (phone.equals("0")){
                return;
            }
            Discipline discipline = resultInput.enterDiscipline();
            int timeMilliSeconds = resultInput.enterSwimmingTime();
            CompetitiveSwimmer competitiveSwimmer = resultController.getCompetitiveSwimmer(phone);
            LocalDate date = resultInput.enterResultDate(competitiveSwimmer);

            resultController.addTrainingResult(phone, discipline, timeMilliSeconds, date);
            System.out.println("Træningsresultat gemt!");
        }
        catch (MemberNotFoundException | NotCompetitiveSwimmerException e){
            System.out.println("Fejl: "+ e.getMessage());
        }
        catch (Exception e){
            System.out.println("Et fejl upstod. "+ e.getMessage());
        }
    }

    /**
     * Tilføjer et stævneresultat
     */
    private void addCompetitionResult(){
        try {
            String phone = input.nextLine(); // TODO make more robust
            if (phone.equals("0")){
                return;
            }

            Discipline discipline = resultInput.enterDiscipline();
            int timeMilliSeconds = resultInput.enterSwimmingTime();

            CompetitiveSwimmer competitiveSwimmer = resultController.getCompetitiveSwimmer(phone);
            LocalDate date = resultInput.enterResultDate(competitiveSwimmer);

            System.out.println("Indtast stævne\n: ");
            String eventName = input.nextLine();

            System.out.println("Indtast placering (1, 2, 3,. . .\n: ");
            int placement = inputHelper.readInt();

            resultController.addCompetitionResult(phone, discipline, timeMilliSeconds, date, eventName, placement);
            System.out.println("Stævneresultat gemt!");

        }
        catch (MemberNotFoundException | NotCompetitiveSwimmerException e){
            System.out.println("Fejl: " + e.getMessage());
        }
        catch (Exception e){
            System.out.println("En fejl upstod: " + e.getMessage());
        }

    }



    /**
     * Viser alle konkurrencesvømmere
     */
    private void viewAllCompetitiveMembers(){
        System.out.println("\n - - - Liste over konkurrencesvømmere - - -");

        boolean foundSwimmers = false; // boolean flag for a simple check

        for ( Member member : memberController.getAllMembers()){
            if (member instanceof CompetitiveSwimmer competitiveSwimmer){
                System.out.printf("%-20s Tlf: %s ",
                        competitiveSwimmer.getFullName(),
                        competitiveSwimmer.getPhoneNr());
                System.out.println();
                foundSwimmers = true;
            }
        }

        if (!foundSwimmers){
            System.out.println("Ingen konkurrencesvømmere.");
        }

        System.out.println("------------------\n");
    }

    /**
     * Viser alle resultater for en konkurrencesvømmer,
     * sorteret efter brugerens valg
     */
    private void viewMemberResuls(){
        try {
            System.out.println("Indtast telefonnummer på svømmeren\n: ");
            String phone = input.nextLine();

            CompetitiveSwimmer competitiveSwimmer = resultController.getCompetitiveSwimmer(phone);

            // Først spørg hvordan vi skal sortere dem
            preferredSortingPrompt(competitiveSwimmer);

            System.out.println("\n- - - Resultater for "+
                    competitiveSwimmer.getFullName() + " - - -");

            // Træningsresultater:
            System.out.println("\nTræningsresultater:");
            if (competitiveSwimmer.getTrainingResults().isEmpty()){
                System.out.println("Ingen resultater.");
            }
            else {
                for (Result result : competitiveSwimmer.getTrainingResults()){
                    String line = String.format("%s - %s - %s",
                            result.getDate(),
                            result.getDiscipline(),
                            resultInput.formatTime(result.getTimeMilliSeconds()));
                    System.out.println(line);
                }
            }

            // Stævneresultater:
            System.out.println("\nStævneresultater:");
            if (competitiveSwimmer.getCompetitionResults().isEmpty()){
                System.out.println("Ingen resultater.");
            }
            else {
                for (CompetitionResult competitionResult : competitiveSwimmer.getCompetitionResults()){
                    String line = String.format("%s - %s - %s (%s, placering: %d)",
                            competitionResult.getDate(),
                            competitionResult.getDiscipline(),
                            resultInput.formatTime(competitionResult.getTimeMilliSeconds()),
                            competitionResult.getEventName(),
                            competitionResult.getPlacement());
                    System.out.println(line);

                }
            }
            System.out.println("\n--------------------------");
        }
        catch (MemberNotFoundException | NotCompetitiveSwimmerException e){
            System.out.println("Fejl: " + e.getMessage());
        }
    }

    /**
     * Spørger brugeren hvordan resultaterne skal sorteres
     */
    private void preferredSortingPrompt(CompetitiveSwimmer competitiveSwimmer){
        System.out.println("Hvordan vil du sortere resultater?");
        System.out.println("1. Hurtigste tid");
        System.out.println("2. Dato (nyeste først)");
        System.out.println("3. Disciplin");
        System.out.println("Valg : ");

        int choice = inputHelper.readInt();

        switch (choice) {
            case 1:
                // sorter efter tid (hurtigst først)
                sortByTime(competitiveSwimmer);
                break;
            case 2:
                // sorter efter dato (nyeste først)
                sortByDate(competitiveSwimmer);
                break;
            case 3:
                // Sortere efter disciplin alfabetisk
                sortByDiscipline(competitiveSwimmer);
                break;
            default:
                System.out.println("Ugyltigt valg - sorter efter hurtigste tid");
                sortByTime(competitiveSwimmer);
        }
    }
    /**
     * Sorterer resultater efter hurtigste tid først
     */
    private void sortByTime(CompetitiveSwimmer competitiveSwimmer){
        // sorter efter tid (hurtigst først)
        competitiveSwimmer.getTrainingResults().sort(Comparator.comparingInt(Result::getTimeMilliSeconds));
        competitiveSwimmer.getCompetitionResults().sort(Comparator.comparingInt(CompetitionResult::getTimeMilliSeconds));
    }
    /**
     * Sorterer resultater efter dato (nyeste først)
     */
    private void sortByDate(CompetitiveSwimmer competitiveSwimmer){
        // sorter efter dato (nyeste først)
        competitiveSwimmer.getTrainingResults().sort(Comparator.comparing(Result::getDate).reversed());
        competitiveSwimmer.getCompetitionResults().sort(Comparator.comparing(CompetitionResult::getDate).reversed());
    }

    /**
     * Sorterer resultater alfabetisk efter disciplin
     */
    private void sortByDiscipline(CompetitiveSwimmer competitiveSwimmer){
        // Sortere efter disciplin alfabetisk
        competitiveSwimmer.getTrainingResults().sort(Comparator.comparing(Result::getDiscipline));
        competitiveSwimmer.getCompetitionResults().sort(Comparator.comparing(CompetitionResult::getDiscipline));
    }
}
