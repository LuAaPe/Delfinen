package ui;

import java.util.Scanner;

public class MainMenu {

    private final Scanner input;
    private final InputHelper inputHelper;
    private final ChairmanMenu chairmanMenu;
    private final TreasurerMenu treasurerMenu;
    private final TrainerMenu trainerMenu;

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

    public void start() {
        while (true) {
            System.out.println("   NAVIGATIONS-MENU:");
            System.out.println("""
                    +-+\s
                    |1|\s FORMAND
                    +-+\s
                    """);
            System.out.println("""
                    +-+\s
                    |2|\s KASSERER
                    +-+\s
                    """);
            System.out.println("""
                    +-+\s
                    |3|\s TRÆNER
                    +-+\s
                    """);
            System.out.println("""
                    +-+\s
                    |4|\s AFSLUT
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
                    input.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("ugyldigt valg");
            }

        }
    }
}

