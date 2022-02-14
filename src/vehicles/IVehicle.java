package vehicles;

public interface IVehicle {
    
    public static int PREY_CLONE_THRESHOLD = 500;
    public static int PREY_STARVATION_THRESHOLD = 100;
    public static int PREY_REFRACTORY_TIME = 100;
    public static int PREY_NUTRITION_VALUE = 10;
    
    public static int PREDATOR_CLONE_THRESHOLD = 200;
    public static int PREDATOR_STARVATION_THRESHOLD = 100;
    public static int PREDATOR_REFRACTORY_TIME = 300;
    public static int PREDATOR_NUTRITION_VALUE = 50;
    
    public static int MAX_FITNESS = 10000;
}