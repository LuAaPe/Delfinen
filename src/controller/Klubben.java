package controller;

import data.MemberFileHandler;
import domain.CompetitiveSwimmer;
import domain.Member;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Copy;
import util.AlreadyCompetitiveSwimmerException;
import util.MemberNotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;


/**
 * Database-klassen fungerer som programmets "hukommelse"/"datalager".
 * <p>
 * Den indeholder:
 * - En liste over alle medlemmer
 * - Metoder til at oprette, ændre og hente medlemmer
 * - Forbindelse til filsystemet via MemberFileHandler
 * <p>
 * Database læser medlemmer fra Memberlist.txt når programmet starter,
 * og gemmer dem tilbage i filen når det sker ændringer
 * <p>
 * Den her klasse interagerer IKKE med brugeren.
 * Den bruges alene af controllers.
 */
public class Klubben {

    /**
     * Bruges til at læse og gemme members i tekstfilen.
     */
    private final MemberFileHandler fileHandler;
    /**
     * Listen med alle Member-objekter som programmet bruger mens det kører.
     */
    private final ArrayList<Member> members;

    /**
     * Konstruktør til Database.
     * Når Database oprettes:
     * 1. Indlæs alle medlemmer fra tekstfilen
     * 2. Kontingenter opdateres (i tilfælde af ændrede priser)
     * 3. Alt gemmes igen så filen er opdateret
     */
    public Klubben(MemberFileHandler fileHandler) {
        this.fileHandler = fileHandler;

        // Indlæs medlemmer fra filen via MemberFileHandler
        this.members = fileHandler.loadedMembers();

        // Sørg for at kontingent-priser altid er korrekte ved opstart
        updateYearlyFee();
    }

    /**
     * Opretter et nyt Member-objekt og tilføjer det til listen,
     * samt gemmer hele listen i tekstfilen.
     * <p>
     * Member(. . .) konstruktøren tager alle de nødvendige parametre.
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
     * Returnerer en kopi listen over alle medlemmer.
     * Der returneres en kopi for ikke at bryde encapsulation
     * Vi vil ikke give adgang til ændring via getAllMembers
     * Bruges bl.a. af Formand- og Kasserer-menuerne.
     */
    public ArrayList<Member> getAllMembers() {
        return new ArrayList<>(members);
    }

    /**
     * Gemmer hele listen af medlemmer i tekstfilen.
     * Bruges når:
     * - medlem oprettes
     * - medlem ændres
     * - medlem betaler
     */
    public void saveMembers() {
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
        saveMembers();
    }

    /**
     * Beregner samlet forventet kontingent fra alle medlemmer.
     * Bruges af Kasserer-menuen.
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
     * Markerer et medlem som betalr, gemmer ændringen i filen,
     * og returnerer true hvis det lykkes.
     * <p>
     * Bruges i Kasserer-menuen.
     */
    public void setMemberPaid(String phoneNr) {
        Member m = findByPhoneNr(phoneNr);
        m.setIsPaid(true);
        saveMembers(); // Gem ændringen
    }

    public Member removeMember(String phoneNr) {
        Member m = findByPhoneNr(phoneNr);
        members.remove(m);
        saveMembers();
        return m;
    }

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
