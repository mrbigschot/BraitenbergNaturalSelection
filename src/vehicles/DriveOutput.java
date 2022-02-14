package vehicles;

/*
 crossed sensor + aggressive = attack the stimulus
 crossed + passive = admires the stimulus, then leaves
 uncrossed sensor + aggressive = run away from stimulus
 uncrossed + passive = attracted to the stimulus
 */
public class DriveOutput {

    private static final int MAX_OUTPUT = 100;
    private double leftWheelOutput;
    private double rightWheelOutput;

    public DriveOutput() { }
    public DriveOutput(double left, double right) {
        this.setLeftWheelOutput(left);
        this.setRightWheelOutput(right);
    }
    public double getLeftWheelOutput() { return this.leftWheelOutput; }
    public final void setLeftWheelOutput(double value) {
        this.leftWheelOutput = value;
        if (value > MAX_OUTPUT) this.leftWheelOutput = MAX_OUTPUT;
        
    }
    public double getRightWheelOutput() { return this.rightWheelOutput; }
    public final void setRightWheelOutput(double value) {
        this.rightWheelOutput = value;
        if (value > MAX_OUTPUT) this.rightWheelOutput = MAX_OUTPUT;
    }

    public void multiply(double value) {
        this.leftWheelOutput *= value;
        this.rightWheelOutput *= value;
    }

    public DriveOutput combine(DriveOutput o, Vehicle v) {
        DriveOutput returnMe = new DriveOutput();
        double left = o.getLeftWheelOutput() + this.getLeftWheelOutput();
        double right = o.getRightWheelOutput() + this.getRightWheelOutput();
        if (left > v.getMaxSpeed()) {
            returnMe.setLeftWheelOutput(v.getMaxSpeed());
        } else {
            returnMe.setLeftWheelOutput(left);
        }
        if (right > v.getMaxSpeed()) {
            returnMe.setRightWheelOutput(v.getMaxSpeed());
        } else {
            returnMe.setRightWheelOutput(right);
        }
        return returnMe;
    }

    @Override
    public String toString() {
        String returnMe = "Drive Output: ";
        returnMe += "\tleft=" + leftWheelOutput;
        returnMe += "\tright=" + rightWheelOutput;
        return returnMe;
    }

}
