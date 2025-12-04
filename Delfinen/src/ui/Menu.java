package ui;

import controller.MemberController;
import util.InvalidBirthYearException;

import java.time.LocalDate;
import java.time.Period;
import java.util.Scanner;

public class Menu {
    Scanner input = new Scanner(System.in); // TODO: kan denne bruges replace de steder hvor en ny scanner oprettes i metoder her nedunder???
    MemberController memberController = new MemberController();
    public Menu(){}

    public void startMenu(){
        while (true){
            System.out.println("   NAVIGATIONS-MENU:");
            System.out.println("""
                    +-+\s
                    |1|\s FORMAND
                    +-+\s
                    """);
            System.out.println("""
                    +-+\s
                    |2|\s KASSERER
                    +-+\s
                    """);
            System.out.println("""
                    +-+\s
                    |3|\s TRÆNER
                    +-+\s
                    """);
            System.out.println("""
                    +-+\s
                    |4|\s AFSLUT
                    +-+\s
                    """);

            try{
                System.out.print(": ");
                int choice = input.nextInt();
                input.nextLine(); // clear buffer
                switch (choice){
                    case 1:
                        //Formand
                        // evt login
                        loop1();
                        break;
                    case 2:
                        // Kasserer
                        break;
                    case 3:
                        //Træner
                        break;
                    case 4:
                        // exit
                        input.close();
                        System.exit(0);
                        break;
                    default:
                        break;
                }

            }
            catch (Exception e){
                System.out.println(e);
            }
        }
    }

    public void loop1(){
        while(true){
            System.out.println("   FORMAND:");
            System.out.println("1. Se Medlemsliste");
            System.out.println("""
                    ╔═══════════════╗
                    ║2. OPRET MEDLEM║
                    ╚═══════════════╝
                    """);
            System.out.println("3. Redigere Medlem");
            System.out.println("4. Slet Medlem");
            System.out.println("5. Find medlem");
            System.out.println("""
                    ╔═══════════════╗
                    ║6. TILBAGE     ║
                    ╚═══════════════╝
                    """);
            System.out.println("""
                    ╔═══════════════╗
                    ║7. AFSLUT      ║
                    ╚═══════════════╝
                    """);

            try {
                Scanner inputFormand = new Scanner(System.in);
                System.out.print(": ");
                int formandChoice = input.nextInt();
                input.nextLine(); // clear buffer
                switch (formandChoice){
                    case 1:
                        // se medlemsliste
                        //System.out.println(memberRegistry);
                        break;
                    case 2:
                        // opret medlem
                        createMember();
                        break;
                    case 3:
                        // Redigere medlem f.eks
                        break;
                    case 4:
                        // f.eks slet medlem
                        // f.eks memberList.deleteMember();
                        break;
                    case 5:
                        // Vis kontingenter f.eks
                        break;
                    case 6:
                        //Tilbage
                        return;
                    case 7:
                        // exit
                        input.close();
                        System.exit(0);
                        break;
                    default:
                        break;
                }
            }
            catch (Exception e){
                System.out.println(e);
            }
        }
    }
    //Metode som samler input data ind fra brugeren og kan oprette ett domain.Member objekt
    private void createMember(){
        boolean isActive;
        boolean isCompetitiveSwimmer;
        // Evt senere at det kan be brugeren at indtaste det fulde navn i et trin, som vi splitter op i fornavn og efternavn
        System.out.println("Indtast fornavn\n : ");
        String firstName = input.nextLine();
        System.out.println("Indtast efternavn\n : ");
        String surName = input.nextLine();
        LocalDate birthDate = enterBirthDate(); // brug dette til noget
        System.out.println("Aktivere medlemskab? j/n\n : ");
        String activePassiveStatus = input.nextLine();
        if(activePassiveStatus.equals("j")){
            isActive = true;
        }
        else {
            isActive = false;
        }

        System.out.println("Konkurrencesvømmer? j/n\n : ");
        String isCompetitiveReply = input.nextLine();
        if(isCompetitiveReply.equals("j")){
            isCompetitiveSwimmer = true;
        }
        else{
            isCompetitiveSwimmer = false;
        }

        System.out.println("Har medlem betalt? j/n\n : ");
        boolean hasPaid;
        String hasPaidReply = input.nextLine();
        if(hasPaidReply.equals("j")){
            hasPaid = true;
        }
        else {
            hasPaid = false;
        }

        //Opret et nyt domain.Member objekt her med de data som samlets ind
        // Test:
        memberController.addNewMember(firstName, surName, birthDate, isCompetitiveSwimmer, isActive, hasPaid);

    }

    public LocalDate enterBirthDate(){
        boolean again = true; // så længe som again er true kører while-loopen
        System.out.print("TAST FØDSELSDATO (ÅÅÅÅ-MM-DD): ");
        LocalDate birthDate = LocalDate.now(); // TODO FIX
        while (again){
            try {
                String date = input.nextLine();
                birthDate = convertStringDateToLocalDate(date);// prøver på at lave et LocalDate objekt fra den input String
                int age = calculateAge(birthDate); // finder lige ud af alderen....
                if (age < 6 || age > 100) {
                    throw new InvalidBirthYearException("Alder skal være minimum 6 og maks 100.");
                }
                again = false;
            } catch (Exception j) {
                System.out.println("Tastefejl. Venligst prøv igen (ÅÅÅÅ-MM-DD): ");
            }
        }
        // TODO null fejlhåndtering
        return birthDate;
    }

    public LocalDate convertStringDateToLocalDate(String date){
        int birthYear = Integer.parseInt(date.split("-")[0]);
        int birthMonth = Integer.parseInt(date.split("-")[1]);
        int birthDay = Integer.parseInt(date.split("-")[2]);
        return LocalDate.of(birthYear, birthMonth, birthDay);
    }

    public int calculateAge(LocalDate date){
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(date, currentDate);
        return period.getYears();
    }



    public void printDolphinArt(){
        System.out.println("""
                    /*
                     *                                    __
                     *                                _.-~  )
                     *                     _..--~~~~,'   ,-/     _
                     *                  .-'. . . .'   ,-','    ,' )
                     *                ,'. . . _   ,--~,-'__..-'  ,'
                     *              ,'. . .  (@)' ---~~~~      ,'
                     *             /. . . . '~~             ,-'
                     *            /. . . . .             ,-'
                     *           ; . . . .  - .        ,'
                     *          : . . . .       _     /
                     *         . . . . .          `-.:
                     *        . . . ./  - .          )
                     *       .  . . |  _____..---.._/ _____
                     * ~---~~~~----~~~~             ~~
                     */
                
                """);
    }
}


