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
    // start with simple csv file like name;age;active;competitive;disciplines;fee;debt
    // TODO: save all members to one text file and load again on startup
    private String fileName;

    public MemberFileHandler(String fileName){
        this.fileName = fileName;
    }

    // Method that takes the file we store members in, and loads them into an ArrayList<Member>
    public ArrayList<Member> loadedMembers(){
        ArrayList<Member> loadedMembers = new ArrayList<>();
        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                // add a  if line.isEmpty() continue here?
                String[] memberData = line.split(",");

                if(memberData.length == 6){ //regular member
                    // Regular member constructor: Member(String firstName, String surName, LocalDate birthDate, boolean isCompetitive, boolean isActive, boolean isPaid)
                    String firstName = memberData[0];
                    String surName = memberData[1];
                    String dateOfBirthString = memberData[2];
                    LocalDate birthDate = convertStringDateToLocalDate(dateOfBirthString);
                    Boolean isCompetitive = Boolean.parseBoolean(memberData[3]);
                    Boolean isActive = Boolean.parseBoolean(memberData[4]);
                    Boolean isPaid = Boolean.parseBoolean(memberData[5]);

                    //TODO: create member from above data
                    Member member = new Member(firstName, surName, birthDate, isCompetitive, isActive, isPaid);
                    //TODO: add member into this method's output array
                    loadedMembers.add(member);
                }

            }

        }
        catch (FileNotFoundException e){
            System.out.println("Filen blev ikke fundet");
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


