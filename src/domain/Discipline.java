package domain;

/**
 * Discipline ræpresenterer de forskellige svømmediscipliner,
 * som en svømmer kan træne eller konkurrere i.
 * Enum-typen bruges for at sikre, at disciplinen altid er en
 * af de gyldige ig tilladte værdier. Det gør programmet mere
 * robust, og minimerer fejl som ellers kunne opstå ved brug
 * af almindelige tekststrenge (stavefejl).
 */
public enum Discipline {
    BUTTERFLY, CRAWL, BACKSTROKE, BREASTSTROKE
}
