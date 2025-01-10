import java.util.Scanner;
public class Main {

    static int maxplayerHP = 100;
    static int playerHP = 100, monsterHP = 50;
    static int playerATKDMG = 10, monsterATKDMG = 5;
    static int level = 1, nextlevel = 2, experiencefornextlevel = 100;
    static int experiencepoints = 0, experiencegain = 10;
    static int healingitems = 3;
    static int flag = 0;
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) throws InterruptedException {


        System.out.println("Welcome to Adventure simulator!");
        while (flag == 0) {
            gameMenu();
            char option;
            option = input.next().charAt(0);
            dooption(option);
        }


    }

    private static void dooption(char option) throws InterruptedException {

        switch (option) {
            case '1':
                explore();
                break;
            case '2':
                printLevel();
                break;
            case '3':
                if (healingpossible(healingitems)) {
                    heal();
                } else {
                    System.out.println("You dont have any healing items left!");
                }
                break;
            case '4':
                if (checkifsure()) {
                    System.exit(0);
                }
                break;
            default:
                System.out.println("Invalid choice!\nchoose a valid option!");
                break;
        }

    }

    private static boolean healingpossible(int amountofitems) {
        return amountofitems > 0;
    }

    private static void gameMenu() {
        System.out.println("What will you do?\n1. Explore\n2. Check experience points\n3. Heal\n4. Quit Game");
    }

    private static boolean checkifsure() {
        System.out.println("Are you sure you want to quit?\n\ty for yes)");
        char choice = input.next().charAt(0);
        return choice == 'Y' || choice == 'y';
    }

    private static boolean iscrit() {
        return chanceRandomizer() > 80;
    }

    private static int chanceRandomizer() {
        return ((int) (Math.random() * 100));
    }

    private static void explore() throws InterruptedException {
        // 10% chance for a village (90+), 50% chance for a mob (40>x>90), 20% for a healing item (20-40), 20%  for nothing (0-20)
        int chancerolled = chanceRandomizer();
        if (chancerolled > 90) {
            System.out.println("You found a village, they liked you and gave you special items!\n+3 healing items!");
            healingitems += 3;
        }
        if (chancerolled > 20 && chancerolled < 40) {
            System.out.println("You found a healing item on the ground!\n+1 healing item");
            healingitems++;
        }
        if (chancerolled > 40 && chancerolled < 90) {
            System.out.println("Monster encountered!");
            fight();
        }
        if (chancerolled > 0 && chancerolled < 20) {
            System.out.println("you couldnt find anything!");
        }
    }

    private static void printLevel() {
        double wayuntilnextlevel = 100.0 * experiencepoints / experiencefornextlevel;
        System.out.println("You are level " + level + "!\n" + "You are " + wayuntilnextlevel + "% away from the next level");
        System.out.println("Experience: " + experiencepoints + "/" + experiencefornextlevel);

    }

    private static void heal() {
        if (playerHP + 20 > maxplayerHP) {
            playerHP = maxplayerHP;
        }
        else {
            playerHP += 20;
        }

        System.out.println("You succesfully used a healing item and you are now on " + playerHP + " HP!\nYou have " + healingitems + " left");
    }

    private static void rollMonsterType() {
        //boss = 10%, hard = 30%, easy = 60%
        int chance = chanceRandomizer();
        if (chance <= 60) {
            monsterTypeEasy();
            System.out.println("look! this is a weak mob!");
        } else if (chance <= 90) {
            monsterTypeHard();
            System.out.println("This monster seems stronger than the usual ones...");
        } else if (chance <= 100) {
            monsterTypeBoss();
            System.out.println("wow! you found a boss! this battle is going to be hard!");
        }
    }

    private static void monsterTypeEasy() {
        monsterHP = 50;
        monsterATKDMG = 5;
        experiencegain = 10;
    }

    private static void monsterTypeHard() {
        monsterHP = 70;
        monsterATKDMG = 10;
        experiencegain = 30;
    }

    private static void monsterTypeBoss() {
        monsterHP = 100;
        monsterATKDMG = 15;
        experiencegain = 120;
    }


    private static void levelUp() {
        experiencepoints -= experiencefornextlevel;
        level++;
        nextlevel++;
        maxplayerHP = (int) (maxplayerHP*1.1);
        playerATKDMG++;
        experiencefornextlevel = (int) (experiencefornextlevel * 1.25);
    }


    private static void printmobstats() {
        System.out.println("\nMob stats:");
        System.out.println("Attack damage: " + monsterATKDMG);
        System.out.println("Health points: " + monsterHP);
        System.out.println("experience you will gain for defeating this mob: " + experiencegain + "\n");
    }


    private static void fight() throws InterruptedException {
        rollMonsterType();
        printmobstats();
        boolean escaped = false;
        int turn = 0;

        while (playerHP > 0 && monsterHP > 0 && !escaped) {
            if (turn % 2 == 0) {
                System.out.println("Your turn!");
                System.out.println("What will you do?");
                System.out.println("1. Attack");
                System.out.println("2. Heal");
                System.out.println("3. Try to escape");
                int choice = input.nextInt();
                if (choice == 1) {
                    if (iscrit()) {
                        System.out.println("Landed a critical hit! you hit for " + (int) (playerATKDMG * 1.5) + "damage");
                        monsterHP -= (int) (playerATKDMG * 1.5);
                        if (monsterHP > 0) {
                            System.out.println("it now has " + monsterHP + "HP!");
                        } else {
                            System.out.println("Monster defeated!");
                        }

                    } else {
                        System.out.println("\n\nYou attacked and hit for " + playerATKDMG + " damage");
                        monsterHP -= playerATKDMG;
                        Thread.sleep(250);
                        if (monsterHP > 0) {
                            System.out.println("it now has " + monsterHP + "HP!");
                        } else {
                            System.out.println("Monster Defeated!");
                        }
                    }
                }

                if (choice == 2) {
                    if (healingpossible(healingitems)) {
                        heal();
                        Thread.sleep(250);
                    } else {
                        System.out.println("You don't have any healing items left!\n");
                        Thread.sleep(250);
                    }
                }

                if (choice == 3 && chanceRandomizer() > 60) {
                    System.out.println("you managed to escape!");
                    Thread.sleep(250);
                    break;
                }
                if (choice == 3 && chanceRandomizer() <= 60) {
                    System.out.println("The monster blocked your escape route!");
                    Thread.sleep(250);
                }
                if (choice == 3 && chanceRandomizer() > 60) {
                    System.out.println("Escaped!");
                    escaped = true;
                }
            }
            if (turn % 2 == 1) if (iscrit()) {
                System.out.println("it landed a critical hit! you got hit for " + (int) (monsterATKDMG * 1.5) + "damage");
                playerHP -= (int) (playerATKDMG * 1.5);
                Thread.sleep(250);
                System.out.println("You now have " + playerHP + "HP!\n");
            } else {
                System.out.println("\n\nYou were hit for " + monsterATKDMG + " damage");
                playerHP -= monsterATKDMG;
                Thread.sleep(250);
                if (playerHP > 0) {
                    System.out.println("You now have " + playerHP + "HP!\n");
                } else {
                    System.out.println("You died!");
                }

            }

            if (monsterHP <= 0) {
                experiencepoints += experiencegain;
                if (experiencepoints >= experiencefornextlevel) {
                    levelUp();
                }
            }
            turn++;
        }


    }
}
