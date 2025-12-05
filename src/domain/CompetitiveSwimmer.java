package domain;

import java.time.LocalDate;
import java.util.ArrayList;

public class CompetitiveSwimmer extends Member{
    private ArrayList<Result> trainingResults = new ArrayList<>();
    private ArrayList<CompetitionResult> competitionResults = new ArrayList<>();

    public CompetitiveSwimmer(String firstName, String surName, String phoneNumber, LocalDate birthDate, boolean isCompetitive, boolean isActive, boolean isPaid) {
        super(firstName, surName, phoneNumber, birthDate, isCompetitive, isActive, isPaid);
    }

    public void addTrainingResult(Result result){
        trainingResults.add(result);
    }

    public void addCompetitionResult(CompetitionResult result){
        competitionResults.add(result);
    }

    public ArrayList<Result> getTrainingResults() {
        return trainingResults;
    }

    public ArrayList<CompetitionResult> getCompetitionResults(){
        return competitionResults;
    }

}