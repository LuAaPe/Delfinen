package controller;

import domain.Member;

import java.time.LocalDate;
import java.util.ArrayList;
/**
 * Den her controller styrer al logik relateret til at styre medlemmer:
 * - oprette nye medlemmer
 * - finde medlemmer
 * - udskrive lister over medlemmer
 * - opdatere oplysninger om medlemmer
 * - at sørge for at kalde database, når noget skal gemmes
 *
 * Menu-klassen arbejder IKKE direkte med Database
 * Menu --> MemberController --> Database
 */
public class MemberController {
    /** Reference til Database-objektet, hvor alle medlemmer ligger.*/
    Database database;

    /**
     * Konstruktør.
     * Modtager et Database-objekt, som controllern skal arbejde med.
     */
    public MemberController(Database database){
        this.database = database;
    }


    /**
     * Opretter et nyt medlem i systemet.
     *
     * Controllern sender informationen videre til Database,
     * som opretter objektet og gemmer det i filen.
     */
    public void addNewMember(String firstName, String surName, String phoneNr, LocalDate birthDate, boolean isCompetitive, boolean isActive, boolean isPaid){
        database.addNewMember(firstName,surName, phoneNr, birthDate,isCompetitive,isActive,isPaid);
    }

    /**
     * Henter hele listen af medlemmer.
     * Bruges bl.a. i Formand-menuen.
     */
    public ArrayList<Member> getAllMembers(){
        return database.getAllMembers();
    }

    /**
     * Opdaterer kontingent for ALLE medlemmer.
     * Bruges ved programstart og hvis kontingentregler ændres.
     */
    public void updateYearlyFee() {
        database.updateYearlyFee();
    }

    /**
     * Bruges når et medlems oplysninger er ændet.
     *
     * Eksempel:
     * - medlem bliver aktiv efter at være passiv
     * - betalningsstatus ændres
     * - kontingent ændres
     *
     * Controlleren beder Database om at gemme ændringerne i filen.
     */
    public void updateMember(Member member){
        database.saveMembers();
    }

    /**
     * Returnerer det samlede forventede kontingent.
     * Bruges af Kassereren.
     */
    public double getTotalExpectedFees() {
        return database.getTotalExpectedFees();
    }

    /**
     * Returnerer liste over alle medlemmer, som ikke har betalt.
     */
    public ArrayList<Member> getMembersInDebt() {
        return database.getMembersInDebt();
    }

    /**
     * Finder et medlem via telefonnummer.
     * Returnerer null hvis det ikke findes.
     */
    public Member findByPhoneNr(String phoneNr) {
        return database.findByPhoneNr(phoneNr);
    }

    /**
     * Markerer et medlem som betalt.
     * Returnerer true hvis det lykkes, ellers false.
     */
    public boolean setMemberPaid(String phoneNr) {
        return database.setMemberPaid(phoneNr);
    }

}
