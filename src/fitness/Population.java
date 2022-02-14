package fitness;

// JDK imports
import java.awt.Graphics;
import java.awt.geom.Point2D;

// BNS imports
import application.ApplicationWindow;
import application.Util;
import application.Environment;
import vehicles.DriveOutput;
import vehicles.PredatorVehicle;
import vehicles.PreyVehicle;
import vehicles.Vehicle;
import vehicles.Vehicles;

public class Population extends Vehicles {

    private Environment environment;
    private Vehicles matingPool = new Vehicles();
    private Vehicles nextGen = new Vehicles();
    private int MATING_POOL;
    private double MUTATION_RATE = .1;

    public Population(Environment env) {
        this.environment = env;
    }

    public void doAGeneration() {
        if (this.size() <= 20) {
            MATING_POOL = (int) (this.size() * .4);
        } else {
        }
        sortByFitness();
        selectMatingPool();
        applyGeneticOperators();
        replacement();
    }

    public void sortByFitness() {
        FitnessComparator fc = new FitnessComparator();
        this.sort(fc);
    }

    public void selectMatingPool() {
        for (int i = 0; i < MATING_POOL; i++) {
            matingPool.add(this.get(i));
        }
    }

    public void applyGeneticOperators() {
        for (Vehicle v : matingPool) {
            mutate(v.createClone());
        }
        initNextGen();
    }

    public void replacement() {
        for (int i = 0; i < nextGen.size(); i++) {
            this.remove(this.size() - 1 - i);
            this.add(nextGen.get(i));
        }
        nextGen.clear();
        matingPool.clear();
        for (Vehicle next : this) {
            next.setFitness(0);
        }
    }

    public void step() {
        Vehicles nextGeneration = new Vehicles();
        for (Vehicle nextVehicle : this) {
            nextVehicle.step(this.environment);
            Vehicle offspring = nextVehicle.reproduce();
            if (offspring != null) nextGeneration.add(offspring);
        }
        for (Vehicle clone : nextGeneration) {
            this.add(clone);
        }
    }

    public void update() {
        for (Vehicle nextVehicle : this) {
            DriveOutput theOutput = nextVehicle.generateOutput(this.environment);
            nextVehicle.moveIt(theOutput);
        }
    }

    @Override
    public String toString() {
        String returnMe = "I am a Population:";
        returnMe += "\n\tContaining:";
        for (Vehicle next : this) {
            returnMe += "\n\t\t" + next.toString();
        }
        return returnMe;
    }

    public void paint(Graphics g) {
        for (Vehicle e : this) {
            e.paint(g);
        }
    }

    private void mutate(Vehicle v) {
        for (int ii = 0; ii < Evaluable.LENGTH; ii++) {
            int rand = Util.randomInt(100);
            if (MUTATION_RATE < rand) {
                switch (v.getDNA()[ii]) {
                    case 0:
                        v.getDNA()[ii] = 1;
                    case 1:
                        v.getDNA()[ii] = 0;
                }
            }
        }
        nextGen.add(v);
    }

    private void initNextGen() {
        for (int ii = 0; ii < this.nextGen.size(); ii++) {
            this.nextGen.get(ii).setLocation(new Point2D.Double(Util.randomInt(ApplicationWindow.WINDOW_WIDTH), Util.randomInt(ApplicationWindow.WINDOW_HEIGHT)));
            this.nextGen.get(ii).setOrientation(Util.randomDouble(2) * Math.PI);
            this.nextGen.get(ii).setFitness(0);
        }
    }

}
