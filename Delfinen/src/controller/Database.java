package controller;

import data.MemberFileHandler;
import domain.Member;

import java.time.LocalDate;
import java.util.ArrayList;

public class Database {
    private ArrayList<Member> memberList = new ArrayList<>();
    private MemberFileHandler memberFileHandler = new MemberFileHandler("Memberlist.txt");

    public Database(){
        this.memberList = memberFileHandler.loadedMembers();
    }

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
