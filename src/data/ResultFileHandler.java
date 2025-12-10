package data;

import domain.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * ResultFileHandler er ansvarlig al filhåndtering relateret til resultater.

 * Den:
 * - indlæser alle resultater fra "Results.txt"
 * - gemmer alle resultater tilbage i filen.

 * Klassen implementerer TextFileHandler-interfacet, så den kan bruge:
 * - openScanner() til at åbne .txt-filen for læsning
 * - openWriter() til at åbne .txt-filen for skrivning

 * Resultater skrives i formatet:

 * TRÆNING
 *      phone,TRÆNING,disciplin,timeMillis,date

 * STÆVNE
 *      phone,STÆVNE,disciplin,timeMillis,date,eventName,placement
 *
 */
public class ResultFileHandler implements TextFileHandler {
    /** Navnet på filen hvor alle resultater gemmes.*/
    private final String fileName;

    /**
     * Konstruktør der modtager navnet på resultatfilen.
     */
    public ResultFileHandler(String fileName){
        this.fileName = fileName;
    }

    /**
     * Indlæser ALLE resultater fra filen og tilføjer dem
     * til de rigtige CompetitiveSwimmer-objekter i medlemslisten

     * openScanner() fra TextFileHandler-interfacet bruges til at åbne filen.

     * 1. Læs hver linje
     * 2. Split den ved komma
     * 3. Find rigtigt medlem via telefonnummer
     * 4. Tjek om medlem er konkurrencesvømmer
     * 5. Opret resultat og tilføj til medlem
     */
    public void loadAllResults(ArrayList<Member> members){

        try {
            // INTERFACE TextFileWriter BRUGES HER!! :))
            Scanner scanner = openScanner(fileName); // <--- SE HER!! :))

            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] lineData = line.split(",");

                // Minimum antal værdier i træningsresultat er 5
                if (lineData.length < 5 ){
                    continue; // spring ugyldige linjer over
                }
                String phone = lineData[0];
                String type = lineData[1];
                Discipline discipline = Discipline.valueOf(lineData[2]);
                int timeMilliSeconds = Integer.parseInt(lineData[3]);
                LocalDate date = LocalDate.parse(lineData[4]); // TODO har jeg lavet en metode for dette som egentlig er skrald, kan den slettes?? og i stedet for bruge parse i MemberFileHandler

                // Find medlem ud fra telefonnummer
                Member member = findByPhoneNr(members, phone);

                // Resultater kan kun tilføjes til konkurrencesvømmere
                if (!(member instanceof CompetitiveSwimmer competitiveSwimmer)){
                    continue;
                }

                // - - - Træningsresultat:
                if (type.equals("TRÆNING")){
                    TrainingResult trainingResult = new TrainingResult(discipline, timeMilliSeconds, date);
                    competitiveSwimmer.addTrainingResult(trainingResult);
                }

                // - - - Stævneresultat:
                // Format: phone,STÆVNE,disciplin,timeMillis,date,eventName,placement
                if(type.equals("STÆVNE") && lineData.length == 7){
                    String eventName = lineData[5];
                    int placement = Integer.parseInt(lineData[6]);
                    CompetitionResult competitionResult = new CompetitionResult(discipline,timeMilliSeconds, date,eventName, placement);
                    competitiveSwimmer.addCompetitionResult(competitionResult);
                }
            }
        }
        catch (IOException e){
            System.out.println("Der opstod en fejl.");
            e.printStackTrace();
        }
    }

    /**
     * Gemmer alle resultater for alle konkurrencesvømmere i filen.
     * openWriter() fra TextFileHandler-interfacet bruges.
     * Skriver træningsresultater først, derefter stævneresultater.
     * @param members
     */
    public void saveAllResults(ArrayList<Member> members){
        try {
            // INTERFACE TextFileWriter BRUGES HER!!
            PrintWriter writer = openWriter(fileName); // <---- SE HER!!
            for (Member member : members){

                // spring ikke-competitive medlemme over
                if (!(member instanceof CompetitiveSwimmer competitiveSwimmer)) {
                    continue;
                }


                // gem træningsresultater
                for (Result result : competitiveSwimmer.getTrainingResults()){
                    String line = String.format("%s,TRÆNING,%s,%d,%s",
                            member.getPhoneNr(),
                            result.getDiscipline(),
                            result.getTimeMilliSeconds(),
                            result.getDate());

                    writer.println(line);
                }

                // gem resultater for stævner
                for (CompetitionResult result : competitiveSwimmer.getCompetitionResults()){
                    String line = String.format("%s,STÆVNE,%s,%d,%s,%s,%d",
                            member.getPhoneNr(),
                            result.getDiscipline(),
                            result.getTimeMilliSeconds(),
                            result.getDate(),
                            result.getEventName(),
                            result.getPlacement()
                    );
                    writer.println(line);
                }


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
    }

    /**
     * Finder et medlem i listen ud fra telefonnummer.
     * Returnerer null hvis intet medlem findes.
     */
    public Member findByPhoneNr(ArrayList<Member> members, String phoneNr){
        for (Member m : members){
            if (m.getPhoneNr().equals(phoneNr)){
                return m;
            }
        }
        return null;
    }

}
