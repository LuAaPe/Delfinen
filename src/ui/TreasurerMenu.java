package ui;

import controller.MemberController;
import domain.CompetitiveSwimmer;
import domain.Member;
import util.MemberNotFoundException;

import java.util.ArrayList;
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
                    ╔═══════════════════════════════════╗
                    ║1. Vis samlet forventet kontingent ║
                    ╚═══════════════════════════════════╝
                    """);
            System.out.println("""
                    ╔════════════════════════════╗
                    ║2. Vis medlemmer i restance ║
                    ╚════════════════════════════╝
                    """);
            System.out.println("""
                    ╔══════════════════════╗
                    ║3. Registrer betaling ║
                    ╚══════════════════════╝
                    """);
            System.out.println("""
                    ╔═════════════════════════════╗
                    ║4. Vis et medlems kontingent ║
                    ╚═════════════════════════════╝
                    """);
            System.out.println("""
                    ╔═══════════╗
                    ║0. Tilbage ║
                    ╚═══════════╝
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

        System.out.println("\n----------------------------------------");
        System.out.println("       SAMLET FORVENTET KONTINGENT");
        System.out.println("----------------------------------------");

        System.out.printf("Total: %.2f kr.\n", total);

        System.out.println("----------------------------------------\n");
    }

    /**
     * Viser alle medlemmer som ikke har betalt kontingent
     */
    private void showMembersInDebt() {
        ArrayList<Member> inDebt = memberController.getMembersInDebt();

        if(inDebt.isEmpty()) {
            System.out.println("\n----------------------------------------");
            System.out.println("      INGEN MEDLEMMER I RESTANCE");
            System.out.println("----------------------------------------\n");
            return;
        }

        System.out.println("\n----------------------------------------");
        System.out.println("         MEDLEMMER I RESTANCE");
        System.out.println("----------------------------------------\n");

        for (Member member: inDebt){
            String status = member.getIsActive() ? "Aktiv" : "Passiv";
            String type = (member instanceof CompetitiveSwimmer) ? "Konkurrence" : "Motionist";

            System.out.printf("%-20s | Tlf: %-8s | Alder: %-3d | %-10s | %-11s\n",
                    member.getFullName(),
                    member.getPhoneNr(),
                    member.getAge(),
                    status,
                    type);
        }

        System.out.println("----------------------------------------\n");
    }

    /**
     * Registerer betaling for et medlem baseret på telefonnummer
     */
    private void registerPayment() {
        System.out.println("\n----------------------------------------");
        System.out.println("           REGISTRER BETALING");
        System.out.println("----------------------------------------");


        System.out.print("Telefonnummer: ");
        String phoneNr = input.nextLine().trim();

        try {
            Member member = memberController.findByPhoneNr(phoneNr);
            memberController.setMemberPaid(phoneNr);
            System.out.println("\n----------------------------------------");
            System.out.println("         BETALING REGISTRERET");
            System.out.println("----------------------------------------");

            printMemberLine(member);
            System.out.println("----------------------------------------\n");

        } catch (MemberNotFoundException e){
            System.out.println("\n----------------------------------------");
            System.out.println("           MEDLEM IKKE FUNDET");
            System.out.println("----------------------------------------");
            System.out.println(e.getMessage());
            System.out.println("----------------------------------------\n");
        }
    }

    private void checkYearlyFee() {
        System.out.println("\n----------------------------------------");
        System.out.println("        TJEK ÅRLIGT KONTINGENT");
        System.out.println("----------------------------------------");
        System.out.print("Telefonnummer (0 for tilbage): ");

        String phone = input.nextLine().trim();
        if (phone.equals("0")) {
            return;
        }
        try {
            Member member = memberController.findByPhoneNr(phone);
            System.out.println(member.getFullName() + ", Kontingent: " + member.getYearlyFee());
        } catch (MemberNotFoundException e) {
            System.out.println("\n----------------------------------------");
            System.out.println("           MEDLEM IKKE FUNDET");
            System.out.println("----------------------------------------");
            System.out.println(e.getMessage());
            System.out.println("----------------------------------------\n");
        }
    }

    /**
     * Udskriver ét medlem i et ensartet format.
     * Bruges i både Chairman- og TreasurerMenu.
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
