package ui;

import java.util.Scanner;

/**
 * MainMenu er programmets øverste navigationsmenu.
 * Denne menu giver adgang til tre roller:
 * 1. Formandens menu
 * 2. Kassererens menu
 * 3. Trænerens menu
 * Menuen fungerer som et "hub", hvorfra brugeren kan vælge,
 * hvilken del af systemet vedkommende vil arbejde i.
 * MainMenu er en ren UI. Den indeholder:
 *   - ingen logik
 *   - ingen datahåndtering
 * Den sender blot brugeren videre til de respektive menuer.
 */
public class MainMenu {
    /** Scanner-objekt til at læse brugerinput fra tastaturet */
    private final Scanner input;
    /** Hjælpeklasse til valideret input (fx talinput) */
    private final InputHelper inputHelper;
    /** Formandens menu – bruges når brugeren vælger 1 */
    private final ChairmanMenu chairmanMenu;
    /** Kassererens menu – bruges når brugeren vælger 2 */
    private final TreasurerMenu treasurerMenu;
    /** Trænerens menu – bruges når brugeren vælger 3 */
    private final TrainerMenu trainerMenu;

    /**
     * Opretter hovedmenuen og binder alle under-menuerne sammen.
     * MainMenu modtager alle sine "værktøjer" udefra.
     * Det gør det nemt at teste og udskifte dele af programmet.
     */
    public MainMenu(Scanner input, InputHelper inputHelper,
                    ChairmanMenu chairmanMenu,
                    TreasurerMenu treasurerMenu,
                    TrainerMenu trainerMenu) {
        this.input = input;
        this.inputHelper = inputHelper;
        this.chairmanMenu = chairmanMenu;
        this.treasurerMenu = treasurerMenu;
        this.trainerMenu = trainerMenu;
    }

    /**
     * Starter hovedmenuens loop.
     * Menuen kører i en uendelig løkke, indtil brugeren vælger "4 – Afslut".
     * Brugerens input bliver læst med inputHelper.readInt(),
     * som sikrer, at ugyldige input ikke crasher programmet.
     * Ud fra brugerens valg sendes vedkommende videre til:
     * - Formandens menu
     * - Kassererens menu
     * - Trænerens menu
     * Hvis brugeren vælger at afslutte programmet:
     * - vises en ASCII-delfin (for sjov og afslutning)
     * - Scanner lukkes
     * - programmet afsluttes med System.exit(0)
     */
    public void start() {
        while (true) {
            System.out.println("\n   NAVIGATIONS-MENU:");
            System.out.println("""
                    +-+\s
                    |1|  FORMAND
                    +-+\s
                    """);
            System.out.println("""
                    +-+\s
                    |2|  KASSERER
                    +-+\s
                    """);
            System.out.println("""
                    +-+\s
                    |3|  TRÆNER
                    +-+\s
                    """);
            System.out.println("""
                    +-+\s
                    |4|  AFSLUT
                    +-+\s
                    """);

            System.out.print(": ");
            int choice = inputHelper.readInt(); // Læser et tal sikkert

            switch (choice) {
                case 1:
                    chairmanMenu.show(); // menu for formanden
                    break;
                case 2:
                    treasurerMenu.show(); // menu for kassereren
                    break;
                case 3:
                    trainerMenu.show(); // menu for træner
                    break;
                case 4:
                    // afslut programmet
                    LookItsADolphin.printDolphinArt();
                    input.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("ugyldigt valg");
            }

        }
    }
}

