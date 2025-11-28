import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        MemberRegistry memberRegistry = new MemberRegistry();

        memberRegistry.addMember(new Member("Freddy", LocalDate.of(1999,01,22)));

        memberRegistry.addMember(new Member("Hannah", LocalDate.of(1999,01,22)));

        memberRegistry.addMember(new Member("Cecilie", LocalDate.of(1989,07,17)));

        memberRegistry.addMember(new Member("Lars", LocalDate.of(1970,10,24)));

        memberRegistry.addMember(new Member("Henriette", LocalDate.of(2005,01,01)));

        System.out.println(memberRegistry);
        /*
        while (true) {
            System.out.println("  |~~~~~~~~~~~~~");
            System.out.println("  | KlubSystem:");
            System.out.println("  | < 1 > Vis Medlemmer");
            System.out.println("  | < 2 > Luk Program");
            System.out.println("  |~~~~~~~~~~~~~");
            System.out.print("  | Vælg Handling: ");

            int number = input.nextInt();
            input.nextLine();
            switch (number) {
                case 1:
                    System.out.println(memberRegistry);
                    break;
                case 2:
                    input.close();
                    System.exit(0);
                    break;
                default:
                    break;
            }
        }*/
    }





}