package ui;

import controller.MemberController;
import controller.ResultController;
import domain.*;
import util.MemberNotFoundException;
import util.NotCompetitiveSwimmerException;

import java.time.LocalDate;
import java.util.ArrayList;
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
    public void show() {
        while (true) {
            System.out.println("\n   TRÆNER-MENU:");
            System.out.println("""
                    ╔═══════════════════════════╗
                    ║1. Tilføj træningsresultat ║
                    ╚═══════════════════════════╝
                    """);
            System.out.println("""
                    ╔═════════════════════════╗
                    ║2. Tilføj stævneresultat ║
                    ╚═════════════════════════╝
                    """);
            System.out.println("""
                    ╔════════════╗
                    ║3. Se TOP 5 ║
                    ╚════════════╝
                    """);
            System.out.println("""
                    ╔══════════════════════════════════════╗
                    ║4. Vis liste over konkurrencesvømmere ║
                    ╚══════════════════════════════════════╝
                    """);
            System.out.println("""
                    ╔═════════════════════════════╗
                    ║5. Vis en medlems resultater ║
                    ╚═════════════════════════════╝
                    """);
            System.out.println("""
                    ╔═══════════╗
                    ║0. Tilbage ║
                    ╚═══════════╝
                    """);


            System.out.print(": ");
            int trainerChoice = inputHelper.readInt();
            switch (trainerChoice) {
                case 1:
                    addTrainingResult();
                    break;
                case 2:
                    addCompetitionResult();
                    break;
                case 3:
                    viewTop5();
                    break;
                case 4:
                    viewAllCompetitiveMembers();
                    break;
                case 5:
                    viewMemberResuls();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Ugyldigt valg.");
            }
        }
    }

    /**
     * Tilføjer et træningsresultat til en konkurrencesvømmer
     */
    private void addTrainingResult() {

        System.out.println("\n----------------------------------------");
        System.out.println("       TILFØJ TRÆNINGSRESULTAT");
        System.out.println("----------------------------------------");

        System.out.print("Telefonnummer (0 for tilbage): ");
        String phone = input.nextLine();
        if (phone.equals("0")) {
            return;
        }


        try {
            CompetitiveSwimmer competitiveSwimmer = resultController.getCompetitiveSwimmer(phone);

            Discipline discipline = resultInput.enterDiscipline();
            int timeMilliSeconds = resultInput.enterSwimmingTime();
            LocalDate date = resultInput.enterResultDate(competitiveSwimmer);

            resultController.addTrainingResult(phone, discipline, timeMilliSeconds, date);

            System.out.println("\n----------------------------------------");
            System.out.println("       TRÆNINGSRESULTAT GEMT");
            System.out.println("----------------------------------------");
            printMemberLine(competitiveSwimmer);

            System.out.println("Disciplin:  " + discipline);
            System.out.println("Tid:        " + resultInput.formatTime(timeMilliSeconds));
            System.out.println("Dato:       " + date);
            System.out.println("----------------------------------------\n");
        } catch (MemberNotFoundException | NotCompetitiveSwimmerException e) {
            System.out.println("\n----------------------------------------");
            System.out.println("                FEJL");
            System.out.println("----------------------------------------");
            System.out.println(e.getMessage());
            System.out.println("----------------------------------------\n");

        } catch (Exception e) {
            System.out.println("\n----------------------------------------");
            System.out.println("         UKENDT FEJL OPSTOD");
            System.out.println("----------------------------------------");
            System.out.println(e.getMessage());
            System.out.println("----------------------------------------\n");
        }
    }

    /**
     * Tilføjer et stævneresultat
     */
    private void addCompetitionResult() {
        System.out.println("\n----------------------------------------");
        System.out.println("        TILFØJ STÆVNERESULTAT");
        System.out.println("----------------------------------------");

        System.out.print("Telefonnummer (0 for tilbage): ");
        String phone = input.nextLine().trim();
        if (phone.equals("0")) {
            return;
        }

        try {
            CompetitiveSwimmer competitiveSwimmer = resultController.getCompetitiveSwimmer(phone);

            Discipline discipline = resultInput.enterDiscipline();
            int timeMilliSeconds = resultInput.enterSwimmingTime();
            LocalDate date = resultInput.enterResultDate(competitiveSwimmer);

            System.out.print("Stævnenavn: ");
            String eventName = input.nextLine();

            System.out.print("Indtast placering (1, 2, 3...): ");
            int placement = inputHelper.readInt();

            resultController.addCompetitionResult(phone, discipline, timeMilliSeconds, date, eventName, placement);
            System.out.println("\n----------------------------------------");
            System.out.println("        STÆVNERESULTAT GEMT");
            System.out.println("----------------------------------------");

            printMemberLine(competitiveSwimmer);

            System.out.println("Stævne:     " + eventName);
            System.out.println("Placering:  " + placement);
            System.out.println("Disciplin:  " + discipline);
            System.out.println("Tid:        " + resultInput.formatTime(timeMilliSeconds));
            System.out.println("Dato:       " + date);
            System.out.println("Dato:   " + date);

        } catch (MemberNotFoundException | NotCompetitiveSwimmerException e) {
            System.out.println("\n----------------------------------------");
            System.out.println("                FEJL");
            System.out.println("----------------------------------------");
            System.out.println(e.getMessage());
            System.out.println("----------------------------------------\n");
        } catch (Exception e) {
            System.out.println("\n----------------------------------------");
            System.out.println("         UKENDT FEJL OPSTOD");
            System.out.println("----------------------------------------");
            System.out.println(e.getMessage());
            System.out.println("----------------------------------------\n");
        }

    }


    /**
     * Viser alle konkurrencesvømmere
     */
    private void viewAllCompetitiveMembers() {
        System.out.println("\n----------------------------------------");
        System.out.println("       LISTE OVER KONKURRENCESVØMMERE");
        System.out.println("----------------------------------------");

        ArrayList<CompetitiveSwimmer> juniors = new ArrayList<>();
        ArrayList<CompetitiveSwimmer> seniors = new ArrayList<>();


        for (Member member : memberController.getAllMembers()) {
            if (member instanceof CompetitiveSwimmer competitiveSwimmer) {
                if (competitiveSwimmer.getAge() < 18) {
                    juniors.add(competitiveSwimmer);
                } else {
                    seniors.add(competitiveSwimmer);
                }
            }
        }

        if (juniors.isEmpty() && seniors.isEmpty()) {
            System.out.println("Ingen konkurrencesvømmere.");
            System.out.println("----------------------------------------\n");
            return;
        }

        System.out.println("\n----------------------------------------");
        System.out.println("           JUNIOR");
        System.out.println("----------------------------------------");
        if (juniors.isEmpty()) {
            System.out.println("Ingen juniorsvømmere.");
            System.out.println("----------------------------------------\n");

        } else {
            for (CompetitiveSwimmer swimmer : juniors) {
                printMemberLine(swimmer);
            }
        }

        System.out.println("\n----------------------------------------");
        System.out.println("           SENIOR");
        System.out.println("----------------------------------------");
        if (seniors.isEmpty()) {
            System.out.println("Ingen seniorsvømmere.");
            System.out.println("----------------------------------------\n");
        } else {
            for (CompetitiveSwimmer swimmer : seniors) {
                printMemberLine(swimmer);
            }
        }

        System.out.println("----------------------------------------\n");
    }


    /**
     * Viser alle resultater for en konkurrencesvømmer,
     * sorteret efter brugerens valg
     */
    private void viewMemberResuls() {
        try {
            System.out.println("\n----------------------------------------");
            System.out.println("     VIS RESULTATER FOR KONKURRENCESVØMMER");
            System.out.println("----------------------------------------");

            System.out.print("Telefonnummer: ");
            String phone = input.nextLine().trim();

            CompetitiveSwimmer competitiveSwimmer = resultController.getCompetitiveSwimmer(phone);

            // Først spørg hvordan vi skal sortere dem
            preferredSortingPrompt(competitiveSwimmer);

            System.out.println("\n----------------------------------------");
            System.out.println("          RESULTATER FOR " + competitiveSwimmer.getFullName());
            System.out.println("----------------------------------------");

            printMemberLine(competitiveSwimmer);
            System.out.println("----------------------------------------");

            // Træningsresultater:
            System.out.println("\nTræningsresultater:");
            if (competitiveSwimmer.getTrainingResults().isEmpty()) {
                System.out.println("Ingen træningsresultater.");
            } else {
                for (Result result : competitiveSwimmer.getTrainingResults()) {
                    System.out.printf(
                            "%-12s | %-12s | %s\n",
                            result.getDate(),
                            result.getDiscipline(),
                            resultInput.formatTime(result.getTimeMilliSeconds()));
                }
            }

            // Stævneresultater:
            System.out.println("\nStævneresultater:");
            if (competitiveSwimmer.getCompetitionResults().isEmpty()) {
                System.out.println("Ingen stævneresultater.");
            } else {
                for (CompetitionResult competitionResult : competitiveSwimmer.getCompetitionResults()) {
                    System.out.printf(
                            "%-12s | %-12s | %s | %s (placering: %d)\n",
                            competitionResult.getDate(),
                            competitionResult.getDiscipline(),
                            resultInput.formatTime(competitionResult.getTimeMilliSeconds()),
                            competitionResult.getEventName(),
                            competitionResult.getPlacement()
                    );

                }
            }
            System.out.println("\n----------------------------------------\n");

        } catch (MemberNotFoundException | NotCompetitiveSwimmerException e) {
            System.out.println("\n----------------------------------------");
            System.out.println("                 FEJL");
            System.out.println("----------------------------------------");
            System.out.println(e.getMessage());
            System.out.println("----------------------------------------\n");
        }
    }

    /**
     * Spørger brugeren hvordan resultaterne skal sorteres
     */
    private void preferredSortingPrompt(CompetitiveSwimmer competitiveSwimmer) {
        System.out.println("Hvordan vil du sortere resultater?");
        System.out.println("1. Hurtigste tid");
        System.out.println("2. Dato (nyeste først)");
        System.out.println("3. Disciplin");
        System.out.print(": ");

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
                System.out.println("Ugyldigt valg - sorter efter hurtigste tid");
                sortByTime(competitiveSwimmer);
        }
    }

    private void viewTop5() {
        System.out.println("\n----------------------------------------");
        System.out.println("              TOP 5 MENU");
        System.out.println("----------------------------------------");

        System.out.println("Vælg disciplin:");
        System.out.println("1. Butterfly");
        System.out.println("2. Crawl");
        System.out.println("3. Rygcrawl");
        System.out.println("4. Brystsvømning");
        System.out.println("----------------------------------------");
        System.out.print("Valg: ");

        int choice = inputHelper.readInt();
        Discipline discipline;

        switch (choice) {
            case 1 -> discipline = Discipline.BUTTERFLY;
            case 2 -> discipline = Discipline.CRAWL;
            case 3 -> discipline = Discipline.BACKSTROKE;
            case 4 -> discipline = Discipline.BREASTSTROKE;
            default -> {
                System.out.println("Ugyldigt valg");
                return;
            }
        }
        System.out.println("\n----------------------------------------");
        System.out.println("""
                Vælg aldersgruppe:
                1. Junior (<18)
                2. Senior (18+)
                """);
        System.out.println("----------------------------------------");
        System.out.print("Valg: ");
        int ageChoice = inputHelper.readInt();
        boolean isJunior;

        switch (ageChoice) {
            case 1 -> isJunior = true;
            case 2 -> isJunior = false;
            default -> {
                System.out.println("Ugyldigt valg.");
                return;
            }
        }

        ArrayList<CompetitiveSwimmer> top5 = resultController.getTop5(discipline, isJunior);

        System.out.println("\n----------------------------------------");
        System.out.println("         TOP 5 " + discipline +
                " (" + (isJunior ? "Junior" : "Senior") + ")");
        System.out.println("----------------------------------------\n");

        if (top5.isEmpty()) {
            System.out.println("Ingen resultater fundet.\n");
            return;
        }

        int rank = 1;
        for (CompetitiveSwimmer swimmer : top5) {
            var best = swimmer.getBestResultForDiscipline(discipline);

            System.out.println(rank++ + ".");
            printMemberLine(swimmer);

            System.out.println("   Bedste tid: " + best.getFormattedTime());
            System.out.println();
        }

        System.out.println("\n----------------------------------------\n");

    }


    /**
     * Sorterer resultater efter hurtigste tid først
     */
    private void sortByTime(CompetitiveSwimmer competitiveSwimmer) {
        // sorter efter tid (hurtigst først)
        competitiveSwimmer.getTrainingResults().sort(Comparator.comparingInt(Result::getTimeMilliSeconds));
        competitiveSwimmer.getCompetitionResults().sort(Comparator.comparingInt(CompetitionResult::getTimeMilliSeconds));
    }

    /**
     * Sorterer resultater efter dato (nyeste først)
     */
    private void sortByDate(CompetitiveSwimmer competitiveSwimmer) {
        // sorter efter dato (nyeste først)
        competitiveSwimmer.getTrainingResults().sort(Comparator.comparing(Result::getDate).reversed());
        competitiveSwimmer.getCompetitionResults().sort(Comparator.comparing(CompetitionResult::getDate).reversed());
    }

    /**
     * Sorterer resultater alfabetisk efter disciplin
     */
    private void sortByDiscipline(CompetitiveSwimmer competitiveSwimmer) {
        // Sortere efter disciplin alfabetisk
        competitiveSwimmer.getTrainingResults().sort(Comparator.comparing(Result::getDiscipline));
        competitiveSwimmer.getCompetitionResults().sort(Comparator.comparing(CompetitionResult::getDiscipline));
    }

    /**
     * Udskriver ét medlem i et ensartet format.
     * Samme format som i ChairmanMenu og TreasurerMenu.
     */
    private void printMemberLine(Member member) {
        if (member == null) {
            System.out.println("Ingen medlemsdata at vise.");
            return;
        }

        String type = (member instanceof CompetitiveSwimmer) ? "Konkurrence" : "Motionist";
        String status = member.getIsActive() ? "Aktiv" : "Passiv";
        int age = member.getAge();

        System.out.printf(
                "%-20s | Tlf: %-12s | Alder: %-3d | %-10s | %-11s\n",
                member.getFullName(),
                member.getPhoneNr(),
                age,
                status,
                type
        );
    }
}
