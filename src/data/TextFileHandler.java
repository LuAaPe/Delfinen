package data;

import java.io.*;
import java.util.Scanner;

/**
 * Interface som indeholder fælles funktionalitet til filhåndtering,
 * som både MemberFileHandler og ResultFileHandler har brug for.
 *
 * Begge klasser arbejder med tekstfiler, og begge skal kunne:
 * - åbne en fil til læsning (Scanner)
 * - åbne en fil til skrivning (PrintWriter)
 *
 * Default-metoder:
 * et interface indeholder "default"-metoder, som har en færdig implementering.
 * Klasser som implenterer interfacet kan bruge metoderne uden selv at skulle skrive dem.
 */
public interface TextFileHandler {

    /**
     * Åbner en tekstfil til læsning og returnerer en Scanner.
     * Hvis filen ikke findes, kastes FileNotFoundException.
     * @param fileName                 Navnet på den fil der skal åbnes.
     * @return                         En Scanner klar til at læse fra filen.
     * @throws FileNotFoundException   Hvis filen ikke findes.
     */
    default Scanner openScanner(String fileName) throws FileNotFoundException{
        return new Scanner(new File(fileName));
    }

    /**
     * Åbner en tekstfil til skrivning og returnerer en PrintWriter.
     * Hvis filen ikke findes, bliver den oprettet automatisk.
     * @param fileName          Navnet på den fil der skal skrives til.
     * @return                  En PrintWriter klar til filen.
     * @throws IOException      IOException hvis filen ikke kan åbnes eller oprettes.
     */
    default PrintWriter openWriter(String fileName) throws IOException {
        return new PrintWriter(new FileWriter(fileName));
    }
}
