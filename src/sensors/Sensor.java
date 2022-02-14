package sensors;

// JDK imports
import java.awt.geom.Point2D;

// BNS imports
import application.Environment;
import vehicles.DriveOutput;
import vehicles.Vehicle;

public abstract class Sensor {
    
    private int weight;
    private boolean crossed;

    public void setWeight(int value) { this.weight = value; }
    public int getWeight() { return weight; }
    public void setCrossed(boolean value) { crossed = value; }
    public boolean getCrossed() { return crossed; }
    
    public double getStimulusStrength(Environment environment, Vehicle v, Point2D.Double sensorLocation) {
        return environment.getStimulusStrength(v, sensorLocation, this.getSource());
    }

    abstract public StimulusType getSource();
    abstract public DriveOutput createDriveOutput(double right, double left, Vehicle v);
    abstract public DriveOutput createDriveOutput(double right, double left, Vehicle v, Stimulus s);

}