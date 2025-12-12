package ui;

import controller.MemberController;
import domain.Member;
import util.InvalidBirthYearException;

import java.time.LocalDate;
import java.time.Period;
import java.util.Scanner;

/**
 * MemberInput er en hjælpeklasse i UI, der håndterer input
 * specifikt til oprettelse og validering af medlemsdata.
 * Formålet er at:
 * - samle al validering ét sted
 * - gøre ChairmanMenu mere overskuelig
 * - sikre ensartet input-format for telefonnumre og fødselsdatoer
 *
 */
public class MemberInput {
    /** Scanner til at læse tekst fra brugeren */
    private final Scanner input;
    /** Controller med adgang til eksisterende medlemmer (bruges til dubbeltjek af telefonnummer) */
    private final MemberController memberController;

    /**
     * Opretter et MemberInput-objekt.
     * @param input Scanner der læser brugerens input
     * @param memberController bruges til at tjekke om et telefonnummer allerede findes
     */
    public MemberInput(Scanner input, MemberController memberController) {
        this.input = input;
        this.memberController = memberController;
    }


    /**
     * Beder brugeren indtaste en fødselsdato og validerer input.
     * Validering består af:
     * 1. Korrekt datoformat (ÅÅÅÅ-MM-DD)
     * 2. Gyldig alder: minimum 6 og maksimum 100 år
     * Metoden bliver ved med at spørge, indtil brugeren indtaster en gyldig dato.
     * Returnerer en LocalDate med brugerens fødselsdato
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
     * Indlæser og validerer et telefonnummer.
     * Validering består af:
     *  1. Nummeret skal være præcis 8 cifre
     *  2. Det må kun indeholde tal
     *  3. Nummeret må ikke være i brug allerede
     * Metoden tjekker alle eksisterende medlemmer gennem MemberController.
     * Den bliver ved indtil et gyldigt og unikt nummer er indtastet.
     * Returnerer et valideret telefonnummer som streng
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
            if (!phoneNr.matches("\\d+")) {
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
     * Konverterer en tekststreng i formatet ÅÅÅÅ-MM-DD til et LocalDate-objekt.
     */
    public LocalDate convertStringDateToLocalDate(String date) {
        int birthYear = Integer.parseInt(date.split("-")[0]);
        int birthMonth = Integer.parseInt(date.split("-")[1]);
        int birthDay = Integer.parseInt(date.split("-")[2]);
        return LocalDate.of(birthYear, birthMonth, birthDay);
    }
    /**
     * Beregner alder ud fra en given fødselsdato.
     * Bruges til at sikre, at nye medlemmer er mellem 6 og 100 år.
     */
    public int calculateAge(LocalDate date) {
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(date, currentDate);
        return period.getYears();
    }
}
