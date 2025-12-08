package domain;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

/**
 * Klassen repræsenterer et almindeligt medlem i svømmeklubben.
 *
 * Medlemmet indeholder:
 * - Personlige oplysninger (navn, tlf.nr., fødselsdato)
 * - Medlemsstatus (aktiv/passiv, betalt/ikke betalt)
 * - Aldersgruppe (junior/senior) beregnet ud fra fødselsdato
 * - Om medlemmet er konkurrencesvømmer eller ej
 * - Den årlige kontingentpris, som beregnes automatisk
 *
 * Bemærk:
 * isCompetitive bruges af fil-indlæsningen for at afgøre,
 * om medlemmer fra filen skal oprettes som CompetitiveSwimmer objekter.
 */
public class Member {
    /** Medlemmets telefonnummer. Bruges som unikt ID i systemet. */
    private final String phoneNr;
    /** Fornavn.*/
    private String firstName;
    /** Efternavn*/
    private String surName;
    /** Fuldtnavn = fornavn + efternavn. */
    private String fullName;
    /** Fødselsdato bruges til at beregne alder og kontingent.*/
    private final LocalDate birthDate;
    /** Aktiv = alminderligt kontingent. Passiv = reduceret kontingent.*/
    private boolean isActive;
    /** Junior = under 18 år. Udregnes automatisk.*/
    private boolean isJunior;
    /** Om medlemmet hat betalt kontingent for året.*/
    private boolean isPaid;
    /** Om medlemmet er konkurrencesvømmer.
     * Dette er især vigtigt ved indlæsning fra fil.
     */
    private boolean isCompetitive;
    /** Medlemmets årlige kontingent. Beregnes automatisk ud fra regler.*/
    private double yearlyFee;


    /**
     * Konstruktør til at oprette et nyt medlem.
     *
     * @param firstName     Fornavn
     * @param surName       Efternavn
     * @param phoneNr       Telefonnummer (unik)
     * @param birthDate     Fødselsdato
     * @param isCompetitive Om medlemmet er konkurrencesvømmer
     * @param isActive      Om medlemmet er aktivt/passivt medlem
     * @param isPaid        Om medlemmet hat betalt kontingent
     */
    public Member(String firstName, String surName, String phoneNr, LocalDate birthDate, boolean isCompetitive, boolean isActive, boolean isPaid){
        this.firstName = firstName;
        this.surName = surName;
        this.fullName = firstName + " " + surName; // TODO metode, noget :))
        this.phoneNr = phoneNr;
        this.birthDate = birthDate;
        this.isActive = isActive;
        this.isCompetitive = isCompetitive;
        this.isPaid = isPaid;

        // Beregn aldersgruppe og årlig kontingent når medlemmet oprettes
        setAgeGroup();
        setYearlyFee();

    }

    // - - - GETTERS - - - //
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

    /** Returnerer alder som helt tal.*/
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

    // - - - SETTERS - - - //

    /**
     * Ændrer aktiv/passiv status.
     * Når status ændres, skal kontingentprisen opdateres.
     * @param isActive      true/false om medlemmen skal være aktiv eller ej
     */
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
    // - - - BEREGNINGER OG LOGIK - - - //

    /**
     * Beregner alder baseret på fødselsdato.
     */
    public int calculateAge(){
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(getBirthDate(), currentDate);
        return period.getYears();
    }

    /**
     * Sætter om medlemmet er junior eller senior
     * Junior = under 18 år.
     */
    public void setAgeGroup(){
        if(getAge() >= 18){
            isJunior = false;
        }
        else {
            isJunior = true;
        }
    }


    /**
     * Beregner det årlige kontingent ud fra klubbens regler:
     * - Passiv medlem: 500 kr
     * - Senior (60+): 25% rabat på voksenpris
     * - Voksen (18-59): 1600 kr
     * - (under 18): 1000 kr
     */
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

    /** Tekst-representation af medlemsobjektet,
     * Bruges i menuer og lister.
     */
    @Override
    public String toString(){
        return fullName + " (" + birthDate + ") " + "- Aktiv: " + isActive + ", Konkurrencesvømmer: " + isCompetitive;
    }

}

