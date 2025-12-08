package controller;

import data.MemberFileHandler;
import domain.Member;

import java.time.LocalDate;
import java.util.ArrayList;
/**
 * Database-klassen fungerer som programmets "hukommelse".
 *
 * Den indeholder:
 * - En liste over alle medlemmer
 * - Metoder til at oprette, ændre og hente medlemmer
 * - Forbindelse til filsystemet via MemberFileHandler
 *
 * Database læser medlemmer fra Memberlist.txt når programmet starter,
 * og gemmer dem tilbage i filen når det sker ændringer
 *
 * Den her klasse interagerer IKKE med brugeren.
 * Den bruges alene af controllers.
 */
public class Database {

    /** Bruges til at læse og gemme members i tekstfilen. */
    private final MemberFileHandler fileHandler;
    /** Listen med alle Member-objekter som programmet bruger mens det kører.*/
    private final ArrayList<Member> members;

    /**
     * Konstruktør til Database.
     * Når Database oprettes:
     * 1. Indlæs alle medlemmer fra tekstfilen
     * 2. Kontingenter opdateres (i tilfælde af ændrede priser)
     * 3. Alt gemmes igen så filen er opdateret
     */
    public Database(MemberFileHandler fileHandler){
        this.fileHandler = fileHandler;

        // Indlæs medlemmer fra filen via MemberFileHandler
        this.members = fileHandler.loadedMembers();

        // Sørg for at kontingent-priser altid er korrekte ved opstart
        updateYearlyFee();

        // Gem igen hvis priserne blev ændret
        saveMembers();
    }

    /**
     * Opretter et nyt Member-objekt og tilføjer det til listen,
     * samt gemmer hele listen i tekstfilen.
     *
     * Member(. . .) konstruktøren tager alle de nødvendige parametre.
     */
    public void addNewMember(String firstName, String surName, String phoneNr, LocalDate birthDate, boolean isCompetitive, boolean isActive, boolean isPaid){
        try {
            Member member = new Member(firstName, surName, phoneNr, birthDate, isCompetitive, isActive, isPaid);
            members.add(member);
            saveMembers(); // Gem liste med ny medlem
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * Returnerer listen over alle medlemmer.
     * Bruges bl.a. af Formand- og Kasserer-menuerne.
     */
    public ArrayList<Member> getAllMembers(){
        return members;
    }

    /**
     * Gemmer hele listen af medlemmer i tekstfilen.
     * Bruges når:
     * - medlem oprettes
     * - medlem ændres
     * - medlem betaler
     */
    public void saveMembers(){
        fileHandler.saveListOfMembersToFile(members);
    }

    /**
     * Går igennem alle medlemmer og beregner korrekt kontingent.
     * Dette sikrer at priser altid er opdateret ved programstart.
     */
    public void updateYearlyFee() {
        for (Member member : members) {
            member.setYearlyFee();
        }
    }

    /**
     * Beregner samlet forventet kontingent fra alle medlemmer.
     * Bruges af Kasserer-menuen.
     */
    public double getTotalExpectedFees(){
        double sum = 0;
        for (Member m : members){
            sum += m.getYearlyFee();
        }
        return sum;
    }

    /**
     * Returnerer en liste med alle medlemmer, som IKKE har betalt.
     */
    public ArrayList<Member> getMembersInDebt(){
        ArrayList<Member> inDebt = new ArrayList<>();
        for (Member m : members){
            if (!m.getIsPaid()){
                inDebt.add(m);
            }
        }
        return inDebt;
    }

    /**
     * Finder et medlem ud fra telefonnummer.
     */
    public Member findByPhoneNr(String phoneNr){
        for (Member m : members){
            if (m.getPhoneNr().equals(phoneNr)){
                return m;
            }
        }
        return null;
    }

    /**
     * Markerer et medlem som betalr, gemmer ændringen i filen,
     * og returnerer true hvis det lykkes.
     *
     * Bruges i Kasserer-menuen.
     */
    public boolean setMemberPaid(String phoneNr){
        Member m = findByPhoneNr(phoneNr);
        if (m != null) {
            m.setIsPaid(true);
            saveMembers(); // Gem ændringen
            return true;
        }
        return false; // Medlem findes ikke
    }

}
