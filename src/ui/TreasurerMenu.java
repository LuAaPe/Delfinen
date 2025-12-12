package ui;

import controller.MemberController;
import domain.CompetitiveSwimmer;
import domain.Member;
import util.MemberNotFoundException;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * TreasurerMenu er den menu som kassereren bruger.
 * Menuen giver mulighed for at:
 * 1. Se det samlede forventede kontingent for alle medlemmer
 * 2. Se alle medlemmer der er i restance (ikke betalt)
 * 3. Registrere betaling for et medlem
 * 4. Se det årlige kontingent for et enkelt medlem
 */
public class TreasurerMenu {
    /** Scanner til brugerinput fra konsollen */
    private final Scanner input;
    /** Hjælpeklasse til valideret input (fx tal, ja/nej) */
    private final InputHelper inputHelper;
    /** Controllerns adgang til alle medlemsfunktioner */
    private final MemberController memberController;

    /**
     * Opretter kasserer-menuen.
     * UI får alle sine "værktøjer" gennem konstruktøren.
     */
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
     * Menuen kører i en løkke indtil brugeren vælger 0 (tilbage).
     * Hver mulighed sender brugeren videre til en separat metode.
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
     * Udregner og viser det samlede forventede kontingent for hele klubben.
     * Først opdateres kontingentet for ALLE medlemmer (skulle priser ændre sig),
     * og derefter hentes summen fra MemberController.
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
     * Viser alle medlemmer som ikke har betalt kontingent.
     * Listen hentes fra MemberController,
     * som selv udvælger de medlemmer hvor isPaid = false.
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
     * Registerer betaling for et medlem baseret på telefonnummer.
     * Arbejdsgang:
     * 1. Brugeren indtaster telefonnummer
     * 2. Controlleren forsøger at finde medlemmet
     * 3. Hvis medlemmet findes → isPaid sættes til true
     * 4. Resultatet udskrives i et pænt format
     * Hvis medlemmet ikke findes, bliver brugeren informeret.
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

    /**
     * Viser det årlige kontingent for ét bestemt medlem.
     * Brugeren indtaster telefonnummer, og controlleren slår medlemmet op.
     * Hvis medlemmet findes, vises det aktuelle kontingent (yearlyFee).
     */
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
