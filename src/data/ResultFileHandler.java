package data;

import domain.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class ResultFileHandler {
    // loading/saving
    // Responsibilities:
    // save results per swimmer or colletively

    private final String fileName;

    public ResultFileHandler(String fileName){
        this.fileName = fileName;
    }

    // Henter alle resultater
    public void loadAllResults(ArrayList<Member> members){
        File file = new File(fileName);
        if(!file.exists()){
            return;
        }

        try {
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] lineData = line.split(",");

                if (lineData.length < 5 ){
                    continue; //TODO tjek om det ikke også kan skrives sådan her i loadedMembers i MemberFileHandler
                }

                String phone = lineData[0];
                String type = lineData[1];
                Discipline discipline = Discipline.valueOf(lineData[2]);
                int timeMilliSeconds = Integer.parseInt(lineData[3]);
                LocalDate date = LocalDate.parse(lineData[4]); // TODO har jeg lavet en metode for dette som egentlig er skrald, kan den slettes?? og i stedet for bruge parse i MemberFileHandler

                Member member = findByPhoneNr(members, phone);

                if (!(member instanceof CompetitiveSwimmer competitiveSwimmer)){
                    continue;
                }

                if (type.equals("TRÆNING")){
                    Result result = new Result(discipline, timeMilliSeconds, date);
                    competitiveSwimmer.addTrainingResult(result);
                }

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

    //Gemmer alle resultater til fil
    public void saveAllResults(ArrayList<Member> members){
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(fileName));
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

                // gem restltater for stævner
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

    public Member findByPhoneNr(ArrayList<Member> members, String phoneNr){
        for (Member m : members){
            if (m.getPhoneNr().equals(phoneNr)){
                return m;
            }
        }
        return null;
    }

}
