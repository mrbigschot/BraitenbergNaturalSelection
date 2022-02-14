package vehicles;

public class PassiveDriveOutput extends DriveOutput {

    public PassiveDriveOutput() { }
    public PassiveDriveOutput(double left, double right, double strength, Vehicle v) {
        double l = v.getMaxSpeed();
        double r = v.getMaxSpeed();

        l = l - left;
        r = r - right;

        this.setLeftWheelOutput(l);
        this.setRightWheelOutput(r);
    }

}