package ui;

import controller.MemberController;
import controller.ResultController;
import domain.CompetitiveSwimmer;
import domain.Member;
import util.AlreadyCompetitiveSwimmerException;
import util.MemberNotFoundException;

import java.time.LocalDate;
import java.util.Scanner;

/**
 * ChairmanMenu repræsenterer menuen for klubbens formand.
 * Formanden har ansvar for medlemsadministration:
 * - Se lister over medlemmer (junior/senior)
 * - Oprette nye medlemmer
 * - Redigere eksisterende medlemmer (fx gøre til konkurrencesvømmer)
 * - Fjerne medlemmer
 * - Søge efter medlemmer via telefonnummer
 * Klassen håndterer kun brugerinteraktion og input. Selve logikken
 * håndteres i MemberController og ResultController.
 */
public class ChairmanMenu {

    private final Scanner input;
    private final InputHelper inputHelper;
    private final MemberController memberController;
    private final ResultController resultController;
    private final MemberInput memberInput;

    /**
     * Opretter en ChairmanMenu med de nødvendige afhængigheder.
     */
    public ChairmanMenu(Scanner input, InputHelper inputHelper, MemberController memberController, MemberInput memberInput, ResultController resultController) {
        this.input = input;
        this.inputHelper = inputHelper;
        this.memberController = memberController;
        this.memberInput = memberInput;
        this.resultController = resultController;
    }


    /**
     * Viser formandens hovedmenu og håndterer brugerens valg.
     * Metoden bliver i loop indtil brugeren vælger '0' (tilbage).
     */
    public void show() {
        while (true) {
            System.out.println("\n   FORMAND-MENU:");
            System.out.println("""
                    ╔════════════════╗
                    ║1. Se medlemmer ║
                    ╚════════════════╝
                    """);
            System.out.println("""
                    ╔════════════════╗
                    ║2. Opret medlem ║
                    ╚════════════════╝
                    """);
            System.out.println("""
                    ╔══════════════════╗
                    ║3. Rediger medlem ║
                    ╚══════════════════╝
                    """);
            System.out.println("""
                    ╔═══════════════╗
                    ║4. Slet medlem ║
                    ╚═══════════════╝
                    """);
            System.out.println("""
                    ╔═══════════════╗
                    ║5. Find medlem ║
                    ╚═══════════════╝
                    """);
            System.out.println("""
                    ╔═══════════╗
                    ║0. TILBAGE ║
                    ╚═══════════╝
                    """);

            System.out.print(": ");
            int formandChoice = inputHelper.readInt();
            switch (formandChoice) {
                case 1:
                    showAllMembers();
                    break;
                case 2:
                    // opret medlem
                    createMember();
                    break;
                case 3:
                    // Redigere medlem f.eks
                    promoteMemberToCompetitive();
                    break;
                case 4:
                    // slet medlem
                    removeMember();
                    break;
                case 5:
                    //Viser Oplysninger for et medlem
                    findMember();
                    break;
                case 0:
                    //Tilbage
                    return;
                default:
                    System.out.println("Ugyldigt valg");
            }
        }
    }

    /**
     * Viser en oversigt over alle medlemmer opdelt i juniorer (<18)
     * og seniorer (18+). Udskriver hver medlem via printMemberLine().
     */
    private void showAllMembers() {
        System.out.println("\n----------------------------------------");
        System.out.println("           JUNIOR MEDLEMMER");
        System.out.println("----------------------------------------");

        boolean foundJunior = false;
        for (Member member : memberController.getAllMembers()) {
            if (member.getAge() < 18) {
                printMemberLine(member);
                foundJunior = true;
            }
        }

        if (!foundJunior) {
            System.out.println("Ingen junior medlemmer registreret.");
        }

        boolean foundSenior = false;
        System.out.println("\n----------------------------------------");
        System.out.println("           SENIOR MEDLEMMER");
        System.out.println("----------------------------------------");
        for (Member member : memberController.getAllMembers()) {
            if (member.getAge() >= 18) {
                printMemberLine(member);
                foundSenior = true;
            }
        }
        if (!foundSenior){
            System.out.println("Ingen senior medlemmer registreret.");
        }

        System.out.println("----------------------------------------\n");
    }

