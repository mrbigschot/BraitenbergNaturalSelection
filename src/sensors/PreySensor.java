package sensors;

// BNS imports
import vehicles.DriveOutput;
import vehicles.AggressiveDriveOutput;
import vehicles.PassiveDriveOutput;
import vehicles.Vehicle;
import vehicles.PredatorVehicle;
import vehicles.PreyVehicle;

public class PreySensor extends Sensor {

    public PreySensor() { }
    public PreySensor(int w) {
        super.setWeight(w);
    }

    @Override
    public StimulusType getSource() { return StimulusType.PREY; }

    @Override
    public DriveOutput createDriveOutput(double left, double right, Vehicle v) {
        double avgStrength = (left + right) / 2.0;
        DriveOutput result = null;
        if (v instanceof PredatorVehicle)
            result = new AggressiveDriveOutput(left, right, avgStrength, v);
        if (v instanceof PreyVehicle)
            result = new PassiveDriveOutput(left, right, avgStrength, v);
        return result;
    }

    @Override
    public DriveOutput createDriveOutput(double left, double right, Vehicle v, Stimulus s) {
        double avgStrength = (left + right) / 2.0;
        DriveOutput result = null;
        if (v instanceof PredatorVehicle)
            result = new AggressiveDriveOutput(left, right, avgStrength, v);
        if (v instanceof PreyVehicle)
            result = new PassiveDriveOutput(left, right, avgStrength, v);
        if (result != null) 
            result.multiply(s.getWeight());
        return result;
    }

}