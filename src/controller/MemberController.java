package controller;

import domain.CompetitiveSwimmer;
import domain.Member;


import java.time.LocalDate;
import java.util.ArrayList;
/**
 * Den her controller har al logik relateret til at styre medlemmer:
 * - oprette nye medlemmer
 * - finde medlemmer
 * - udskrive lister over medlemmer
 * - opdatere oplysninger om medlemmer
 * - at sørge for at kalde database, når noget skal gemmes

 * Menu-klassen arbejder IKKE direkte med Database
 * Menu --> MemberController --> Database
 */
public class MemberController {
    /** Reference til Database-objektet, hvor alle medlemmer ligger.*/
    private final Klubben klubben;

    /**
     * Konstruktør.
     * Modtager et Database-objekt, som controller skal arbejde med.
     */
    public MemberController(Klubben klubben){
        this.klubben = klubben;
    }


    /**
     * Opretter et nyt medlem i systemet.

     * Controlleren sender informationen videre til Database,
     * som opretter objektet og gemmer det i filen.
     */
    public void addNewMember(String firstName, String surName, String phoneNr, LocalDate birthDate, boolean isCompetitive, boolean isActive, boolean isPaid){
        klubben.addNewMember(firstName,surName, phoneNr, birthDate,isCompetitive,isActive,isPaid);
    }

    /**
     * Henter hele listen af medlemmer.
     * Bruges bl.a. i Formand-menuen.
     */
    public ArrayList<Member> getAllMembers(){
        return klubben.getAllMembers();
    }

    /**
     * Opdaterer kontingent for ALLE medlemmer.
     * Bruges ved programstart og hvis kontingentregler ændres.
     */
    public void updateYearlyFee() {
        klubben.updateYearlyFee();
    }

    /**
     * Returnerer det samlede forventede kontingent.
     * Bruges af Kassereren.
     */
    public double getTotalExpectedFees() {
        return klubben.getTotalExpectedFees();
    }

    /**
     * Returnerer liste over alle medlemmer, som ikke har betalt.
     */
    public ArrayList<Member> getMembersInDebt() {
        return klubben.getMembersInDebt();
    }

    /**
     * Finder et medlem via telefonnummer.
     * Returnerer null hvis det ikke findes.
     */
    public Member findByPhoneNr(String phoneNr) {
        return klubben.findByPhoneNr(phoneNr);
    }

    /**
     * Markerer et medlem som betalt.
     * Returnerer true hvis det lykkes, ellers false.
     */
    public void setMemberPaid(String phoneNr) {
        klubben.setMemberPaid(phoneNr);
    }

    public Member removeMember (String phoneNr) {
        return klubben.removeMember(phoneNr);
    }

    public CompetitiveSwimmer promoteToCompetitive(String phoneNr){
        return klubben.promoteToCompetitive(phoneNr);
    }
}
