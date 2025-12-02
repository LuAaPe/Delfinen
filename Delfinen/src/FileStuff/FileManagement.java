package FileStuff;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileManagement {
    public FileManagement(){}

    public static void readLines() throws FileNotFoundException{
        String pathname = ""; // ????
        File file = new File(pathname);
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()){
            // Læs linjer, brug output fra scanner.nextLine() til noget her
            //scanner.nextLine();
        }
    }

    public static void writeToCsv(String testString){ // TODO: lav senere om til at tage en ArrayList<Member> i stedet for String
        // eksempel format: Daniella,Norgren,1990-10-19, . . ., . . .
        // %s,%s,%s
        String memberString = String.format("%s,%s,%s", "get name", "get last name", "get birthdate");
        try{
            Pr
        }

    }


}
