package ui;

import controller.Database;
import controller.MemberController;
import controller.ResultController;
import domain.*;
import util.InvalidBirthYearException;
import util.MemberNotFoundException;
import util.NotCompetitiveSwimmerException;

import java.time.LocalDate;
import java.time.Period;
import java.util.Scanner;

public class Menu {
    Scanner input = new Scanner(System.in);
    private final Database database = new Database();
    private final MemberController memberController = new MemberController(database);
    ResultController resultController = new ResultController(database);


    public Menu(){
        resultController.loadResults();
    }

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
                int choice = readInt(); // Metode som sørger for at brugeren taster en int ind
                switch (choice){
                    case 1:
                        chairmanMenu();
                        break;
                    case 2:
                        treasurerMenu();
                        break;
                    case 3:
                        trainerMenu();
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

    private void chairmanMenu(){
        while(true){
            System.out.println("   FORMAND:");
            System.out.println("""
                    ╔═══════════════╗
                    ║1. SE MEDLEMMER║
                    ╚═══════════════╝
                    """);
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
                System.out.print(": ");
                int formandChoice = readInt();
                switch (formandChoice){
                    case 1:
                        for(Member m : memberController.getAllMembers()){
                            System.out.println(m);
                        }
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
        String phoneNr = enterPhoneNr();
        LocalDate birthDate = enterBirthDate();
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
        memberController.addNewMember(firstName, surName, phoneNr, birthDate, isCompetitiveSwimmer, isActive, hasPaid);

    }

    private void treasurerMenu(){
        boolean run = true;
        while(run){
            System.out.println("""
                    --KASSERER--
                    1. Vis samlet forventet kontingent
                    2. Vis medlemmer i restance
                    3. Registrer betaling
                    0. Tilbage
                    """);
            int choice = readInt();

            switch (choice){
                case 1 -> showTotalFees();
                case 2 -> showMembersInDebt();
                case 3 -> registerPayment();
                case 0 -> run = false;
                default -> System.out.println("Ugyldigt Valg");
            }
        }
    }
    private void trainerMenu(){
        while(true){
            System.out.println("   FORMAND:");
            System.out.println("""
                    ╔══════════════════════════╗
                    ║1. Tilføj træningsresultat║
                    ╚══════════════════════════╝
                    """);
            System.out.println("""
                    ╔════════════════════════╗
                    ║2. Tilføj stævneresultat║
                    ╚════════════════════════╝
                    """);
            System.out.println("3. Se top 5");
            System.out.println("""
                    ╔═════════════════════════════════════╗
                    ║4. Vis liste over konkurrencesvømmere║
                    ╚═════════════════════════════════════╝
                    """);
            System.out.println("""
                    ╔════════════════════════════╗
                    ║5. Vis en medlems resultater║
                    ╚════════════════════════════╝
                    """);
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
                System.out.print(": ");
                int trainerChoice = readInt();
                switch (trainerChoice){
                    case 1:
                        addTrainingResult();
                        break;
                    case 2:
                        addCompetitionResult();
                        break;
                    case 3:
                        // top 5
                        break;
                    case 4:
                        viewAllCompetitiveMembers();
                        break;
                    case 5:
                        viewMemberResuls();
                        break;
                    case 6:
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
    private void showTotalFees(){
        memberController.updateYearlyFee();
        double total = memberController.getTotalExpectedFees();
        System.out.println("Samlet forventet kontingent: " + total + " kr.");
    }
    private void showMembersInDebt(){
        for (Member m : memberController.getMembersInDebt()){
            System.out.println(m);
        }
    }
    private void registerPayment(){
        System.out.print("Indtast telefon nr. på medlemmet: ");
        String phoneNr = input.nextLine().trim();

        boolean success = memberController.setMemberPaid(phoneNr);

        if (success) {
            System.out.println("Betaling registreret på: " + memberController.findByPhoneNr(phoneNr));
        } else {
            System.out.println("Ingen medlem med det telefonnummer blev fundet.");
        }
    }

    private void addTrainingResult(){
        try {
            System.out.println("Indtast telefonnummer på svømmer\n: ");
            String phone = input.nextLine();
            Discipline discipline = enterDiscipline();
            int timeMilliSeconds = enterSwimmingTime();
            LocalDate date = enterResultDate();

            resultController.addTrainingResult(phone, discipline, timeMilliSeconds, date);
            System.out.println("Træningsresultat gemt!");
        }
        catch (MemberNotFoundException | NotCompetitiveSwimmerException e){
            System.out.println("Fejl: "+ e.getMessage());
        }
        catch (Exception e){
            System.out.println("Et fejl upstod. "+ e.getMessage());
        }
    }

    private void addCompetitionResult(){
        try {
            System.out.println("Indtast telefonnummer på svømmer:\n");
            String phone = input.nextLine();

            Discipline discipline = enterDiscipline();
            int timeMilliSeconds = enterSwimmingTime();
            LocalDate date = enterResultDate();

            System.out.println("Indtast stævne\n: ");
            String eventName = input.nextLine();

            System.out.println("Indtast placering (1, 2, 3,. . .\n: ");
            int placement = readInt();

            resultController.addCompetitionResult(phone, discipline, timeMilliSeconds, date, eventName, placement);
            System.out.println("Stævneresultat gemt!");

        }
        catch (MemberNotFoundException | NotCompetitiveSwimmerException e){
            System.out.println("Fejl: " + e.getMessage());
        }
        catch (Exception e){
            System.out.println("En fejl upstod: " + e.getMessage());
        }

    }

    //helper: metode til at få brugeren at indtaste en disciplin
    private Discipline enterDiscipline(){
        System.out.println("""
                --- INDTAST DISCIPLIN ---
                1. Butterfly
                2. Crawl
                3. Backstroke
                4. Breaststroke
                """);

        while (true){
            int choice = readInt();
            switch (choice){
                case 1:
                    return Discipline.BUTTERFLY;
                case 2:
                    return Discipline.CRAWL;
                case 3:
                    return Discipline.BACKSTROKE;
                case 4:
                    return Discipline.BREASTSTROKE;
                default:
                    System.out.println("Ugyldigt valg, prøv igen:");
                    break;
            }
        }
    }

    // Metode som får brugeren at taste resultattid ind og konverterer det til millisekunder
    private int enterSwimmingTime(){
        System.out.println("Indtast tid (SS.mmm eller MM:SS.mmm)\n: ");

        while (true){
            String timeString = input.nextLine();

            try {
                if (timeString.contains(":")){
                    //MM:SS.mmm
                    String[] parts = timeString.split("[:.]"); // Hvis timeString har ":" så skulle det gerne have 3 dele
                    if(parts.length != 3){
                        throw new IllegalArgumentException();
                    }
                    int minutes = Integer.parseInt(parts[0]);
                    int seconds = Integer.parseInt(parts[1]);
                    int milliSeconds = Integer.parseInt(parts[2]);
                    return (minutes * 60000) + (seconds * 1000) + milliSeconds;
                }
                else { // Hvis timeString IKKE har et ":" så svarer det til et decimaltal
                    // brugeren kan f.eks indtaste "57.3", "57.31", "57.314", "57.3149" hvilket ville give lidt problemer med at splitte det ind i dele og tolke med Integer parse
                    // SS.mm
                    double seconds = Double.parseDouble(timeString);
                    return (int)(seconds * 1000);
                }
            }
            catch (Exception e){
                System.out.println("Ugyldig tid. Prøv igen\n: ");
            }
        }
    }

    // Metode som får brugeren at taste et dato ind til resultater
    private LocalDate enterResultDate(){
        System.out.println("Indtast dato (ÅÅÅÅ-MM-DD)\n: ");
        while (true){

            try {
                String inputDate = input.nextLine();
                return LocalDate.parse(inputDate);
            }
            catch (Exception e){
                System.out.println("Forkert format, prøv igen (ÅÅÅÅ-MM-DD)\n: ");
            }
        }
    }

    // Metode som viser en liste af alle konkurrencesvømmere
    private void viewAllCompetitiveMembers(){
        System.out.println("\n - - - Liste over konkurrencesvømmere - - -");

        boolean foundSwimmers = false; // boolean flag for a simple check

        for ( Member member : database.getAllMembers()){
            if (member instanceof CompetitiveSwimmer competitiveSwimmer){
                System.out.printf("%-20s Tlf: %s ",
                        competitiveSwimmer.getFullName(),
                        competitiveSwimmer.getPhoneNr());
                foundSwimmers = true;
            }
        }

        if (!foundSwimmers){
            System.out.println("Ingen konkurrencesvømmere.");
        }

        System.out.println("------------------\n");
    }

    // Metode som finder en konkurrensesvømmer, og viser resultater den har
    private void viewMemberResuls(){
        try {
            System.out.println("Indtast telefonnummer på svømmeren\n: ");
            String phone = input.nextLine();

            CompetitiveSwimmer competitiveSwimmer = resultController.getCompetitiveSwimmer(phone);

            // TODO: Can use Comparator here to sort results by date
            // competitiveSwimmer.getResults() . . . // Comparator here
            // competitiveSwimmer.getCompetitionResults() . . .

            System.out.println("\n- - - Resultater for "+
                    competitiveSwimmer.getFullName() + " - - -");

            System.out.println("\nTræningsresultater:");
            if (competitiveSwimmer.getTrainingResults().isEmpty()){
                System.out.println("Ingen resultater.");
            }
            else {
                for (Result result : competitiveSwimmer.getTrainingResults()){
                    String line = String.format("%s - %s - %s",
                            result.getDate(),
                            result.getDiscipline(),
                            formatTime(result.getTimeMilliSeconds()));
                    System.out.println(line);
                }
            }

            System.out.println("\nStævneresultater:");
            if (competitiveSwimmer.getCompetitionResults().isEmpty()){
                System.out.println("Ingen resultater.");
            }
            else {
                for (CompetitionResult competitionResult : competitiveSwimmer.getCompetitionResults()){
                    String line = String.format("%s - %s - %s (%s, placering: %d)",
                            competitionResult.getDate(),
                            competitionResult.getDiscipline(),
                            formatTime(competitionResult.getTimeMilliSeconds()),
                            competitionResult.getEventName(),
                            competitionResult.getPlacement());
                    System.out.println(line);

                }
            }
            System.out.println("\n--------------------------");
        }
        catch (MemberNotFoundException | NotCompetitiveSwimmerException e){
            System.out.println("Fejl: " + e.getMessage());
        }
    }

    // Metode som formaterer resultattiden fra en int så den kan fremvises i konsolen
    private String formatTime(int milliSeconds){
        int minutes = milliSeconds / 60000;
        int seconds = (milliSeconds % 60000) / 1000;
        int millis = milliSeconds % 1000;

        return String.format("%d:%02d.%03d", minutes, seconds, millis);
    }

    // Metode som sørjer for at brugeren indtaster en int
    private int readInt(){
        while (true){
            try {
                int value = Integer.parseInt(input.nextLine());
                return value;
            }
            catch (NumberFormatException e){
                System.out.println("Ugyltigt tal, prøv igen.");
            }
        }
    }

    // Metode som ber brugeren at indtaste fødselsdato
    public LocalDate enterBirthDate(){
        boolean again = true; // så længe som again er true kører while-loopen
        System.out.print("TAST FØDSELSDATO (ÅÅÅÅ-MM-DD): ");
        LocalDate birthDate = LocalDate.now();
        while (again){
            try {
                String date = input.nextLine();
                birthDate = convertStringDateToLocalDate(date);// prøver på at lave et LocalDate objekt fra den input String
                int age = calculateAge(birthDate); // finder lige ud af alderen....
                if (age < 6 || age > 100) {
                    throw new InvalidBirthYearException("Alder skal være minimum 6 og maks 100.\nVenligst prøv igen (ÅÅÅÅ-MM-DD): ");
                }
                again = false;
            }
            catch (InvalidBirthYearException e){
                System.out.println(e.getMessage());
            }
            catch (Exception j) {
                System.out.println("Tastefejl. Venligst prøv igen (ÅÅÅÅ-MM-DD): ");
            }
        }
        // TODO null fejlhåndtering?
        return birthDate;
    }

    public String enterPhoneNr() {
        while (true) {
            System.out.print("Indtast telefon Nr: ");
            String phoneNr = input.nextLine().trim();

            if ((phoneNr.length() != 8) && (phoneNr.length() != 11)) {
                System.out.println("Fejl: Indtast et telefon nr på 8 cifre eller 11 cifre");
                System.out.println(" 8 cifre: 12345678");
                System.out.println("11 cifre: +45 12345678");
                continue;
            }

            if (!phoneNr.matches("\\+?\\d+")){
                System.out.println("Fejl Telfon nr. må kun indeholde tal og evt. + foran");
                continue;
            }

            boolean duplicatePhoneNr = false;

            for (Member m : memberController.getAllMembers()) {
                if (m.getPhoneNr().equals(phoneNr)) {
                    duplicatePhoneNr = true;
                    break;
                }
            }
            if (duplicatePhoneNr){
                System.out.println("Telefon Nr. er allerede registeret på en eksisterende kunde");
                continue;
            }
            return phoneNr;
        }
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


