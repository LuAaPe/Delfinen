package data;

import domain.*;
import util.MemberNotFoundException;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * ResultFileHandler har ansvaret for al filhåndtering af svømmeresultater.
 * <p>
 * Den fungerer som programmets “database” for resultater, og den:
 * - indlæser alle resultater fra "Results.txt" ved programstart
 * - oversætter tekstlinjer til TrainingResult- eller CompetitionResult-objekter
 * - gemmer alle resultater tilbage i filen når noget ændres.
 * <p>
 * Klassen implementerer TextFileHandler-interfacet, så den kan bruge:
 * - openScanner() til at åbne .txt-filen for læsning
 * - openWriter() til at åbne .txt-filen for skrivning
 * <p>
 * Resultater skrives i formatet:
 * <p>
 * TRÆNING
 * phone,TRÆNING,disciplin,timeMillis,date
 * <p>
 * STÆVNE
 * phone,STÆVNE,disciplin,timeMillis,date,eventName,placement
 */
public class ResultFileHandler implements TextFileHandler {
    /**
     * Navnet på filen hvor alle resultater gemmes.
     */
    private final String fileName;

    /**
     * Opretter en ResultFileHandler med navnet på den fil der skal bruges.
     */
    public ResultFileHandler(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Indlæser ALLE resultater fra filen og tilføjer dem
     * til de rigtige CompetitiveSwimmer-objekter i medlemslisten
     * <p>
     * openScanner() fra TextFileHandler-interfacet bruges til at åbne filen.
     * <p>
     * Arbejdsgang:
     * 1. Åbn resultatfilen med openScanner()
     * 2. Læs linje for linje
     * 3. Split linjen ved komma
     * 4. Find medlem ud fra telefonnummer
     * 5. Tjek om medlemmet er en konkurrencesvømmer
     * 6. Afgør om linjen beskriver TRÆNING eller STÆVNE
     * 7. Opret det korrekte resultatobjekt og tilføj det til svømmeren
     * Der bruges fejl-håndtering pr. linje, så en forkert linje
     * ikke ødelægger hele indlæsningen (robusthed).
     */
    public void loadAllResults(ArrayList<Member> members) {


        // INTERFACE TextFileWriter BRUGES HER!! :))
        try (Scanner scanner = openScanner(fileName)) { // <--- SE HER!! :))

            //Holder styr på hvilken linje vi er nået til
            int lineNumber = 0;
            while (scanner.hasNextLine()) {

                //Tæller for linje
                lineNumber++;
                String line = scanner.nextLine();

                //Springer tommelinjer over
                if (line.isEmpty()) {
                    continue;
                }

                String[] lineData = line.split(",");

                // Minimum antal værdier i træningsresultat er 5
                if (lineData.length < 5) {
                    continue; // spring ugyldige linjer over
                }

                //indre test af hver linje, så hele program ikke fejler ved fejl på enkelt linje
                try {
                    String phone = lineData[0];
                    String type = lineData[1];
                    Discipline discipline = Discipline.valueOf(lineData[2]);
                    int timeMilliSeconds = Integer.parseInt(lineData[3]);
                    LocalDate date = LocalDate.parse(lineData[4]); // TODO har jeg lavet en metode for dette som egentlig er skrald, kan den slettes?? og i stedet for bruge parse i MemberFileHandler

                    // Find medlem ud fra telefonnummer
                    Member member = findByPhoneNr(members, phone);

                    // Resultater kan kun tilføjes til konkurrencesvømmere
                    if (!(member instanceof CompetitiveSwimmer competitiveSwimmer)) {
                        System.out.printf("Ignorerer linje %d i %s: medlem %s findes ikke som konkurrencesvømmer.%n", lineNumber, fileName, phone);
                        continue;
                    }

                    // - - - Træningsresultat:
                    if (type.equals("TRÆNING")) {
                        TrainingResult trainingResult = new TrainingResult(discipline, timeMilliSeconds, date);
                        competitiveSwimmer.addTrainingResult(trainingResult);
                        continue;
                    }

                    // - - - Stævneresultat:
                    // Format: phone,STÆVNE,disciplin,timeMillis,date,eventName,placement
                    if (type.equals("STÆVNE") && lineData.length == 7) {
                        String eventName = lineData[5];
                        int placement = Integer.parseInt(lineData[6]);
                        CompetitionResult competitionResult = new CompetitionResult(discipline, timeMilliSeconds, date, eventName, placement);
                        competitiveSwimmer.addCompetitionResult(competitionResult);
                    }
                } catch (Exception e) {
                    System.out.printf("Kunne ikke indlæse linje %d i %s: %s%n", lineNumber, fileName, e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Der opstod en fejl: " + e.getMessage());
        }
    }

    /**
     * Gemmer alle resultater for alle konkurrencesvømmere i filen.
     * openWriter() fra TextFileHandler-interfacet bruges.
     * Skriver træningsresultater først, derefter stævneresultater.
     */
    public void saveAllResults(ArrayList<Member> members) {

        // INTERFACE TextFileWriter BRUGES HER!!
        try (PrintWriter writer = openWriter(fileName)) { // <---- SE HER!!
            for (Member member : members) {

                // spring ikke-competitive medlemme over
                if (!(member instanceof CompetitiveSwimmer competitiveSwimmer)) {
                    continue;
                }


                // gem træningsresultater
                for (Result result : competitiveSwimmer.getTrainingResults()) {
                    String line = String.format("%s,TRÆNING,%s,%d,%s",
                            member.getPhoneNr(),
                            result.getDiscipline(),
                            result.getTimeMilliSeconds(),
                            result.getDate());

                    writer.println(line);
                }

                // gem resultater for stævner
                for (CompetitionResult result : competitiveSwimmer.getCompetitionResults()) {
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
        } catch (FileNotFoundException e) {
            System.out.println("Filen blev ikke fundet");
        } catch (IOException e) {
            System.out.println("Der upstod en fejl under skrivning til filen");
            e.printStackTrace();
        }
    }

    /**
     * Fjerner alle linjer i resultatfilen for det angivne telefonnummer.
     * Hvis filen ikke findes, gøres intet.
     */
    public void removeResultsForMember(String phone) {
        ArrayList<String> remainingLines = new ArrayList<>();

        try (Scanner scanner = openScanner(fileName)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length > 0 && parts[0].equals(phone)) {
                    continue;
                }

                remainingLines.add(line);
            }
        } catch (FileNotFoundException e) {
            return; // Intet at slette hvis filen ikke findes
        }

        try (PrintWriter writer = openWriter(fileName)) {
            for (String line : remainingLines) {
                writer.println(line);
            }
        } catch (IOException e) {
            System.out.println("Der upstod en fejl under skrivning til filen");
            e.printStackTrace();
        }
    }

    /**
     * Finder et medlem i listen ud fra telefonnummer.
     * Returnerer null hvis intet medlem findes.
     */
    public Member findByPhoneNr(ArrayList<Member> members, String phoneNr) {
        for (Member m : members) {
            if (m.getPhoneNr().equals(phoneNr) || m.getPhoneNr().contains(phoneNr)) {
                return m;
            }
        }
        throw new MemberNotFoundException("Intet medlem med telefonnummer fundet: " + phoneNr);
    }

}
