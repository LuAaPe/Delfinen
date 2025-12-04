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
    // Member(String firstName, String surName, String PhoneNr, LocalDate birthDate, boolean isCompetitive, boolean isActive, boolean isPaid)
    public void addNewMember(String firstName, String surName, String phoneNr, LocalDate birthDate, boolean isCompetitive, boolean isActive, boolean isPaid){
        try {
            Member member = new Member(firstName, surName, phoneNr, birthDate, isCompetitive, isActive, isPaid);
            memberList.add(member);
            memberFileHandler.saveListOfMembersToFile(memberList);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateYearlyFee() {
        for (Member member : memberList) {
            member.setYearlyFee();
        }
    }

    public ArrayList<Member> getAllMembers(){
        return memberList;
    }



    //TODO Overvej hvor disse Metoder skal ligge???

    //Retunere den forventede indkomst for Året
    public double getTotalExpectedFees(){
        double sum = 0;
        for (Member m : memberList){
            sum += m.getYearlyFee();
        }
        return sum;
    }

    public ArrayList<Member> getMembersInDebt(){
        ArrayList<Member> inDebt = new ArrayList<>();
        for (Member m : memberList){
            if (!m.getIsPaid()){
                inDebt.add(m);
            }
        }
        return inDebt;
    }

    public Member findByPhoneNr(String phoneNr){
        for (Member m : memberList){
            if (m.getPhoneNr().equals(phoneNr)){
                return m;
            }
        }
        return null;
    }

    public void setMemberPaid(String phoneNr){
        findByPhoneNr(phoneNr).setIsPaid(true);
    }

}
