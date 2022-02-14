package vehicles;

// JDK imports
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;

// BNS imports
import application.Util;
import application.Environment;
import sensors.PreySensor;
import sensors.PredatorSensor;
import sensors.Sensor;
import sensors.StimulusList;
import sensors.Stimulus;

public class PredatorVehicle extends Vehicle {

    private int speed, appetite, sight;
    private PreyVehicle currentMeal = null;
    private PreyVehicle lastMeal = null;

    public PredatorVehicle() {
        super();
        this.preySense = 12000;
        this.predSense = 1000;
    }
    public PredatorVehicle(Point2D.Double location, double orientation) {
        this();
        super.location = location;
        super.orientation = orientation;
        setupSensors();
        initTraits();
    }
    public PredatorVehicle(int fit, byte[] dna) {
        this();
        super.setFitness(fit);
        super.dna = dna;
        setupSensors();
        initTraits();
    }

    @Override
    public int getAntennaeSize() { return 30; }
    
    private void setupSensors() {
        PreySensor preySensor = new PreySensor(1);
        preySensor.setCrossed(true);
        addSensor(preySensor);

        PredatorSensor predSensor = new PredatorSensor();
        predSensor.setCrossed(false);
        addSensor(predSensor);
    }

    @Override
    public void paint(Graphics g) {
        int x = (int) getX();
        int y = (int) getY();
        drawBody(g, x, y);
        g.setColor(Color.RED);
        Point2D.Double left = leftSensorLocation();
        g.drawLine(x, y, (int) left.getX(),(int)left.getY());
        Point2D.Double right = rightSensorLocation();
        g.drawLine(x, y, (int)right.getX(), (int)right.getY());
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
        DriveOutputList returnMe = new DriveOutputList();
        for (int i = 0; i < sensors.size(); i++) {
            Sensor nextSensor = sensors.get(i);
            Stimulus stim = stimList.get(i);
            double left = stim.getLeft();
            double right = stim.getRight();
            if (nextSensor.getCrossed()) {
                returnMe.add(nextSensor.createDriveOutput(right, left, this)); //crossed
            } else {
                returnMe.add(nextSensor.createDriveOutput(left, right, this));
            }
        }
        return returnMe;
    }

    @Override
    public void step(Environment environment) {
        super.step(environment);
        lastMeal = currentMeal;
    }

    @Override
    public String toString() {
        return "  speed = " + speed + " sight = " + sight + " appetite = " + appetite;
    }

    @Override
    public void determineTraits() {
        int[] traits = PredatorTraitDeterminer.getValue(this);
        speed = traits[PredatorTraitDeterminer.SPEED];
        appetite = traits[PredatorTraitDeterminer.APPETITE];
        sight = traits[PredatorTraitDeterminer.SIGHT];
    }

    @Override
    public byte[] getDNA() {
        return dna;
    }

    @Override
    public Vehicle createClone() {
        return new PredatorVehicle(super.getFitness(), super.dna.clone());
    }

    @Override
    public void evaluateFitness() {
        if (isStarving()) {
            subtractFitness(1);
        } else if (isFeeding()) {
            addFitness((int)(foodWeight * PREDATOR_NUTRITION_VALUE));
        }
    }

    @Override
    public final void initTraits() {
        determineTraits();
        implementTraits();
    }

    @Override
    public void implementTraits() {
        maxSpeed = speed * .5;
        preySense = 1800 * appetite;
        predSense = 100 * sight;

    }

    @Override
    public void setCurrentMeal(PreyVehicle value) { this.currentMeal = value; }
    private boolean notSameMeal() { return lastMeal.getDNA() != currentMeal.getDNA(); }

}