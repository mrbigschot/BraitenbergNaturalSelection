package application;

public class Util {

    public static double MAX_STIM = 0.0;

    public static double THRESH = 1500.0;
    protected static boolean DEBUG = true;

    protected static int food = 12;
    public static int preySize = 16;
    public static int predSize = 5;
    public static int genTime = 1000;
    public static double growthRate = 0.3;

    public static void debug(String s) {
        if (DEBUG) {
            System.out.println(s);
        }
    }

    public static int randomInt(int max) {
        return (int) (Math.random() * max);
    }

    public static int randomInt(int min, int max) {
        int returnMe = min;
        returnMe += (int) (Math.random() * (max - min));
        return returnMe;
    }

    public static double randomDouble(double max) {
        return Math.random() * max;
    }

    public static double randomDouble(double min, double max) {
        double returnMe = min;
        returnMe += (Math.random() * (max - min));
        return returnMe;
    }

    public static int getFood() {
        return food;
    }

    public static int getPreySize() {
        return preySize;
    }

    public static int getPredSize() {
        return predSize;
    }

    public static int getGenTime() {
        return genTime;
    }

    public static double getGrowthRate() {
        return growthRate;
    }

}
