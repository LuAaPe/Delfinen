package ui;
/**
 * LookItsADolphin er en lille dekorativ UI-klasse,
 * som udelukkende bruges til at udskrive ASCII-kunst,
 * når programmet afsluttes.
 * Klassen bruges fra MainMenu, når brugeren vælger "Afslut".
 */
public class LookItsADolphin {

    /**
     * Dekorativ ASCII kunst :-)
     * Inspirationen kommer fra den klassiske "So Long, and thanks for
     * all the fish!" fra Hitchhiker's Guide to the Galaxy.
     */
    public static void printDolphinArt(){
        System.out.println("""
                    /*
                     *      So Long, and thanks for all the fish!
                     *                                 _.-~  )
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
