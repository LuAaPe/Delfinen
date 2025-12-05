package test;
import domain.Member;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class MemberTest {
    @Test
    void passiveMemberpays500(){
        String phoneNumber = "33665544";
        Member member = new Member("Test","Member", phoneNumber, LocalDate.now(),
                false,
                false,
                false);

        assertEquals(500, member.getYearlyFee(), 0.01); // 0.1 something about doubles can be a litte "off" from the actual number we are trying to represent
    }

    @Test
    void juniorActivePays1000(){
        String phoneNumber = "33665544";
        Member member = new Member("Test", "Member", phoneNumber,
                LocalDate.of(2015,10,10),
                false,
                true,
                false);

        assertEquals(1000, member.getYearlyFee());
    }

    @Test
    void seniorActive1600(){
        String phoneNumber = "33665544";
        Member member = new Member("Test", "Member", phoneNumber,
                LocalDate.of(2000,10,10),
                false,
                true,
                false);

        assertEquals(1600, member.getYearlyFee());
    }

    @Test
    void senior60PlusGetsDiscount(){
        String phoneNumber = "33665544";
        Member member = new Member("Test", "Member", phoneNumber,
                LocalDate.of(1965,10,10),
                false,
                true,
                false);

        assertEquals(1200, member.getYearlyFee());
    }

    @Test
    void memberAge18IsSenior(){
        String phoneNumber = "33665544";
        Member member = new Member("Test", "Member", phoneNumber,
                LocalDate.now().minusYears(18),
                false,
                true,
                false);

        assertEquals(1600, member.getYearlyFee());
    }

    @Test
    void settingMemberFromPassiveToActiveRecalculatesFee(){
        String phoneNumber = "33665544";
        Member member = new Member("Test", "Member", phoneNumber,
                LocalDate.now().minusYears(18),
                false,
                false,
                false);

        assertEquals(500, member.getYearlyFee());

        member.setIsActive(true); // burde opdatere yearlyFee på den aktuelle medlem

        assertEquals(1600, member.getYearlyFee());
    }
}
