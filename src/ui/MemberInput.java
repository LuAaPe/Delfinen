package ui;

import controller.MemberController;
import domain.Member;
import util.InvalidBirthYearException;

import java.time.LocalDate;
import java.time.Period;
import java.util.Scanner;


public class MemberInput {

    private final Scanner input;
    private final MemberController memberController;

    public MemberInput(Scanner input, MemberController memberController) {
        this.input = input;
        this.memberController = memberController;
    }


    /**
     * Indlæs og valider fødselsdato
     */
    public LocalDate enterBirthDate() {
        System.out.print("TAST FØDSELSDATO (ÅÅÅÅ-MM-DD)\n: ");
        while (true) {
            try {
                String date = input.nextLine();
                LocalDate birthDate = convertStringDateToLocalDate(date);// prøver på at lave et LocalDate objekt fra den input String
                int age = calculateAge(birthDate); // finder lige ud af alderen....
                if (age < 6 || age > 100) {
                    throw new InvalidBirthYearException("Alder skal være minimum 6 og maks 100.\nVenligst prøv igen (ÅÅÅÅ-MM-DD)\n: ");
                }
                return birthDate;
            } catch (InvalidBirthYearException e) {
                System.out.println(e.getMessage());
            } catch (Exception j) {
                System.out.print("Tastefejl. Venligst prøv igen (ÅÅÅÅ-MM-DD)\n: ");
            }
        }
    }

    /**
     * Indlæs telefonnummer, valider format og tjek om det allerede findes
     * flyttet til memberInput
     */
    public String enterPhoneNr() {
        while (true) {
            System.out.print("Indtast telefon Nr\n: ");
            String phoneNr = input.nextLine().trim();


            if ((phoneNr.length() != 8)) {
                System.out.println("Fejl: Indtast et telefon nr på 8 cifre");
                continue;
            }
            // "Regular expressions"
            if (!phoneNr.matches("\\+?\\d")) {
                System.out.println("Fejl Telefon nr. må kun indeholde tal");
                continue;
            }

            boolean duplicatePhoneNr = false;

            for (Member m : memberController.getAllMembers()) {
                if (m.getPhoneNr().equals(phoneNr)) {
                    duplicatePhoneNr = true;
                    break;
                }
            }
            if (duplicatePhoneNr) {
                System.out.println("Telefon Nr. er allerede registeret på en eksisterende kunde");
                continue;
            }
            return phoneNr;
        }
    }

    /**
     * Konverterer en dato String til LocalDate
     */
    public LocalDate convertStringDateToLocalDate(String date) {
        int birthYear = Integer.parseInt(date.split("-")[0]);
        int birthMonth = Integer.parseInt(date.split("-")[1]);
        int birthDay = Integer.parseInt(date.split("-")[2]);
        return LocalDate.of(birthYear, birthMonth, birthDay);
    }
    /**
     * Beregn alder ud fra fødselsdato
     */
    public int calculateAge(LocalDate date) {
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(date, currentDate);
        return period.getYears();
    }
}
