package sensors;

// BNS imports
import vehicles.Vehicle;
import vehicles.DriveOutput;
import vehicles.PassiveDriveOutput;

public class FoodSensor extends Sensor {

    public FoodSensor() { }
    public FoodSensor(int w) {
        super.setWeight(w);
    }

    @Override
    public StimulusType getSource() { return StimulusType.FOOD; }

    @Override
    public DriveOutput createDriveOutput(double left, double right, Vehicle v) {
        double avgStrength = (left + right) / 2.0;
        DriveOutput result = new PassiveDriveOutput(left, right, avgStrength, v);
        return result;
    }

    @Override
    public DriveOutput createDriveOutput(double left, double right, Vehicle v, Stimulus s) {
        double avgStrength = (left + right) / 2.0;
        DriveOutput result = new PassiveDriveOutput(left, right, avgStrength, v);
        result.multiply(s.getWeight());
        return result;
    }

}