package controller;

import domain.Member;

import java.time.LocalDate;
import java.util.ArrayList;

public class MemberController {
    //TODO er denne klasse ikke lidt skrald?

    // logic
    // Responsibilities:
    // create new member, get all members, calculate total fees, find members in debt
    Database database = new Database(); // MemberController klassen har et Database objekt, som kun kan addnewMember

    //Member(String firstName, String surName, String PhoneNr, LocalDate birthDate, boolean isCompetitive, boolean isActive, boolean isPaid)
    public void addNewMember(String firstName, String surName, String phoneNr, LocalDate birthDate, boolean isCompetitive, boolean isActive, boolean isPaid){
        database.addNewMember(firstName,surName, phoneNr, birthDate,isCompetitive,isActive,isPaid);
    }

    public ArrayList<Member> getAllMembers(){
        return database.getAllMembers();
    }


}
