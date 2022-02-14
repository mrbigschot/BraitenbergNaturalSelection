package vehicles;

import java.util.ArrayList;

public class DriveOutputList extends ArrayList<DriveOutput> {

    public DriveOutput average() {
        double left = 0;
        double right = 0;

        for (DriveOutput next : this) {
            left += next.getLeftWheelOutput();
            right += next.getRightWheelOutput();
        }

        left = left / this.size();
        right = right / this.size();

        return new DriveOutput(left, right);
    }

    public DriveOutput sum() {
        double left = 0;
        double right = 0;

        for (DriveOutput next : this) {
            left += next.getLeftWheelOutput();
            right += next.getRightWheelOutput();
        }
        
        return new DriveOutput(left, right);
    }

}
