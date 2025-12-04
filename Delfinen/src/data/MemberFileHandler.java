package data;

import domain.Member;

import java.io.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Scanner;

public class MemberFileHandler {
    // loading/saving
    // Responsibilities:
    // save all members to file, load all members from file,
    private String fileName;

    public MemberFileHandler(String fileName){
        //Navnet på filen vi vil oprette til at gemme medlemmer i
        this.fileName = fileName;
    }

    // Metode som giver os en liste af alle members som indtil videre er gemt i filen med members
    public ArrayList<Member> loadedMembers(){
        ArrayList<Member> loadedMembers = new ArrayList<>();
        try {
            // Åbner filen vi har members i
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);
            // så længe som det er noget tekst (hasNextLine) så looper denne while-loop:
            while (scanner.hasNextLine()){
                //Henter indholdet i linjen (dvs: fornavn, efternavn, 1979-03-13. . . . etc)
                String line = scanner.nextLine();
                // splitter indholdet, og lægger det i en liste
                String[] memberData = line.split(",");
                // listen skulle gerne være 6 stk elementer lang (vi ændrer dette senere)
                if(memberData.length == 6){
                    // Regular member constructor: Member(String firstName, String surName, LocalDate birthDate, boolean isCompetitive, boolean isActive, boolean isPaid)
                    String firstName = memberData[0];
                    String surName = memberData[1];
                    String dateOfBirthString = memberData[2];

                    LocalDate birthDate = convertStringDateToLocalDate(dateOfBirthString);
                    Boolean isCompetitive = Boolean.parseBoolean(memberData[3]);
                    Boolean isActive = Boolean.parseBoolean(memberData[4]);
                    Boolean isPaid = Boolean.parseBoolean(memberData[5]);

                    // Opretter så et Member objekt
                    Member member = new Member(firstName, surName, birthDate, isCompetitive, isActive, isPaid);
                    // Tilføjer den samme member i ArrayListen
                    loadedMembers.add(member);

                }

            }

        }
        catch (FileNotFoundException e){
            System.out.println("Filen blev ikke fundet, opretter en ny fil.");
            return new ArrayList<>();
        }
        catch (IOException e){
            System.out.println("Der opstod en fejl.");
            e.printStackTrace();
        }

        return loadedMembers;
    }

    public void saveListOfMembersToFile(ArrayList<Member> members){
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(fileName));
            for (Member member : members){
                String memberString = String.format("%s,%s,%s,%b,%b,%b",
                        member.getFirstName(),
                        member.getSurName(),
                        member.getBirthDate(),
                        member.getIsCompetitive(),
                        member.getIsActive(),
                        member.getIsPaid());

                writer.println(memberString);
            }
            writer.close();
        }
        catch (FileNotFoundException e){
            System.out.println("Filen blev ikke fundet");
        }
        catch (IOException e){
            System.out.println("Der upstod en fejl under skrivning til filen");
            e.printStackTrace();
        }
        finally {

        }
    }

    public void writeToFile(Member member){
        String pathname = ""; // ?
        // member constructor: Member(String firstName, String surName, LocalDate birthDate, boolean isCompetitive, boolean isActive, boolean isPaid)
        String memberString = String.format("%s,%s,%s,%b,%b,%b",
                member.getFirstName(),
                member.getSurName(),
                member.getBirthDate(),
                member.getIsCompetitive(),
                member.getIsActive(),
                member.getIsPaid());
        try {
            PrintWriter writer = new PrintWriter(new FileWriter("TuesDayTest.txt"));
            writer.println(memberString);
            writer.close();
        }
        catch (FileNotFoundException e){
            System.out.println("Filen blev ikke fundet");
        }
        catch (IOException e){
            System.out.println("Der upstod en fejl under skrivning til filen");
            e.printStackTrace();
        }
    }

    private LocalDate convertStringDateToLocalDate(String date){
        int birthYear = Integer.parseInt(date.split("-")[0]);
        int birthMonth = Integer.parseInt(date.split("-")[1]);
        int birthDay = Integer.parseInt(date.split("-")[2]);
        return LocalDate.of(birthYear, birthMonth, birthDay);
    }
}


