package sensors;

import java.util.ArrayList;

public class StimulusList extends ArrayList<Stimulus> {

    public boolean oversaturated(double thresh) {
        for (Stimulus next : this) {
            if (next.oversaturated(thresh)) {
                return true;
            }
        }
        return false;
    }
    
    public void multiply(double d) {
        for (Stimulus next : this) {
            next.multiply(d);
        }
    }

}