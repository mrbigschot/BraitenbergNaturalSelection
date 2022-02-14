package vehicles;

// JDK imports
import java.awt.geom.Point2D;
import java.util.ArrayList;

// BNS imports
import application.ApplicationWindow;
import application.Util;

public class Vehicles  extends ArrayList<Vehicle> {
    
    public void initPredators() {
        for (int ii = 0; ii < Util.getPredSize(); ii++) {
            this.add(new PredatorVehicle(new Point2D.Double(Util.randomInt(ApplicationWindow.WINDOW_WIDTH), Util.randomInt(ApplicationWindow.WINDOW_HEIGHT)), Util.randomDouble(2) * Math.PI));
        }
    }
    
    public void initPrey() {
        for (int ii = 0; ii < Util.getPreySize(); ii++) {
            this.add(new PreyVehicle(new Point2D.Double(Util.randomInt(ApplicationWindow.WINDOW_WIDTH), Util.randomInt(ApplicationWindow.WINDOW_HEIGHT)), Util.randomDouble(2) * Math.PI));
        }
    }
}