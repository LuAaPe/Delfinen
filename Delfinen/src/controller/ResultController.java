package controller;

import data.ResultFileHandler;

public class ResultController {
    // logic
    // Responsibilities:
    // add training results, add competition results, compute top 5

    // TODO: addTrainingResult(member, discipline, time, date)
    // TODO: addCompetitionResult(member, discipline, date, eventName, placement)
    // TODO: getTop5(discipline, junior/senior)

    private ResultFileHandler fileHandler = new ResultFileHandler("Results.txt");
    private Database database = new Database();

    public void saveResults(){
        fileHandler.saveAllResults(database.getAllMembers());
    }

    public void loadResults(){
        fileHandler.loadAllResults(database.getAllMembers());
    }
}