    /**
     * Udskriver én linje med et medlems centrale oplysninger i et
     * fast og ensartet format:
     * - Fulde navn
     * - Telefonnummer
     * - Alder
     * - Aktiv/passiv status
     * - Motionist / konkurrencesvømmer
     * Bruges af flere menuvalg for at sikre konsistent output.
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

    /**
     * Opretter et nyt medlem ud fra brugerens input.
     * Indsamler fornavn, efternavn, telefonnummer, fødselsdato
     * samt statusfelter som aktiv/passiv og betalingsstatus.
     * Efter oprettelse hentes medlemmet igen og vises for at bekræfte,
     * at det er korrekt oprettet.
     */
    private void createMember() {
        System.out.println("\n----------------------------------------");
        System.out.println("           OPRET NYT MEDLEM");
        System.out.println("----------------------------------------");
        System.out.print("Fornavn: ");
        String firstName = input.nextLine();

        System.out.print("Efternavn: ");
        String surName = input.nextLine();

        String phoneNr = memberInput.enterPhoneNr();
        LocalDate birthDate = memberInput.enterBirthDate();

        boolean isActive = inputHelper.readYesOrNo("Aktivt medlemskab? (j/n): ");
        boolean isCompetitiveSwimmer = inputHelper.readYesOrNo("Konkurrencesvømmer? (j/n): ");
        boolean hasPaid = inputHelper.readYesOrNo("Har medlemmet betalt? (j/n): ");

        memberController.addNewMember(firstName, surName, phoneNr, birthDate,
                isCompetitiveSwimmer, isActive, hasPaid);
        System.out.println("\n----------------------------------------");
        System.out.println("            MEDLEM OPRETTET");
        System.out.println("----------------------------------------");
        Member created = memberController.findByPhoneNr(phoneNr);
        printMemberLine(created);
        System.out.println("----------------------------------------\n");
    }

    /**
     * Fjerner et medlem baseret på telefonnummer.
     * Udskriver medlemmet der blev fjernet, hvis det findes.
     * Alle resultater knyttet til medlemmet slettes også via ResultController.
     * Kaster MemberNotFoundException hvis nummeret ikke findes.
     */
    private void removeMember() {
        System.out.println("\n----------------------------------------");
        System.out.println("      FJERN MEDLEM FRA KLUBBEN");
        System.out.println("----------------------------------------");

        System.out.print("Telefonnummer: ");
        String phoneNr = input.nextLine();

        try {
            Member removed = memberController.removeMember(phoneNr);
            System.out.println("\n----------------------------------------");
            System.out.println("             MEDLEM FJERNET");
            System.out.println("----------------------------------------");

            printMemberLine(removed);
            resultController.removeResultsForMember(phoneNr);
            System.out.println("----------------------------------------\n");
        } catch (MemberNotFoundException e) {
            System.out.println("\n----------------------------------------");
            System.out.println("           MEDLEM IKKE FUNDET");
            System.out.println("----------------------------------------");
            System.out.println(e.getMessage());
            System.out.println("----------------------------------------\n");
        }
    }

    /**
     * Opgraderer et medlem til konkurrencesvømmer.
     * Hvis medlemmet allerede er konkurrencesvømmer, kastes
     * AlreadyCompetitiveSwimmerException.
     * Hvis medlemmet ikke findes, kastes MemberNotFoundException.
     * Ved succes vises det opgraderede medlem.
     */
    private void promoteMemberToCompetitive() {
        System.out.println("\n----------------------------------------");
        System.out.println("   GØR MEDLEM TIL KONKURRENCESVØMMER");
        System.out.println("----------------------------------------");
        System.out.print("Telefonnummer: ");
        String phoneNr = input.nextLine();

        try {
            CompetitiveSwimmer promoted = memberController.promoteToCompetitive(phoneNr);
            System.out.println("\n----------------------------------------");
            System.out.println("         MEDLEM OPGRADERET");
            System.out.println("----------------------------------------");

            printMemberLine(promoted);

            System.out.println("----------------------------------------\n");
        } catch (AlreadyCompetitiveSwimmerException e) {
            System.out.println("\n----------------------------------------");
            System.out.println("       ALLEREDE KONKURRENCESVØMMER");
            System.out.println("----------------------------------------");
            System.out.println(e.getMessage());
            System.out.println("----------------------------------------\n");
        } catch (MemberNotFoundException e) {
            System.out.println("\n----------------------------------------");
            System.out.println("           MEDLEM IKKE FUNDET");
            System.out.println("----------------------------------------");
            System.out.println(e.getMessage());
            System.out.println("----------------------------------------\n");
        }
    }

    /**
     * Finder og viser et medlem baseret på telefonnummer.
     * Udskriver tydelig fejlmeddelelse hvis medlemmet ikke findes.
     */
    private void findMember() {
        System.out.println("\n----------------------------------------");
        System.out.println("    FIND MEDLEM VIA TELEFONNUMMER");
        System.out.println("----------------------------------------");
        System.out.print("Telefonnummer: ");
        String phoneNr = input.nextLine();

        try {
            Member member = memberController.findByPhoneNr(phoneNr);

            System.out.println("\n----------------------------------------");
            System.out.println("             MEDLEM FUNDET");
            System.out.println("----------------------------------------");
            printMemberLine(member);
            System.out.println("----------------------------------------\n");
        } catch (MemberNotFoundException e) {
            System.out.println("\n----------------------------------------");
            System.out.println("           MEDLEM IKKE FUNDET");
            System.out.println("----------------------------------------");
            System.out.println(e.getMessage());
            System.out.println("----------------------------------------\n");
        }
    }
}

