package ui;

import controller.MemberController;
import domain.Member;
import util.MemberNotFoundException;

import java.util.Scanner;

public class TreasurerMenu {

    private final Scanner input;
    private final InputHelper inputHelper;
    private final MemberController memberController;


    public TreasurerMenu(Scanner input, InputHelper inputHelper, MemberController memberController) {
        this.input = input;
        this.inputHelper = inputHelper;
        this.memberController = memberController;
    }

    /**
     * Menu for kassereren
     * - kontingent
     * - restance
     * - registrer betaling
     */
    public void show() {
        boolean run = true;
        while (run) {
            System.out.println("\n   KASSERER-MENU:");
            System.out.println("""
                    ╔══════════════════════════════════╗
                    ║1. Vis samlet forventet kontingent║
                    ╚══════════════════════════════════╝
                    """);
            System.out.println("""
                    ╔═══════════════════════════╗
                    ║2. Vis medlemmer i restance║
                    ╚═══════════════════════════╝
                    """);
            System.out.println("""
                    ╔═════════════════════╗
                    ║3. Registrer betaling║
                    ╚═════════════════════╝
                    """);
            System.out.println("""
                    ╔════════════════════════════╗
                    ║4. Vis et medlems kontingent║
                    ╚════════════════════════════╝
                    """);
            System.out.println("""
                    ╔══════════╗
                    ║0. Tilbage║
                    ╚══════════╝
                    """);
            System.out.print(": ");
            int choice = inputHelper.readInt();

            switch (choice) {
                case 1 -> showTotalFees();
                case 2 -> showMembersInDebt();
                case 3 -> registerPayment();
                case 4 -> checkYearlyFee();
                case 0 -> run = false;
                default -> System.out.println("Ugyldigt Valg");
            }
        }
    }

    /**
     * Beregner det samlede kontingent for alle medlemmer
     */
    private void showTotalFees() {
        memberController.updateYearlyFee();
        double total = memberController.getTotalExpectedFees();
        System.out.println("Samlet forventet kontingent: " + total + " kr.");
    }

    /**
     * Viser alle medlemmer som ikke har betalt kontingent
     */
    private void showMembersInDebt() {
        for (Member m : memberController.getMembersInDebt()) {
            System.out.println(m);
        }
    }

    /**
     * Registerer betaling for et medlem baseret på telefonnummer
     */
    private void registerPayment() {

        System.out.print("Indtast telefon nr. på medlemmet: ");
        String phoneNr = input.nextLine().trim();

        try {
            Member member = memberController.findByPhoneNr(phoneNr);
            memberController.setMemberPaid(phoneNr);
            System.out.println("Betaling registreret på: " + member);

        } catch (MemberNotFoundException e){
            System.out.println(e.getMessage());
        }
    }

    private void checkYearlyFee() {
        System.out.println("Indtast telefonnummer på svømmer (Tast 0 for at gå tilbage)\n: ");
        String phone = input.nextLine();
        if (phone.equals("0")) {
            return;
        }
        try {
            Member member = memberController.findByPhoneNr(phone);
            System.out.println(member.getFullName() + ", Kontingent: " + member.getYearlyFee());
        } catch (MemberNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
