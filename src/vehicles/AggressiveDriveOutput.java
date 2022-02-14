package vehicles;

public class AggressiveDriveOutput extends DriveOutput {

    public AggressiveDriveOutput() { }
    public AggressiveDriveOutput(double left, double right, double strength, Vehicle v) {
        double l = 0;
        double r = 0;
        double maxL = v.getMaxSpeed();
        double maxR = v.getMaxSpeed();

        if (left + l < maxL) {
            this.setLeftWheelOutput(left + l);
        } else {
            this.setLeftWheelOutput(maxL);
        }
        if (right + r < maxR) {
            this.setRightWheelOutput(right + r);
        } else {
            this.setRightWheelOutput(maxR);
        }
    }
    
}