import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        MemberRegistry memberRegistry = new MemberRegistry();

        memberRegistry.addMember(new Member("Freddy", LocalDate.of(1999,01,22)));

        memberRegistry.addMember(new Member("Hannah", LocalDate.of(1999,01,22)));

        memberRegistry.addMember(new Member("Cecilie", LocalDate.of(1989,07,17)));

        memberRegistry.addMember(new Member("Lars", LocalDate.of(1970,10,24)));

        memberRegistry.addMember(new Member("Henriette", LocalDate.of(2005,01,01)));

        System.out.println(memberRegistry);
    }
}