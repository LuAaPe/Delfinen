package ui;

import java.util.Scanner;

/**
 * InputHelper håndterer validering af brugerens input

 * Grunden til at InputHelper eksisterer:
 * - Menu-klassen bliver renere og nemmere at læse
 */
public class InputHelper {
    /** Den fælles Scanner som Menu giver til helperen.*/
    private final Scanner input;

    /**
     * Opretter et InputHelper-objekt.
     */
    public InputHelper(Scanner input){
        // Vi får en Scanner fra Menu ind i InputHelper
        // så at hele programmet bruger SAMME input,
        // i stedet for at oprette nye Scanner objekt
        this.input = input;
    }

    /**
     * Læser et heltal (int) fra brugeren.
     * Metoden sikrer:
     * - at brugeren ikke kan indtaste bogstaver eller ugyldigt input
     * - at programmet ikke crasher ved forkert input
     * Hvis brugeren skriver noget forkert, vises en fejlbesked,
     * og metoden spørger igen indtil den får et gyldigt tal.
     * Returnerer et valideret heltal indtastet af brugeren
     */
    public int readInt(){
        while (true){
            try {
                // Læser en linje tekst og forsøger at konvertere den til et tal
                return Integer.parseInt(input.nextLine().trim());
            }
            catch (NumberFormatException e){
                // Brugeren skrev noget forkert ---> given fejlbesked og prøv igen
                System.out.println("Ugyldigt tal, prøv igen.");
                System.out.print(": ");
            }
        }
    }

    /**
     * Læser et ja/nej-svar fra brugeren.
     * Acceptable svar:
     *   - "j" = ja
     *   - "n" = nej
     * Alle andre inputs giver en fejlbesked, og metoden spørger igen.
     *
     * @param output Den tekst som brugeren skal se før input (fx "Aktiv? j/n:")
     * @return true hvis brugeren svarer 'j', false hvis 'n'
     */
    public boolean readYesOrNo(String output){
        while (true){
            System.out.println(output);
            String answer = input.nextLine().toLowerCase().trim();
            if (answer.equals("j")){
                return true;
            }
            if (answer.equals("n")){
                return false;
            }
            System.out.println("Skriv j eller n");
        }
    }

}
