package ui;

import controller.MemberController;
import domain.CompetitiveSwimmer;
import domain.Member;

import java.time.LocalDate;
import java.util.Scanner;

public class ChairmanMenu {

    private final Scanner input;
    private final InputHelper inputHelper;
    private final MemberController memberController;
    private final MemberInput memberInput;

    public ChairmanMenu(Scanner input, InputHelper inputHelper, MemberController memberController, MemberInput memberInput) {
        this.input = input;
        this.inputHelper = inputHelper;
        this.memberController = memberController;
        this.memberInput = memberInput;
    }


    /**
     * Menu for Formand
     * Indeholder medlemsadministration
     */
    public void show() {
        while (true) {
            System.out.println("   FORMAND:");
            System.out.println("""
                    ╔═══════════════╗
                    ║1. SE MEDLEMMER║
                    ╚═══════════════╝
                    """);
            System.out.println("""
                    ╔═══════════════╗
                    ║2. OPRET MEDLEM║
                    ╚═══════════════╝
                    """);
            System.out.println("3. Rediger medlem");
            System.out.println("4. Slet medlem");
            System.out.println("5. Find medlem");
            System.out.println("""
                    ╔═══════════════╗
                    ║6. TILBAGE     ║
                    ╚═══════════════╝
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
                    break;
                case 4:
                    // f.eks slet medlem
                    // f.eks memberList.deleteMember();
                    break;
                case 5:
                    // Vis kontingenter f.eks
                    break;
                case 6:
                    //Tilbage
                    return;
                case 7:
                    // exit
                    input.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Ugyldigt valg");
            }
        }
    }

    private void showAllMembers() {
        System.out.println("\n- - - JUNIOR MEDLEMMER - - -");
        for (Member member : memberController.getAllMembers()){
            if (member.getAge() < 18){
                printMemberLine(member);
            }
        }

        System.out.println("\n- - - SENIOR MEDLEMMER - - -");
        for (Member member : memberController.getAllMembers()){
            if (member.getAge() >= 18){
                printMemberLine(member);
            }
        }
        System.out.println();
    }

    private void printMemberLine(Member member){
        String type = (member instanceof CompetitiveSwimmer) ? "Konkurrensesvømmer" : "Motionist";
        String status = member.getIsActive() ? "Aktiv" : "Passiv";

        System.out.printf("%-20s | Tlf: %-12s | Alder: %-2d | %-11s | %-9s\n",
                member.getFullName(),
                member.getPhoneNr(),
                member.getAge(),
                type,
                status);
    }

    /**
     * Opretter et nyt medlem baseret på brugerinput
     */
    private void createMember() {
        System.out.println("Indtast fornavn\n : ");
        String firstName = input.nextLine();

        System.out.println("Indtast efternavn\n : ");
        String surName = input.nextLine();

        String phoneNr = memberInput.enterPhoneNr();
        LocalDate birthDate = memberInput.enterBirthDate();

        boolean isActive = inputHelper.readYesOrNo("Aktivere medlemskab? j/n\n: ");
        boolean isCompetitiveSwimmer = inputHelper.readYesOrNo("Konkurrencesvømmer? j/n\n: ");
        boolean hasPaid = inputHelper.readYesOrNo("Har medlem betalt? j/n\n: ");

        memberController.addNewMember(firstName, surName, phoneNr, birthDate,
                isCompetitiveSwimmer, isActive, hasPaid);
    }
}

