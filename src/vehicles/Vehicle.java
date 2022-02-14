package vehicles;

// JDK imports
import application.ApplicationWindow;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.Color;

// BNS imports
import application.Util;
import application.Environment;
import sensors.Sensor;
import sensors.SensorList;
import fitness.Evaluable;

public abstract class Vehicle implements IVehicle, Evaluable {

    protected int timeSinceReproduced = 0;
    protected int timeSinceFed = 0;
    private boolean feeding = false;
    
    protected byte[] dna;
    private int fitness;
    protected boolean killed;
    
    protected int preySense, predSense, foodSense;

    protected double foodWeight = 1.0;
    protected Point2D.Double location;
    protected double orientation;
    protected double velocity;
    protected SensorList sensors;
    protected int size = 30;
    protected double maxSpeed = 10.0;
    protected double baseSpeed = 2.0;
    protected double vX, vY;

    public Vehicle() {
        this.sensors = new SensorList();
        this.dna = new byte[Evaluable.LENGTH];
        for (int i = 0; i < Evaluable.LENGTH; i++) {
            this.dna[i] = (byte)(Util.randomInt(2));
        }
        if (this instanceof PredatorVehicle) {
            this.fitness = PREDATOR_CLONE_THRESHOLD / 2;
        } else if (this instanceof PreyVehicle) {
            this.fitness = PREY_CLONE_THRESHOLD / 2;
        }
    }
    public Vehicle(byte[] dna) {
        this();
        this.dna = dna;
    }
    
    public int getTimeSinceReproduced() { return this.timeSinceReproduced; }
    public void setTimeSinceReproduced(int value) { this.timeSinceReproduced = value; }
    public int getTimeSinceFed() { return this.timeSinceFed; }
    public void setTimeSinceFed(int value) { this.timeSinceFed = value; }
    public void setFeeding(boolean value) {
        this.feeding = value;
        if (this.feeding) this.timeSinceFed = 0;
    }
    public boolean isFeeding() { return this.feeding; }
    
    public double getMaxSpeed() {
        if (this.getFitnessRatio() < 0.3) return this.maxSpeed * this.getFitnessRatio();
        return maxSpeed;
    }
    public double getPreySense() { return this.preySense; }
    public double getPredSense() { return this.predSense; }
    public double getBaseSpeed() { return this.baseSpeed; }
    @Override public void setFitness(int value) { this.fitness = value; }
    @Override public int getFitness() { return this.fitness; }
    public void setCurrentMeal(PreyVehicle value) { } // do nothing by default
    
    public double getOrientation() { return this.orientation; }
    public void setOrientation(double nuOrientation) { this.orientation = nuOrientation; }
    public Point2D.Double getLocation() { return this.location; }
    public void setLocation(Point2D.Double nuLocation) { this.location = nuLocation; }
    public double getX() { return this.location.getX(); }
    public double getY() { return this.location.getY(); }
    public int getSize() { return this.size; }
    public void setEaten(boolean value) { }
    public boolean isKilled() { return this.killed; }
   
    protected Point2D.Double leftSensorLocation() {
        double dx = getAntennaeSize() * Math.cos(getOrientation() + Math.PI / 4);
        double dy = -getAntennaeSize() * Math.sin(getOrientation() + Math.PI / 4);
        return new Point2D.Double(getX() + dx * 2, getY() + dy * 2);
    }
    protected Point2D.Double rightSensorLocation() {
        double dx = getAntennaeSize() * Math.cos(getOrientation() - Math.PI / 4);
        double dy = -getAntennaeSize() * Math.sin(getOrientation() - Math.PI / 4);
        return new Point2D.Double(getX() + dx * 2, getY() + dy * 2);
    }
    
    public void setFoodWeight(double value) { this.foodWeight = value; }
    
    abstract public Vehicle createClone();
    @Override public byte[] getDNA() { return this.dna; }

    abstract public int getAntennaeSize();
    public void addSensor(Sensor value) { this.sensors.add(value); }
    abstract public DriveOutput generateOutput(Environment environment);
    
