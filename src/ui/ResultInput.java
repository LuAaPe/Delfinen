package ui;

import domain.CompetitiveSwimmer;
import domain.Discipline;
import util.InvalidResultDateException;

import java.time.LocalDate;
import java.util.Scanner;

/**
 * ResultInput er en hjælpeklasse i UI, der håndterer alt input
 * relateret til svømmeresultater.
 * Klassen bruges af TrainerMenu til at:
 * - vælge disciplin
 * - indtaste svømmetider i forskellige formater
 * - indtaste og validere datoer for resultater
 * - formatere tid fra millisekunder til tekst
 * Formålet er at samle alle input-relaterede funktioner ét sted,
 * så TrainerMenu bliver mere overskuelig.
 */
public class ResultInput {
    /** Scanner til brugerinput fra konsollen */
    private final Scanner input;
    /** Hjælpeklasse til valideret input (eks. heltal) */
    private final InputHelper inputHelper;

    /**
     * Opretter et ResultInput-objekt.
     */
    public ResultInput(Scanner input, InputHelper inputHelper) {
        this.input = input;
        this.inputHelper = inputHelper;
    }


    /**
     * Viser en lille menu hvor træneren vælger disciplin.
     * Metoden bliver ved indtil brugeren har indtastet et gyldigt valg.
     * Returnerer den valgte disciplin som en Discipline-enum
     */
    public Discipline enterDiscipline(){
        System.out.println("""
                --- INDTAST DISCIPLIN ---
                1. Butterfly
                2. Crawl
                3. Backstroke
                4. Breaststroke
                """);

        while (true){
            System.out.print(": ");
            int choice = inputHelper.readInt();
            switch (choice){
                case 1:
                    return Discipline.BUTTERFLY;
                case 2:
                    return Discipline.CRAWL;
                case 3:
                    return Discipline.BACKSTROKE;
                case 4:
                    return Discipline.BREASTSTROKE;
                default:
                    System.out.print("Ugyldigt valg, prøv igen\n: ");
                    break;
            }
        }
    }

    /**
     * Modtager og fortolker en svømmetid fra brugeren.
     * Tid kan indtastes i to formater:
     *   - "SS.mmm"         (kun sekunder, f.eks. 57.31 eller 57.314)
     *   - "MM:SS.mmm"      (minutter og sekunder, f.eks. 1:02.450)
     * Metoden omdanner tiden til millisekunder, som bruges i Result-objekter.
     * Den bliver ved med at spørge indtil formatet er gyldigt.
     * Returnerer tiden i millisekunder.
     */
    public int enterSwimmingTime(){
        System.out.print("Indtast tid (SS.mmm eller MM:SS.mmm)\n: ");

        while (true){
            String timeString = input.nextLine();

            try {
                if (timeString.contains(":")){
                    //MM:SS.mmm
                    String[] parts = timeString.split("[:.]"); // Hvis timeString har ":" så skulle det gerne have 3 dele
                    if(parts.length != 3){
                        throw new IllegalArgumentException();
                    }
                    int minutes = Integer.parseInt(parts[0]);
                    int seconds = Integer.parseInt(parts[1]);
                    int milliSeconds = Integer.parseInt(parts[2]);
                    return (minutes * 60000) + (seconds * 1000) + milliSeconds;
                }
                else { // Hvis timeString IKKE har et ":" så svarer det til et decimaltal
                    // brugeren kan f.eks indtaste "57.3", "57.31", "57.314", "57.3149" hvilket ville give lidt problemer med at splitte det ind i dele og tolke med Integer parse
                    // SS.mm
                    double seconds = Double.parseDouble(timeString);
                    return (int)(seconds * 1000);
                }
            }
            catch (Exception e){
                System.out.print("Ugyldig tid. Prøv igen\n: ");
            }
        }
    }

    /**
     * Beder brugeren indtaste en dato for et resultat (ÅÅÅÅ-MM-DD).
     * Der foretages tre former for validering:
     * 1. **Korrekt datoformat**
     * 2. **Datoen må ikke ligge i fremtiden**
     * 3. **Datoen må ikke være før svømmeren var 6 år gammel**
     * Ved fejl får brugeren en forklaring og bliver bedt om at prøve igen.
     * CompetitiveSwimmer bruges til at beregne yngste gyldige dato
     * Returnerer en gyldig LocalDate
     */
    public LocalDate enterResultDate(CompetitiveSwimmer competitiveSwimmer){
        System.out.print("Indtast dato for resultat (ÅÅÅÅ-MM-DD)\n: ");
        while (true){

            LocalDate earliestByAge = competitiveSwimmer.getBirthDate().plusYears(6);
            LocalDate today = LocalDate.now();

            try {
                String inputDate = input.nextLine();
                LocalDate date = LocalDate.parse(inputDate);

                // Ej i fremtiden
                if(date.isAfter(today)){
                    System.out.println("Dato kan ikke være i fremtiden. Prøv igen.");
                    continue;
                }

                // Ej inden minimum alder 6 år
                if(date.isBefore(earliestByAge)){
                    throw new InvalidResultDateException("Dato er før svømmeren var 6 år gammel.");
                }

                return date;
            }catch (InvalidResultDateException e){
                System.out.println("Fejl: " + e.getMessage());
            }
            catch (Exception e){
                System.out.print("Forkert format, prøv igen (ÅÅÅÅ-MM-DD)\n: ");
            }
        }
    }

    /**
     * Formarter millisekunder til MM:SS.mmm-format
     */
    public String formatTime(int milliSeconds){
        int minutes = milliSeconds / 60000;
        int seconds = (milliSeconds % 60000) / 1000;
        int millis = milliSeconds % 1000;

        return String.format("%d:%02d.%03d", minutes, seconds, millis);
    }
}
