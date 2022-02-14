package vehicles;

// JDK imports
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;

// BNS imports
import application.Util;
import application.Environment;
import sensors.PreySensor;
import sensors.FoodSensor;
import sensors.PredatorSensor;
import sensors.Sensor;
import sensors.StimulusList;
import sensors.Stimulus;

public class PreyVehicle extends Vehicle {

    private int dangerSense, herding, speed;
    private boolean eaten;
    private boolean inDanger = false;

    public PreyVehicle() {
        super();
        this.preySense = 1000;
        this.predSense = 12000;
    }
    public PreyVehicle(int fit, byte[] dna) {
        this();
        super.setFitness(fit);
        super.dna = dna;
        setupSensors();
        determineTraits();
    }
    public PreyVehicle(Point2D.Double location, double orientation) {
        this();
        this.location = location;
        this.orientation = orientation;
        setupSensors();
        determineTraits();
    }

    @Override
    public int getAntennaeSize() { return 18; }
    
    private void setupSensors() {
        PredatorSensor predSensor = new PredatorSensor();
        predSensor.setCrossed(false);
        super.addSensor(predSensor);

        FoodSensor foodSensor = new FoodSensor();
        foodSensor.setCrossed(false);
        super.addSensor(foodSensor);

        PreySensor preySensor = new PreySensor();
        preySensor.setCrossed(false);
        super.addSensor(preySensor);
    }

    @Override
    public DriveOutput generateOutput(Environment environment) {
        StimulusList stimList = createStimList(environment);
        DriveOutputList driveList = createDriveList(environment, stimList);
        DriveOutput returnMe = driveList.sum();
        return returnMe;
    }

    public StimulusList createStimList(Environment environment) {
        StimulusList returnMe = new StimulusList();
        for (Sensor nextSensor : sensors) {
            double l = nextSensor.getStimulusStrength(environment, this, leftSensorLocation());
            double r = nextSensor.getStimulusStrength(environment, this, rightSensorLocation());
            returnMe.add(new Stimulus(l, r, nextSensor.getSource()));
        }
        while (returnMe.oversaturated(Util.THRESH)) {
            returnMe.multiply(.8); // makes stimulus strength 20% weaker
        }
        return returnMe;
    }

    public DriveOutputList createDriveList(Environment environment, StimulusList stimList) {
        double totalStim = 0;
        for (Stimulus next : stimList) {
            totalStim += next.getAvgStim();
        }
        DriveOutputList returnMe = new DriveOutputList();
        for (int i = 0; i < sensors.size(); i++) {
            Sensor nextSensor = sensors.get(i);
            Stimulus stim = stimList.get(i);
            stim.setWeight(stim.getAvgStim() / totalStim);
            double left = stim.getLeft();
            double right = stim.getRight();
            if (nextSensor.getCrossed()) {
                returnMe.add(nextSensor.createDriveOutput(right, left, this, stim)); //crossed
            } else {
                returnMe.add(nextSensor.createDriveOutput(left, right, this, stim));
            }
        }
        return returnMe;
    }

    @Override
    public void step(Environment environment) {
        super.step(environment);
        if (this.eaten) super.killed = true;
    }

    @Override
    public String toString() {
        String returnMe = "  dangerSense = " + dangerSense + " herding = " + herding + " speed = " + speed;
        return returnMe;
    }

    @Override
    public void setEaten(boolean value) { this.eaten = value; }
    public boolean isEaten() { return this.eaten; }

    @Override
    public void determineTraits() {
        int[] traits = PreyTraitDeterminer.getValue(this);
        dangerSense = traits[PreyTraitDeterminer.DANGER_SENSE];
        herding = traits[PreyTraitDeterminer.HERDING];
        speed = traits[PreyTraitDeterminer.SPEED];
    }

    @Override
    public byte[] getDNA() {
        return dna;
    }

    @Override
    public Vehicle createClone() {
        return new PreyVehicle(super.getFitness(), super.dna.clone());
    }

    @Override
    public void paint(Graphics g) {
        int x = (int) getX();
        int y = (int) getY();
        if (!this.eaten) {
            drawBody(g, x, y);
            g.setColor(Color.green);
            Point2D.Double left = leftSensorLocation();
            g.drawLine(x, y, (int)left.getX(), (int)left.getY());
            Point2D.Double right = rightSensorLocation();
            g.drawLine(x, y, (int)right.getX(), (int)right.getY());

        } else {
            g.setColor(Color.red);
            g.fillOval(x - size / 2, y - size / 2, size, size);
        }
    }

    @Override
    public void evaluateFitness() {
        if (isStarving()) {
            subtractFitness(1);
        } else if (isFeeding()) {
            addFitness(PREY_NUTRITION_VALUE);
        }
    }

    @Override
    public void initTraits() {
        determineTraits();
        implementTraits();
    }

    @Override
    public void implementTraits() {
        maxSpeed = speed * 2;
        preySense = 100 * herding;
        predSense = 1200 * dangerSense;
    }

}