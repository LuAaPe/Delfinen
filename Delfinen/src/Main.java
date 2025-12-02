import UI.Menu;

import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Database memberRegistry = new Database();

        memberRegistry.addMember(new Member("Freddy", "surname", LocalDate.of(1999,01,22)));

        memberRegistry.addMember(new Member("Hannah","", LocalDate.of(1963,01,22)));

        memberRegistry.addMember(new Member("Cecilie","", LocalDate.of(1989,07,17)));

        memberRegistry.addMember(new Member("Lars","", LocalDate.of(1970,10,24)));

        memberRegistry.addMember(new Member("Henriette","Laholm", LocalDate.of(2010,01,01)));

        System.out.println(memberRegistry);

        Member member01 = new Member("Lars","Bentesen",LocalDate.of(1985,03,03));

        System.out.println(member01);
        Menu menu = new Menu();
        menu.printDolphinArt();
        menu.startMenu();
        System.out.println("Git TEST 123");
        System.out.println("HEJ HALLÅ FRA DANI");
    }





}