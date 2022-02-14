package sensors;

// BNS imports
import vehicles.DriveOutput;
import vehicles.AggressiveDriveOutput;
import vehicles.PassiveDriveOutput;
import vehicles.Vehicle;
import vehicles.PredatorVehicle;
import vehicles.PreyVehicle;

public class PredatorSensor extends Sensor {

    public PredatorSensor() { }
    public PredatorSensor(int w) {
        super.setWeight(w);
    }

    @Override
    public StimulusType getSource() {
        return StimulusType.PREDATOR;
    }

    @Override
    public DriveOutput createDriveOutput(double left, double right, Vehicle v) {
        double avgStrength = (left + right) / 2.0;
        DriveOutput result = null;
        if (v instanceof PredatorVehicle)
            result = new PassiveDriveOutput(left, right, avgStrength, v);
        if (v instanceof PreyVehicle)
            result = new AggressiveDriveOutput(left, right, avgStrength, v);
        return result;
    }

    @Override
    public DriveOutput createDriveOutput(double left, double right, Vehicle v, Stimulus s) {
        double avgStrength = (left + right) / 2.0;
        DriveOutput result = null;
        if (v instanceof PredatorVehicle)
            result = new PassiveDriveOutput(left, right, avgStrength, v);
        if (v instanceof PreyVehicle)
            result = new AggressiveDriveOutput(left, right, avgStrength, v);
        if (result != null)
            result.multiply(s.getWeight());
        return result;
    }

}
