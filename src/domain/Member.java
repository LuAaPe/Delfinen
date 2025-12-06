package domain;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class Member {
    private final String phoneNr;
    // classes representing the real world
    private String firstName;
    private String surName;
    private String fullName;
    private final LocalDate birthDate;
    private boolean isActive;
    private boolean isJunior; //instead of String ageGroup
    private boolean isPaid;
    private boolean isCompetitive; // TODO SKAL DETTE BRUGES? KAN DET FJERNES?
    private double yearlyFee;
    //private final int memberID;
    //private static int nextID = 1;
    //private final LocalDate joinDate = LocalDate.now();


    public Member(String firstName, String surName, String phoneNr, LocalDate birthDate, boolean isCompetitive, boolean isActive, boolean isPaid){
        this.firstName = firstName;
        this.surName = surName;
        this.fullName = firstName + " " + surName; // TODO metode, noget :))
        this.phoneNr = phoneNr;
        this.birthDate = birthDate;
        //this.memberID = nextID++;
        this.isActive = isActive;
        this.isCompetitive = isCompetitive;
        this.isPaid = isPaid;

        setAgeGroup();
        setYearlyFee();

    }

    public String getFirstName(){
        return this.firstName;
    }

    public String getSurName(){
        return this.surName;
    }

    public String getFullName(){
        return this.fullName;
    }

    public LocalDate getBirthDate(){
        return this.birthDate;
    }

    public int getAge(){
        return calculateAge();
    }

    public boolean getIsActive(){
        return this.isActive;
    }

    public boolean getIsJunior(){
        return isJunior;
    }

    public boolean getIsCompetitive(){
        return this.isCompetitive;
    }

    public boolean getIsPaid(){
        return this.isPaid;
    }

    public double getYearlyFee(){
        return this.yearlyFee;
    }


    public String getPhoneNr(){
        return this.phoneNr;
    }

    //TODO: check if this actually changes yearlyFee correctly if used
    public void setIsActive(boolean isActive){
        this.isActive = isActive;
        setYearlyFee(); // TODO KAN DENNE FJERNES ????
    }

    public void setIsCompetitive(boolean isCompetitive){
        this.isCompetitive = isCompetitive;
    }

    public void setIsPaid(boolean isPaid){
        this.isPaid = isPaid;
    }

    public int calculateAge(){
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(getBirthDate(), currentDate);
        return period.getYears();
    }

    public void setAgeGroup(){
        if(getAge() >= 18){
            isJunior = false;
        }
        else {
            isJunior = true;
        }
    }

    public void setYearlyFee(){
        if (!getIsActive()){
            this.yearlyFee = 500;
        } else if (getAge() >= 60) {
            this.yearlyFee = (1600*0.75);
        } else if (!getIsJunior()) {
            this.yearlyFee = 1600;
        } else {
            this.yearlyFee = 1000;
        }
    }

    @Override
    public String toString(){
        return fullName + " (" + birthDate + ") " + "- Aktiv: " + isActive + ", Konkurrencesvømmer: " + isCompetitive;
    }

}

