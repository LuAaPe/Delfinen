package util;

import domain.CompetitiveSwimmer;
import domain.Discipline;
import domain.Result;

import java.util.Comparator;

/**
 * SwimmerBestResultComparator bruges til at sammenligne to konkurrencesvømmere
 * ud fra deres bedste resultat i en bestemt disciplin.
 * Denne comparator anvendes især i forbindelse med udregning af TOP 5-listen,
 * hvor svømmere skal sorteres efter:
 *      hurtigste tid  --->  langsommere tid
 * Comparatoren indeholder også logik til at håndtere manglende resultater.
 * Hvis en svømmer ikke har nogen tider i den valgte disciplin, placeres
 * vedkommende nederst på listen.
 * Dette gør det muligt at sortere svømmere konsistent, selvom nogle har
 * flere eller færre registrerede resultater.
 */
public class SwimmerBestResultComparator implements Comparator<CompetitiveSwimmer> {
    /** Den disciplin der skal sorteres efter (fx CRAWL, BUTTERFLY osv.) */
    private final Discipline discipline;

    /**
     * Opretter en comparator der kan sammenligne svømmere efter en bestemt disciplin.
     */
    public SwimmerBestResultComparator(Discipline discipline){
        this.discipline = discipline;
    }

    /**
     * Sammenligner to svømmere ved at udtrække deres bedste tid
     * for den valgte disciplin.
     * Regler for sammenligning:
     *  1. Har begge svømmere ingen tider → de er "lige".
     *  2. Har kun én af dem en tid → den med tid rangeres højere.
     *  3. Har begge tider → hurtigste tid rangeres højest.
     *
     * @param swimmer1 første svømmer
     * @param swimmer2 anden svømmer
     * @return negativt tal hvis swimmer1 er hurtigere end swimmer2,
     *         positivt tal hvis swimmer2 er hurtigere,
     *         0 hvis de er lige.
     */
    @Override
    public int compare(CompetitiveSwimmer swimmer1, CompetitiveSwimmer swimmer2){
        // Find bedste resultat for hver svømmer
        Result result1 = swimmer1.getBestResultForDiscipline(discipline);
        Result result2 = swimmer2.getBestResultForDiscipline(discipline);

        // Hvis begge mangler resultat ----> den er lige
        if(result1 == null && result2 == null){
            return 0;
        }

        // Mangler swimmer1 resultat ---> den skal længere ned i listen
        if (result1 == null) {
            return 1;
        }

        // Mangler swimmer2 resultat -----> længst ned
        if (result2 == null) {
            return -1;
        }

        // Begge har en tid ---> sammenlign tider (laveste = hurtigst)
        return Integer.compare(result1.getTimeMilliSeconds(),result2.getTimeMilliSeconds());

    }
}
