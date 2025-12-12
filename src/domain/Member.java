package domain;

import java.time.LocalDate;
import java.time.Period;
/**
 * Klassen repræsenterer et almindeligt medlem i svømmeklubben.
 * Medlemmet indeholder basale oplysninger om et medlem:
 * - Navn og telefonnummer (telefonnummer bruges som unik ID)
 * - Fødselsdato og beregnet alder
 * - Aldersgruppe (junior/senior) beregnet ud fra fødselsdato
 * - Om medlemmet er konkurrencesvømmer eller ej
 * - Om medlemmet er akrivt eller passivt
 * - Om medlemmet har betalt kontingent
 * - Den årlige kontingentpris, som beregnes automatisk med setYearlyFee()

 * Konkurrencesvømmere er en særlig type medlem.
 * De representeres af en separat klasse: CompetitiveSwimmer, som arver fra Member.
 */
public class Member {
    /** Medlemmets telefonnummer. Bruges som unikt ID i systemet. */
    private final String phoneNr;
    /** Fornavn.*/
    private final String firstName;
    /** Efternavn*/
    private final String surName;
    /** Fuldt navn = fornavn + efternavn. */
    private final String fullName;
    /** Fødselsdato bruges til at beregne alder og kontingent.*/
    private final LocalDate birthDate;
    /** Aktiv = almindeligt kontingent. Passiv = reduceret kontingent.*/
    private boolean isActive;
    /** Junior = under 18 år. Udregnes automatisk.*/
    private boolean isJunior;
    /** Om medlemmet hat betalt kontingent for året.*/
    private boolean isPaid;
    /** Om medlemmet er konkurrencesvømmer.
     * Bruges af filindlæsning og når medlemmet forfremmes til CompetitiveSwimmer.
     */
    private boolean isCompetitive;
    /** Medlemmets årlige kontingent. Beregnes automatisk ud fra regler.*/
    private double yearlyFee;


    /**
     * Konstruktør til at oprette et nyt medlem.
     */
    public Member(String firstName, String surName, String phoneNr, LocalDate birthDate, boolean isActive, boolean isPaid){
        this.firstName = firstName;
        this.surName = surName;
        this.fullName = firstName + " " + surName;
        this.phoneNr = phoneNr;
        this.birthDate = birthDate;
        this.isActive = isActive;
        this.isCompetitive = false;
        this.isPaid = isPaid;

        // Beregn aldersgruppe og årlig kontingent når medlemmet oprettes
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

    /** Returnerer alder som helt tal.
     * Alderen beregnes hver gange med calculateAge() for at være opdateret
     **/
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


    /**
     * Ændrer aktiv/passiv status.
     * Når status ændres, opdateres kontingent automatisk.
     */
    public void setIsActive(boolean isActive){
        this.isActive = isActive;
        setYearlyFee();
    }

    /**
     * Markerer et medlem som konkurrencesvømmer
     * Bruges når et medlem bliver forfremmet til CompetitiveSwimmer
     */
    public void setIsCompetitive(boolean isCompetitive){
        this.isCompetitive = isCompetitive;
    }

    public void setIsPaid(boolean isPaid){
        this.isPaid = isPaid;
    }

    /**
     * Beregner alder baseret på fødselsdato.
     */
    public int calculateAge(){
        LocalDate currentDate = LocalDate.now();
        // java.time.Period bruges til at finde forskellen mellem nuværende dato og fødselsdato.
        Period period = Period.between(getBirthDate(), currentDate);
        return period.getYears();
    }

    /**
     * Sætter om medlemmet er junior eller senior
     * Junior = under 18 år.
     */
    public void setAgeGroup(){
        isJunior = getAge() < 18;
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

