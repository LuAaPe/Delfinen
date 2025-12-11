package ui;

import controller.MemberController;
import controller.ResultController;
import domain.CompetitiveSwimmer;
import domain.Member;
import util.AlreadyCompetitiveSwimmerException;
import util.MemberNotFoundException;

import java.time.LocalDate;
import java.util.Scanner;

public class ChairmanMenu {

    private final Scanner input;
    private final InputHelper inputHelper;
    private final MemberController memberController;
    private final ResultController resultController;
    private final MemberInput memberInput;

    public ChairmanMenu(Scanner input, InputHelper inputHelper, MemberController memberController, MemberInput memberInput, ResultController resultController) {
        this.input = input;
        this.inputHelper = inputHelper;
        this.memberController = memberController;
        this.memberInput = memberInput;
        this.resultController = resultController;
    }


    /**
     * Menu for Formand
     * Indeholder medlemsadministration
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
     * Opretter et nyt medlem baseret på brugerinput
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

