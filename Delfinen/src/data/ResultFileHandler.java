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

    // TODO load each swimmers results, load them back on program start
    private final String fileName;

    public ResultFileHandler(String fileName){
        this.fileName = fileName;
    }

    // Henter alle resultater
    public void loadAllResults(ArrayList<Member> members){
        File file = new File(fileName);

        try {
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                String[] lineData = line.split(",");

                if (lineData.length < 6 ){
                    continue; //TODO tjek om det ikke også kan skrives sådan her i loadedMembers i MemberFileHandler
                }

                String phone = lineData[0];
                String type = lineData[1];
                Discipline discipline = Discipline.valueOf(lineData[2]);
                double time = Double.parseDouble(lineData[3]);
                LocalDate date = LocalDate.parse(lineData[4]); // TODO har jeg lavet en metode for dette som egentlig er skrald, kan den slettes?? og i stedet for bruge parse i MemberFileHandler

                Member member = findByPhoneNr(members, phone);

                if (!(member instanceof CompetitiveSwimmer competitiveSwimmer)){
                    continue;
                }

                if (type.equals("TRÆNING")){
                    Result result = new Result(discipline, time, date);
                    competitiveSwimmer.addTrainingResult(result);
                }

                if(type.equals("STÆVNE")){
                    String eventName = lineData[5];
                    int placement = Integer.parseInt(lineData[6]);
                    CompetitionResult competitionResult = new CompetitionResult(discipline,time, date,eventName, placement);
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


                // gem træningsresultater //TODO fix formatet
                for (Result result : competitiveSwimmer.getTrainingResults()){
                    writer.println(member.getPhoneNr()+
                            ",TRÆNING," +
                            result.getDiscipline()+","+
                            result.getTime()+ ","+
                            result.getDate());
                }
                // TODO FIX FORMATET
                for (CompetitionResult result : competitiveSwimmer.getCompetitionResults()){
                    writer.println(member.getPhoneNr()+
                            ",STÆVNE,"+
                            result.getDiscipline()+","+
                            result.getTime()+","+
                            result.getDate()+","+
                            result.getEventName()+","+
                            result.getPlacement()
                            );
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
