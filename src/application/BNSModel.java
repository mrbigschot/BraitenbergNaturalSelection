package application;

// JDK imports
import java.awt.Graphics;
import java.awt.geom.Point2D;

// BNS imports
import fitness.Population;
import vehicles.Vehicle;
import vehicles.PredatorVehicle;
import vehicles.PreyVehicle;
import miscellaneous.AbstractSource;
import miscellaneous.FoodSource;
import miscellaneous.SourceList;
import sensors.StimulusType;

public class BNSModel {

    private int time;
    private Population preyPop, predatorPop;
    private SourceList foodSources;
    private Environment environment;

    public BNSModel(Environment env) {
        this.environment = env;
        this.initPopulations();
        this.initSources();
    }

    private void initPopulations() {
        this.preyPop = new Population(this.environment);
        this.preyPop.initPrey();
        this.predatorPop = new Population(this.environment);
        this.predatorPop.initPredators();
    }

    private void initSources() {
        this.foodSources = new SourceList();
        for (int i = 0; i < Util.getFood(); i++) {
            this.foodSources.add(new FoodSource(
                new Point2D.Double(
                    Util.randomInt(ApplicationWindow.WINDOW_WIDTH),
                    Util.randomInt(ApplicationWindow.WINDOW_HEIGHT)
                ),
                Util.randomInt(5000, 10000)
            ));
        }
    }
    
    public Population getPreyPop() { return this.preyPop; }
    public Population getPredatorPop() { return this.predatorPop; }
    public void reset() {
        this.time = 0;
        this.initPopulations();
        this.initSources();
    }

    public void step() {
        this.time++;
        this.checkForCollisions();
        this.evaluateFitness();
        this.killDeadVehicles();
    }

    public void doAGeneration() {
        this.preyPop.doAGeneration();
        this.predatorPop.doAGeneration();
    }

    public void evaluateFitness() {
        this.preyPop.step();
        this.preyPop.update();
        this.predatorPop.step();
        this.predatorPop.update();
    }

    public void init() { }

    public void finish() {
        this.doAGeneration();
        this.time = 0;
    }

    public int getTime() { return this.time; }

    public void paint(Graphics g) {
        this.foodSources.paint(g);
        this.preyPop.paint(g);
        this.predatorPop.paint(g);
    }

    public void completeGeneration() {
        this.doAGeneration();
        this.time = 0;
    }
    
    private void checkForCollisions() {
        this.checkForPredatorFood();
        this.checkForPreyFood();
    }
     
    private void checkForPredatorFood() {
        for (int ii = 0; ii < this.predatorPop.size(); ii++) {
            PredatorVehicle predator = (PredatorVehicle)this.predatorPop.get(ii);
            predator.setFeeding(hasEaten(predator));
        }
        killDeadPrey();
    }
    private boolean hasEaten(PredatorVehicle predator) {
         for (int ii = 0; ii < this.preyPop.size(); ii++) {
            PreyVehicle prey = (PreyVehicle)this.preyPop.get(ii);
            if (!prey.isEaten() && inRange(predator, prey)) {
                prey.setEaten(true);
                predator.setFoodWeight(prey.getFitnessRatio());
                return true;
            }
        }
        return false;
    }
    private boolean inRange(PredatorVehicle predator, PreyVehicle prey) {
        double preyX = prey.getLocation().getX();
        double preyY = prey.getLocation().getY();

        double predX = predator.getLocation().getX();
        double predY = predator.getLocation().getY();
        
        return distance(preyX, preyY, predX, predY) <= 2 * predator.getAntennaeSize();
    }
    
    private void checkForPreyFood() {
        for (int ii = 0; ii < preyPop.size(); ii++) {
            PreyVehicle prey = (PreyVehicle)preyPop.get(ii);
            prey.setFeeding(feeding(prey, foodSources));
        }
    }
    private boolean feeding(PreyVehicle prey, SourceList foodSources) {
        for (AbstractSource source : foodSources) {
            if (source.getIntensity() > 0 && inRange(prey, source)) return true;
        }
        return false;
    }
    private boolean inRange(PreyVehicle prey, AbstractSource source) {
        double preyX = prey.getLocation().getX();
        double preyY = prey.getLocation().getY();

        double sourceX = source.getLocation().getX();
        double sourceY = source.getLocation().getY();
        
        return distance(preyX, preyY, sourceX, sourceY) <= prey.getAntennaeSize() + source.getSize();
    }
    
    private double distance(double x1, double y1, double x2, double y2) {
        double dX = x1 - x2;
        double dY = y1 - y2;
        double xSquared = Math.pow(dX, 2);
        double ySquared = Math.pow(dY, 2);
        return Math.sqrt(xSquared + ySquared);
    }
    
    public double getStimulusStrength(Point2D.Double location, Vehicle v, StimulusType source) {
        double result = 0.0;
        switch (source) {
            case FOOD:
                for (AbstractSource nextSource : foodSources) {
                    double d = location.distance(nextSource.getLocation());
                    double strength = nextSource.getIntensity() / (1.5 * (d * d));
                    result += strength;
                    nextSource.deplete(strength);
                }
            case PREY:
                for (Vehicle nextVehicle : this.preyPop) {
                    double d = location.distance(nextVehicle.getLocation());
                    result += v.getPreySense() / (d * d);
                }
                break;
            case PREDATOR:
                for (Vehicle nextVehicle : this.predatorPop) {
                    double d = location.distance(nextVehicle.getLocation());
                    result += v.getPredSense() / (d * d);
                }
                break;
        }
        return result;
    }
    
    private void killDeadPrey() {
        for (int i = 0; i < preyPop.size(); i++) {
            if (preyPop.get(i).isKilled()) {
                preyPop.remove(i);
            }
        }
    }

    private void killDeadVehicles() {
//        for (int i = 0; i < preyPop.size(); i++) {
//            if (preyPop.get(i).getKill()) {
//                preyPop.remove(i);
//            }
//        }
        for (int i = 0; i < predatorPop.size(); i++) {
            if (predatorPop.get(i).isKilled()) {
                predatorPop.remove(i);
            }
        }
    }
    

}