    public void setCrossed(boolean value) {
        for (Sensor next : this.sensors) {
            next.setCrossed(value);
        }
    }
    public void step(Environment environment) {
        this.timeSinceFed++;
        this.timeSinceReproduced++;
        inbounds();
        evaluateFitness();
        if (this.fitness == 0) this.killed = true;
    }
    abstract public void paint(Graphics g);

    @Override
    public String toString() {
        return "Vehicle";
    }
    
    abstract public void determineTraits();
    public Vehicle reproduce() {
        Vehicle offspring = null;
        if (this.canReproduce()) {
            this.setFitness(this.getFitness() / 2);
            offspring = this.createClone();
            offspring.setLocation(this.getLocation());
            offspring.setOrientation(-this.getOrientation());
            this.setTimeSinceReproduced(0);
        }
        return offspring;
    }
    private boolean canReproduce() {
        int fitnessThresh = PREY_CLONE_THRESHOLD;
        int refractoryThresh = PREY_REFRACTORY_TIME;
        if (this instanceof PredatorVehicle) {
            fitnessThresh = Vehicle.PREDATOR_CLONE_THRESHOLD;
            refractoryThresh = PREDATOR_REFRACTORY_TIME;
        }
        return this.getTimeSinceReproduced() > refractoryThresh && this.getFitness() >=  fitnessThresh;
    }
    
    public void moveIt(DriveOutput theOutput) {
        double leftOutput = theOutput.getLeftWheelOutput();
        double rightOutput = theOutput.getRightWheelOutput();
        double direction = this.getOrientation();

        double distance = (leftOutput + rightOutput) / 2;
        double dx = distance * Math.cos(direction);
        double dy = -distance * Math.sin(direction);

        double x = this.getLocation().getX();
        double y = this.getLocation().getY();

        this.setLocation(new Point2D.Double(x + dx, y + dy));

        double deltaDirection = ((rightOutput - leftOutput) / this.getSize()) * (Math.PI / 8);
        this.setOrientation(direction + deltaDirection);
    }
    
    public boolean isStarving() {
        if (this instanceof PredatorVehicle) {
            return this.timeSinceFed > PREDATOR_STARVATION_THRESHOLD;
        } else if (this instanceof PreyVehicle) {
            return this.timeSinceFed > PREY_STARVATION_THRESHOLD;
        }
        return false;
    }
    
    protected void drawBody(Graphics g, int x, int y) {
        g.setColor(Color.BLACK);
        g.drawOval(x - size / 2, y - size / 2, size, size);
        g.setColor(Color.GRAY);
        int fitnessSize = (int)(size * getFitnessRatio());
        g.fillOval(x - fitnessSize / 2, y - fitnessSize / 2, fitnessSize, fitnessSize);
    }

    @Override
    public void addFitness(int value) {
        this.fitness += value;
        if (this.fitness > MAX_FITNESS) this.fitness = MAX_FITNESS;
    }
    @Override
    public void subtractFitness(int value) { 
        this.fitness -= value;
        if (this.fitness < 0) this.fitness = 0;
    }
    
    public double getFitnessRatio() {
        double denominator = 1.0;
        if (this instanceof PredatorVehicle) {
            denominator = PREDATOR_CLONE_THRESHOLD * 1.0;
        } else if (this instanceof PreyVehicle) {
            denominator = PREY_CLONE_THRESHOLD * 1.0;
        }
        double result = this.fitness / denominator;
        if (result > 1) return 1.0;
        return result;
    }
    
    protected void inbounds() {
        if (getX() > ApplicationWindow.WINDOW_WIDTH) {
            setLocation(new Point2D.Double(0, getY()));
        } else if (getX() < 0) {
            setLocation(new Point2D.Double(ApplicationWindow.WINDOW_WIDTH, getY()));
        }

        if (getY() > ApplicationWindow.WINDOW_HEIGHT) {
            setLocation(new Point2D.Double(getX(), 0));
        } else if (getY() < 0) {
            setLocation(new Point2D.Double(getX(), ApplicationWindow.WINDOW_HEIGHT));
        }
    }
}