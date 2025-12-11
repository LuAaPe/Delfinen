package ui;

import domain.CompetitiveSwimmer;
import domain.Discipline;
import util.InvalidResultDateException;

import java.time.LocalDate;
import java.util.Scanner;

public class ResultInput {

    private final Scanner input;
    private final InputHelper inputHelper;


    public ResultInput(Scanner input, InputHelper inputHelper) {
        this.input = input;
        this.inputHelper = inputHelper;
    }


    /**
     * Menu for at vælge en disciplin
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
     * Metode til at indtaste en tid på formatet "SS.mm" eller "MM:SS.mmm

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
     * Validerer datoen for et resultat ud fra svømmerens alder
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
