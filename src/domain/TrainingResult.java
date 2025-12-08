package domain;

import java.time.LocalDate;

public class TrainingResult extends Result{

    public TrainingResult(Discipline discipline, int timeMilliSeconds, LocalDate date){
        super(discipline,timeMilliSeconds,date);
    }
}
