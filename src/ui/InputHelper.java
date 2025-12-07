package ui;

import java.util.Scanner;

/**
 * InputHelper håndterer validering af brugerens input
 *
 * Grunden til at InputHelper eksisterer:
 * - Menu-klassen bliver renere og nemmere at læse
 */
public class InputHelper {
    /** Den fælles Scanner som Menu giver til helperen.*/
    private final Scanner input;

    /**
     * Indlæser en integer fra konsolen.
     * Hvis brugeren taster noget forkert ind, bliver en fejlbesked printet
     * og brugeren bliver bet om at prøve igen indtil et korrekt nummer er indtastet.
     * @param input Scanneren som er oprettet i Menu
     */
    public InputHelper(Scanner input){
        // Vi får en Scanner fra Menu ind i InputHelper
        // så at hele programmet bruger SAMME input,
        // i stedet for at oprette nye Scanner objekt
        this.input = input;
    }

    /**
     * Førsøger at læse et heltal fra brugeren.
     * Hvis brugeren skriver noget der ikke kan omdannes til et tal,
     * bliver der vist en fejlbesked og brugeren bliver bedt om at prøve igen.
     * @return
     */
    public int readInt(){
        while (true){
            try {
                // Læser en linje tekst og førsøger at konvertere den til et tal
                int value = Integer.parseInt(input.nextLine());
                return value;
            }
            catch (NumberFormatException e){
                // Brugeren skrev noget forkert ---> given fejlbesked og prøv igen
                System.out.println("Ugyltigt tal, prøv igen.");
                System.out.print(": ");
            }
        }
    }

}
