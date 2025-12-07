package controller;

import data.MemberFileHandler;
import data.ResultFileHandler;
import domain.*;
import util.MemberNotFoundException;
import util.NotCompetitiveSwimmerException;

import java.time.LocalDate;

public class ResultController {
    // logic
    // Responsibilities:
    // add training results, add competition results, compute top 5

    // TODO: getTop5(discipline, junior/senior)

    private final ResultFileHandler fileHandler = new ResultFileHandler("Results.txt");
    private final Database database;

    public ResultController(Database database){
        this.database = database;
    }

    // tilføj træningsresultat
    public void addTrainingResult(String phone, Discipline discipline,
                                  int timeMilliSeconds, LocalDate date){
        Member member = database.findByPhoneNr(phone);

        if(member == null) {
            throw new MemberNotFoundException("Telefonnummer findes ikke.");
        }

        if(!(member instanceof CompetitiveSwimmer competitiveSwimmer)){
            throw new NotCompetitiveSwimmerException("Medlem er ikke konkurrencesvømmer.");
        }

        competitiveSwimmer.addTrainingResult(new Result(discipline, timeMilliSeconds, date));
        saveResults();
    }

    // tilføj stævneresultat
    public void addCompetitionResult(String phone, Discipline discipline, int timeMilliSeconds, LocalDate date, String eventName, int placement){
        Member member = database.findByPhoneNr(phone);

        if (member == null){
            throw new MemberNotFoundException("Telefonnummer findes ikke.");
        }

        if(!(member instanceof CompetitiveSwimmer competitiveSwimmer)){
            throw new NotCompetitiveSwimmerException("Medlem er ikke konkurrencesvømmer.");
        }

        competitiveSwimmer.addCompetitionResult(new CompetitionResult(discipline, timeMilliSeconds, date, eventName, placement));
        saveResults();
    }

    public void saveResults(){
        fileHandler.saveAllResults(database.getAllMembers());
    }

    public void loadResults(){
        fileHandler.loadAllResults(database.getAllMembers());
    }

    // henter en konkurrencesvømmer via telefonnummer
    public CompetitiveSwimmer getCompetitiveSwimmer(String phone){
        Member member = database.findByPhoneNr(phone);

        if (member == null){
            throw new MemberNotFoundException("Telefonnummeret findes ikke.");
        }

        if (!(member instanceof CompetitiveSwimmer competitiveSwimmer)){
            throw new NotCompetitiveSwimmerException("Medlem er ikke konkurrencesvømmer.");
        }

        return competitiveSwimmer;
    }
}
