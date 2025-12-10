package data;

import domain.CompetitiveSwimmer;
import domain.Member;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * MemberFileHandler har ansvaret for at:
 * - indlæse alle medlemmer fra tekstfilen "Memberlist.txt"
 * - gemme alle medlemmer tilbage i filen
 * <p>
 * Klassen implementerer TextFileHandler-interfacet,
 * hvilket betyder, at den automatisk arver fælles metoder til:
 * - at åbne en Scanner til læsning
 * - at åbne en PrintWriter til skrivning
 * <p>
 * Formatet i filen:
 * firstName, surName, phoneNr, birthDate, isCompetitive, isActive, isPaid
 */
public class MemberFileHandler implements TextFileHandler {
    /**
     * Navnet på filen hvor medlemmerne gemmes.
     */
    private final String fileName;

    /**
     * Konstruktør, som modtager navnet på den fil
     * der skal læses/skrive medlemmer til.
     *
     * @param fileName navnet på filen hvor medlemmer gemmes
     */
    public MemberFileHandler(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Indlæser alle medlemmer fra tekstfilen og returnerer dem som en ArrayList.
     * Her benyttes openScanner() fra TextFileHandler-interfacet.
     * 1. Åbn filen
     * 2. Læs linje for linje
     * 3. Split linjen i 7 værdier
     * 4. Opret enten Member eller CompetitiveSwimmer
     * 5. Tilføj til listen
     * <p>
     * Hvis filen ikke findes, returneres en tom liste.
     */
    public ArrayList<Member> loadedMembers() {
        ArrayList<Member> loadedMembers = new ArrayList<>();


        // INTERFACE TextHandler BRUGES HER!! SE!! :))
        try (Scanner scanner = openScanner(fileName)) { // <--- her!!){


            // Læs alle linjer i filen
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                // Splitter linjen ved komma
                String[] memberData = line.split(",");

                // Dataformatet kræver 7 værdier
                if (memberData.length == 7) {

                    String firstName = memberData[0];
                    String surName = memberData[1];
                    String phoneNumber = memberData[2];
                    String dateOfBirthString = memberData[3];

                    LocalDate birthDate = convertStringDateToLocalDate(dateOfBirthString);
                    boolean isCompetitive = Boolean.parseBoolean(memberData[4]);
                    boolean isActive = Boolean.parseBoolean(memberData[5]);
                    boolean isPaid = Boolean.parseBoolean(memberData[6]);

                    // Opretter det rigtige medlemstype
                    Member member;
                    if (isCompetitive) {
                        member = new CompetitiveSwimmer(firstName, surName, phoneNumber, birthDate, isActive, isPaid);
                    } else {
                        member = new Member(firstName, surName, phoneNumber, birthDate, isActive, isPaid);
                    }
                    // Tilføjer den samme member i ArrayListen
                    loadedMembers.add(member);
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("Filen blev ikke fundet, opretter en ny fil.");
            return new ArrayList<>();
        }

        return loadedMembers;
    }

    /**
     * Gemmer alle medlemmer i tekstfilen.
     * Benytter openWriter() fra TextFileHandler-interfacet.
     * <p>
     * Hver linje skrives i samme format som ved indlæsning:
     * firstName, surName, phoneNr, birthDate, isCompetitive, isActive, isPaid
     */
    public void saveListOfMembersToFile(ArrayList<Member> members) {

        // INTERFACE TextFileHandler BRUGES HER!!
        try (PrintWriter writer = openWriter(fileName)) {// <--- SE HER!! :))
            for (Member member : members) {
                String memberString = String.format("%s,%s,%s,%s,%b,%b,%b",
                        member.getFirstName(),
                        member.getSurName(),
                        member.getPhoneNr(),
                        member.getBirthDate(),
                        member.getIsCompetitive(),
                        member.getIsActive(),
                        member.getIsPaid());

                writer.println(memberString);
            }

        } catch (FileNotFoundException e) {
            System.out.println("Filen blev ikke fundet");
        } catch (IOException e) {
            System.out.println("Der opstod en fejl under skrivning til filen" + e.getMessage());
        }
    }

    /**
     * Konverterer en dato i tekstformat (ÅÅÅÅ-MM-DD)
     * til et LocalDate objekt.
     */
    private LocalDate convertStringDateToLocalDate(String date) {
        int birthYear = Integer.parseInt(date.split("-")[0]);
        int birthMonth = Integer.parseInt(date.split("-")[1]);
        int birthDay = Integer.parseInt(date.split("-")[2]);
        return LocalDate.of(birthYear, birthMonth, birthDay);
    }
}


