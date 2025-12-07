package util;

import domain.CompetitionResult;

import java.util.Comparator;

public class CompetitionDateComparator implements Comparator<CompetitionResult> {
    public int compare(CompetitionResult result1, CompetitionResult result2){
        return result1.getDate().compareTo(result2.getDate());
    }
}
