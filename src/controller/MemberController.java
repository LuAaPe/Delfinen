package controller;

import domain.Member;

import java.time.LocalDate;
import java.util.ArrayList;

public class MemberController {
    //TODO er denne klasse ikke lidt skrald?

    // logic
    // Responsibilities:
    // create new member, get all members, calculate total fees, find members in debt
    Database database;

    public MemberController(Database database){
        this.database = database;
    }


    // MemberController klassen har et Database objekt, som kun kan addnewMember

    //Regular member constructor: Member(String firstName, String surName, LocalDate birthDate, boolean isCompetitive, boolean isActive, boolean isPaid)
    public void addNewMember(String firstName, String surName, String phoneNr, LocalDate birthDate, boolean isCompetitive, boolean isActive, boolean isPaid){
        database.addNewMember(firstName,surName, phoneNr, birthDate,isCompetitive,isActive,isPaid);
    }

    public ArrayList<Member> getAllMembers(){
        return database.getAllMembers();
    }

    public void updateYearlyFee() {
        database.updateYearlyFee();
    }

    // Metode som skal bruges efter at have ændret fælter på en member
    // f.eks. i forbindelse med setActive! så at ændringerne faktisk gemmes, når en medlem er gået fra passiv til aktiv
    public void updateMember(Member member){
        database.saveMembers();
    }

    public double getTotalExpectedFees() {
        return database.getTotalExpectedFees();
    }

    public ArrayList<Member> getMembersInDebt() {
        return database.getMembersInDebt();
    }

    public Member findByPhoneNr(String phoneNr) {
        return database.findByPhoneNr(phoneNr);
    }

    public boolean setMemberPaid(String phoneNr) {
        return database.setMemberPaid(phoneNr);
    }

}
