import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class MemberTest {

    @org.junit.jupiter.api.Test
    void calculateAge() {
        Member member01 = new Member("Lars","Bentesen", LocalDate.of(1985,03,03));
        int expected = 40;
        assertEquals(expected, member01.getAge());
    }

    @org.junit.jupiter.api.Test
    void setYearlyFee() {
        Member member01 = new Member("Lars","Bentesen", LocalDate.of(1985,03,03));
        double expected = 1600;
        assertEquals(expected, member01.getYearlyFee());
    }
}