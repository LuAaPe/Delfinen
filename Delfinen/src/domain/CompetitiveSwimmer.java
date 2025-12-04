package domain;

import java.time.LocalDate;

public class CompetitiveSwimmer extends Member{
    // classes representing the real world
    public CompetitiveSwimmer(String firstName, String surName, String phoneNumber, LocalDate birthDate, boolean isCompetitive, boolean isActive, boolean isPaid) {
        super(firstName, surName, phoneNumber, birthDate, isCompetitive, isActive, isPaid);
    }

}