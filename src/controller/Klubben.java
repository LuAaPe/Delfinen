package controller;

import data.MemberFileHandler;
import domain.CompetitiveSwimmer;
import domain.Member;
import util.AlreadyCompetitiveSwimmerException;
import util.MemberNotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;


/**
 * Klassen Klubben fungerer som programmets "hukommelse"/"datalager".
 * Den indeholder:
 * - En liste over alle medlemmer, som programmet arbejder med i hukommelsen
 * - Metoder til at oprette, ændre og fjerne medlemmer
 * - Forbindelse til filsystemet via MemberFileHandler
 * Den her klasse interagerer aldrig direkte med brugeren.
 * Den bruges alene af controllers.
 * Når Klubben oprettes:
 * 1. Indlæses alle eksisterende medlemmer fra filen
 * 2. Kontingent-priser genberegnes (i tilfælde af prisændringer)
 * 3. Data gemmes igen så filen altid er opdateret
 */
public class Klubben {

    /**
     * Filhåndterings-klassen der står for at læse og gemme medlemmer i tekstfilen.
     */
    private final MemberFileHandler fileHandler;

    /**
     * Listen med alle medlemmer, som programmet arbejder med mens programmet kører.
     * Dette er programmets "levende" medlemsregister.
     */
    private final ArrayList<Member> members;

    /**
     * Opretter et nyt Klubben-objekt
     * - Henter alle medlemmer fra tekstfilen via fileHandler
     * - Beregner og opdaterer deres kontingent
     * Klubben fungerer herefter som en samlet kilde til alle medlemsdata
     */
    public Klubben(MemberFileHandler fileHandler) {
        this.fileHandler = fileHandler;

        // Indlæs medlemmer fra filen via MemberFileHandler
        this.members = fileHandler.loadedMembers();

        // Sørg for at kontingent-priser altid er korrekte ved opstart
        updateYearlyFee();
    }

    /**
     * Opretter og gemmer et nyt medlem.
     * Hvis isCompetitive = true --> oprettes som CompetitiveSwimmer,
     * ellers som almindeligt Member-objekt.
     * Medlemmet tilføjes i listen og hele medlemslisten gemmes i filen.
     * Denne metode bruges af ChairmanMenu gennem MemberController.
     */
    public void addNewMember(String firstName, String surName, String phoneNr, LocalDate birthDate, boolean isCompetitive, boolean isActive, boolean isPaid) {

        Member member;

        if (isCompetitive) {
            member = new CompetitiveSwimmer(firstName, surName, phoneNr, birthDate, isActive, isPaid);
        } else {
            member = new Member(firstName, surName, phoneNr, birthDate, isActive, isPaid);
        }

        members.add(member);
        saveMembers(); // Gem liste med ny medlem

    }


    /**
     * Returnerer en KOPI ad medlemslisten.
     * Vi returnerer ikke den originale liste, da det bryder encapsulation,
     * og giver andre klasser mulighed for at ændre data direkte.
     * Bruges af menu-klasser til at vise alle medlemmer.
     */
    public ArrayList<Member> getAllMembers() {
        return new ArrayList<>(members);
    }

    /**
     * Gemmer hele medlemslisten i tekstfilen
     * Kaldes hver gang det sker en ændring:
     * - Nyt medlem
     * - Fjernet medlem
     * - Betaling registeret
     * - Ændring ad kontingent
     */
    public void saveMembers() {
        fileHandler.saveListOfMembersToFile(members);
    }

    /**
     * Opdaterer kontingent for ALLE medlemmer.
     * Dette sikrer at kontingent-priserne altid er korrekte når programmet startes.
     * Resultatet gemmes i filen.
     */
    public void updateYearlyFee() {
        for (Member member : members) {
            member.setYearlyFee();
        }
        saveMembers();
    }

    /**
     * Beregner den samlede kontingentindtægt ud fra alle medlemmers yearlyFee
     * Bruges af kasserer-menuen.
     */
    public double getTotalExpectedFees() {
        double sum = 0;
        for (Member m : members) {
            sum += m.getYearlyFee();
        }
        return sum;
    }

    /**
     * Returnerer en liste med alle medlemmer, som IKKE har betalt.
     * Bruges i kasserer-menuen til at vise medlemmer i restance.
     */
    public ArrayList<Member> getMembersInDebt() {
        ArrayList<Member> inDebt = new ArrayList<>();
        for (Member m : members) {
            if (!m.getIsPaid()) {
                inDebt.add(m);
            }
        }
        return inDebt;
    }

    /**
     * Finder et medlem via telefonnummer.
     * Kaster MemberNotFoundException hvis det ikke findes.
     */
    public Member findByPhoneNr(String phoneNr) {
        for (Member m : members) {
            if (m.getPhoneNr().equals(phoneNr)) {
                return m;
            }
        }
        throw new MemberNotFoundException("Intet medlem med telefonnummer: " + phoneNr);
    }

    /**
     * Markerer et medlem som betalt, gemmer ændringen i filen,
     * og returnerer true hvis det lykkes.
     * <p>
     * Bruges i kasserer-menuen.
     */
    public void setMemberPaid(String phoneNr) {
        Member m = findByPhoneNr(phoneNr);
        m.setIsPaid(true);
        saveMembers(); // Gem ændringen
    }

    /**
     * Fjerner et medlem fra systemet og gemmer ændringen,
     * Returnerer det fjernede medlem, så UI kan vise information om det.
     * Bruges af formand-menuen
     */
    public Member removeMember(String phoneNr) {
        Member m = findByPhoneNr(phoneNr);
        members.remove(m);
        saveMembers();
        return m;
    }

    /**
     * Opgraderer et almindeligt medlem til en konkurrencesvømmer.
     * Hvis medlemmet allerede er konkurrencesvømmer kastes en fejl.
     * Implementationen:
     * 1. Find det eksisterende medlem
     * 2. Opret et ny CompetitiveSwimmer-objekt med samme data
     * 3. Fjern det gamle medlem og tilføj det nye.
     * 4. Gem listen.
     * Bruges af formand-menuen når et medlem skal opgraderes.
     */
    public CompetitiveSwimmer promoteToCompetitive(String phoneNr){

        Member member = findByPhoneNr(phoneNr);

        if (member instanceof CompetitiveSwimmer){
            throw new AlreadyCompetitiveSwimmerException("Medlem er allerede konkurrence svømmer");
        }
        members.remove(member);
        CompetitiveSwimmer competitiveSwimmer = new CompetitiveSwimmer(
                member.getFirstName(),
                member.getSurName(),
                member.getPhoneNr(),
                member.getBirthDate(),
                member.getIsActive(),
                member.getIsPaid()
        );

        members.add(competitiveSwimmer);

        saveMembers();

        return competitiveSwimmer;

    }



}
