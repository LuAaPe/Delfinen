package util;

import domain.CompetitiveSwimmer;
import domain.Discipline;
import domain.Result;

import java.util.Comparator;

/**
 * Comparator der sammenligner to konkurrencesvømmere ud fra deres
 * beste resultat i en bestemt disciplin.
 * <p>
 * Comparator bruges af ResultController.getTop5(), så vi kan sortere svømmere:
 *          hurtigste tid ----> langsommere tid
 * <p>
 * Hvis en svømmer ikke har nogen tid i den disciplin, bliver de sorteret
 * nederst på listen (efter dem som HAR tider).
 */
public class SwimmerBestResultComparator implements Comparator<CompetitiveSwimmer> {
    private final Discipline discipline;

    public SwimmerBestResultComparator(Discipline discipline){
        this.discipline = discipline;
    }

    @Override
    public int compare(CompetitiveSwimmer swimmer1, CompetitiveSwimmer swimmer2){
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

        // Begge har en tid ---> sammenlign dem,
        return Integer.compare(result1.getTimeMilliSeconds(),result2.getTimeMilliSeconds());

    }
}
