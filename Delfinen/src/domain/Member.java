package domain;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class Member {
    private String firstName;
    private String surName;
    private String fullName;
    private final LocalDate birthDate;
    private final int memberID;
    private static int nextID = 1; // til senere: kan den ikke starte med 1, når vi får filer og medlemmer som allerede eksisterer med medlemsnummer
    private final LocalDate joinDate = LocalDate.now(); // evt noget med at alle betaler fast kontingnent i januar eller sådan
    private boolean isActive;
    private boolean isCompetitive;
    private boolean hasPaid;
    private double yearlyFee;
    private String ageGroup;
    private int age;


    public Member(String firstName, String surName, LocalDate birthDate) {
        this.firstName = firstName;
        this.surName = surName;
        this.fullName = firstName + " " + surName;
        this.birthDate = birthDate;
        this.memberID = nextID++; //
        this.isActive = true;
        this.isCompetitive = false;
        this.hasPaid = false;
        ageGroup = getAgeGroup();
        age = calculateAge();
        setYearlyFee();
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getAge(){
        return age;
    }

    public int getMemberID() {
        return memberID;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public boolean getIsActive(){
        return isActive;
    }

    public double getYearlyFee(){
        return yearlyFee;
    }

    public int calculateAge() {
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(birthDate, currentDate);
        return period.getYears();
    }

    public String getAgeGroup() {
        return getAge() < 18 ? "Junior" : "Senior"; //risikerer stavefejl, lav evt om til boolean isJunior, eller enum
    }

    public void setYearlyFee() {
        if (!isActive) {
            this.yearlyFee = 500;
        } else if (getAge() >= 60) {
            this.yearlyFee = 1600 * 0.75;
        } else if (getAgeGroup() == "Senior") {
            this.yearlyFee = 1600;
        } else {
            this.yearlyFee = 1000;
        }
    }

    @Override
    public String toString(){
        String status = getIsActive() ? "Aktiv" : "Passiv";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = birthDate.format(formatter);
        return formattedDate+" "+getFullName()+", Alder: "+getAge()+", Hold: "+getAgeGroup()+"\nStatus: "+status+", Kontingent: "+getYearlyFee()+"\n";
    }
}
