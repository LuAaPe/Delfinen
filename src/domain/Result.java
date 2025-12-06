package domain;

import java.time.LocalDate;

public class Result {
    private final Discipline discipline;
    private final int timeMilliSeconds;
    private final LocalDate date;
    Member member;

    public Result(Discipline discipline, int timeMilliSeconds, LocalDate date){
        this.discipline = discipline;
        this.timeMilliSeconds = timeMilliSeconds;
        this.date = date;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public int getTimeMilliSeconds() {
        return timeMilliSeconds;
    }

    public LocalDate getDate() {
        return date;
    }

    // Metode som hjælper med at formatere resultattiden
    public String getFormattedTime(){
        int totalMilliSeconds = timeMilliSeconds;
        int minutes = totalMilliSeconds / 60000;
        int seconds = (totalMilliSeconds % 60000) / 1000;
        int milliSeconds = totalMilliSeconds % 1000;

        return String.format("%d:%02d.%03d", minutes, seconds, milliSeconds);
    }
}



