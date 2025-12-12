package controller;

import domain.CompetitiveSwimmer;
import domain.Member;


import java.time.LocalDate;
import java.util.ArrayList;
/**
 * MemberController fungerer som et mellemled mellem brugerens menuer (UI)
 * og selve datahåndteringen i Klubben.
 * UI-klasserne arbejder aldrig direkte med data eller filer.
 * I stedet går alle operationer gennem denne controller:
 * UI --> MemberController --> Klubben
 * Controllerns ansvar:
 * - Oprette nye medlemmer
 * - Hente lister med medlemmer
 * - Søge efter medlemmer
 * - Opdatere oplysninger (betaling, status, osv.)
 * - Promovere medlemmer til konkurrencesvømmere
 * - Sikre at Klubben gemmer ændringer korrekt i filen
 */
public class MemberController {
    /** Reference til Klubben, som controllern kommunikerer med.
     * Alle ændringer på medlemmer foretages i Klubben via denne controller.*/
    private final Klubben klubben;

    /**
     * Opretter en MemberController og binder den til et Klubben-objekt.
     */
    public MemberController(Klubben klubben){
        this.klubben = klubben;
    }


    /**
     * Opretter et nyt medlem i systemet.

     * UI indsamler alle oplysninger (fornavn, efternav, telefon, osv)
     * og controlleren sender dem videre til Klubben, som opretter og gemmer
     * selve objektet i filen.
     */
    public void addNewMember(String firstName, String surName, String phoneNr, LocalDate birthDate, boolean isCompetitive, boolean isActive, boolean isPaid){
        klubben.addNewMember(firstName,surName, phoneNr, birthDate,isCompetitive,isActive,isPaid);
    }

    /**
     * Returnerer en liste over alle medlemmer i systemet.
     * Bruges af formand-menuen til at vise komplette medlemslister.
     */
    public ArrayList<Member> getAllMembers(){
        return klubben.getAllMembers();
    }

    /**
     * Beder Klubben om at opdatere kontingent for alle medlemmer.
     * Bruges ved opstart eller hvis kontingentregler ændres.
     */
    public void updateYearlyFee() {
        klubben.updateYearlyFee();
    }

    /**
     * Returnerer den samlede forventede kontingentindtægt.
     * Bruges i kasserer-menuen.
     */
    public double getTotalExpectedFees() {
        return klubben.getTotalExpectedFees();
    }

    /**
     * Henter alle medlemmer som ikke har betalt kontingent.
     * Bruges i kasserer-menuen.
     */
    public ArrayList<Member> getMembersInDebt() {
        return klubben.getMembersInDebt();
    }

    /**
     * Finder et medlem via telefonnummer.
     */
    public Member findByPhoneNr(String phoneNr) {
        return klubben.findByPhoneNr(phoneNr);
    }

    /**
     * Markerer et medlem som betalt.
     * Bruges i kasserer-menuen når et medlem betaler kontingent.
     * Klubben gemmer automatisk ændringen til filen.
     */
    public void setMemberPaid(String phoneNr) {
        klubben.setMemberPaid(phoneNr);
    }

    /**
     * Fjerner et medlem fra systemet.
     * Returnerer det fjernede Member-objekt, UI bruger dette til at udskrive feedback
     *Bruges i formand-menuen når et medlem skal slettes.
     */
    public Member removeMember (String phoneNr) {
        return klubben.removeMember(phoneNr);
    }

    /**
     * Opgraderer et almindeligt medlem til konkurrencesvømmer.
     */
    public CompetitiveSwimmer promoteToCompetitive(String phoneNr){
        return klubben.promoteToCompetitive(phoneNr);
    }
}
