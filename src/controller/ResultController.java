package controller;

import data.MemberFileHandler;
import data.ResultFileHandler;
import domain.*;

import java.time.LocalDate;

public class ResultController {
    // logic
    // Responsibilities:
    // add training results, add competition results, compute top 5

    // TODO: addTrainingResult(member, discipline, time, date)
    // TODO: addCompetitionResult(member, discipline, date, eventName, placement)
    // TODO: getTop5(discipline, junior/senior)
    /*
    private final ResultFileHandler fileHandler = new ResultFileHandler("Results.txt");
    private final Database database;

    public ResultController(Database database){
        this.database = database;
    }

    // tilføj træningsresultat
    public boolean addTrainingResult(String phone, Discipline discipline,
                                     double time, LocalDate date){
        Member member = database.findByPhoneNr(phone);

        if(!(member instanceof CompetitiveSwimmer competitiveSwimmer)){
            return false;
        }

        competitiveSwimmer.addTrainingResult(new Result(discipline, time, date));
        saveResults();
        return true;
    }

    public boolean addCompetitionResult(String phone, Discipline discipline, double time, LocalDate date, String eventName, int placement){
        Member member = database.findByPhoneNr(phone);

        if(!(member instanceof CompetitiveSwimmer competitiveSwimmer)){
            return false;
        }

        competitiveSwimmer.addCompetitionResult(new CompetitionResult(discipline, time, date, eventName, placement));
        saveResults();
        return true;
    }

    public void saveResults(){
        fileHandler.saveAllResults(database.getAllMembers());
    }

    public void loadResults(){
        fileHandler.loadAllResults(database.getAllMembers());
    } */
}
