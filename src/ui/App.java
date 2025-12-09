package ui;

import controller.Database;
import controller.MemberController;
import controller.ResultController;
import data.MemberFileHandler;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        InputHelper inputHelper = new InputHelper(input);

        Database database = new Database(new MemberFileHandler("Memberlist.txt"));
        MemberController memberController = new MemberController(database);
        ResultController resultController = new ResultController(database);
        resultController.loadResults();

        // Input-services
        MemberInput memberInput = new MemberInput(input, inputHelper, memberController);
        ResultInput resultInput = new ResultInput(input, inputHelper);

        // Rolle-menyer
        ChairmanMenu chairmanMenu = new ChairmanMenu(input, inputHelper, memberController, memberInput);
        TreasurerMenu treasurerMenu = new TreasurerMenu(input, inputHelper, memberController);
        TrainerMenu trainerMenu = new TrainerMenu(input, inputHelper, resultInput, resultController, memberController);

        MainMenu mainMenu = new MainMenu(input, inputHelper, chairmanMenu, treasurerMenu, trainerMenu);

        mainMenu.start();

        input.close();
    }
}
