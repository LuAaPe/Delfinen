package controller;

import data.MemberFileHandler;
import domain.Member;

import java.time.LocalDate;
import java.util.ArrayList;

public class Database {
    // memberList med alle member objekter vi bruger når programmet kører
    private ArrayList<Member> memberList = new ArrayList<>();
    // Et MemberFileHandler objekt, som har de metoder vi bruger for at læse og gemme filen med member info
    private MemberFileHandler memberFileHandler = new MemberFileHandler("Memberlist.txt");

    public Database(){
        // når et Database opjekt oprettes, så fyldes memberList med de members som finnes i filen
        this.memberList = memberFileHandler.loadedMembers();
    }

    // Metode som opretter nyt Member objekt, og tilføjer det i filen
    // regular member constructor: Member(String firstName, String surName, LocalDate birthDate, boolean isCompetitive, boolean isActive, boolean isPaid)
    public void addNewMember(String firstName, String surName, LocalDate birthDate, boolean isCompetitive, boolean isActive, boolean isPaid){
        try {
            Member member = new Member(firstName, surName, birthDate, isCompetitive, isActive, isPaid);
            memberList.add(member);
            memberFileHandler.saveListOfMembersToFile(memberList);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
