package ui;

import controller.Klubben;
import controller.MemberController;
import controller.ResultController;
import data.MemberFileHandler;

import java.time.LocalDate;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        InputHelper inputHelper = new InputHelper(input);

        Klubben klubben = new Klubben(new MemberFileHandler("Memberlist.txt"));
        MemberController memberController = new MemberController(klubben);
        ResultController resultController = new ResultController(klubben);
        resultController.loadResults();

        // Input håndtering og behandling
        MemberInput memberInput = new MemberInput(input, inputHelper, memberController);
        ResultInput resultInput = new ResultInput(input, inputHelper);

        // Rolle-menuer
        ChairmanMenu chairmanMenu = new ChairmanMenu(input, inputHelper, memberController, memberInput);
        TreasurerMenu treasurerMenu = new TreasurerMenu(input, inputHelper, memberController);
        TrainerMenu trainerMenu = new TrainerMenu(input, inputHelper, resultInput, resultController, memberController);

        MainMenu mainMenu = new MainMenu(input, inputHelper, chairmanMenu, treasurerMenu, trainerMenu);

        mainMenu.start();

        input.close();
    }
}
