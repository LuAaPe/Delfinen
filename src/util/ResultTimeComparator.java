package util;

import domain.Result;

import java.util.Comparator;

public class ResultTimeComparator implements Comparator<Result> {

    public int compare(Result result1, Result result2){
        return Integer.compare(result1.getTimeMilliSeconds(), result2.getTimeMilliSeconds());
    }
}
