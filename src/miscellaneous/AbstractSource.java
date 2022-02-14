package miscellaneous;

// JDK imports
import java.awt.Graphics;
import java.awt.geom.Point2D;

// BNS imports
import application.Util;

public abstract class AbstractSource {

    protected Point2D.Double location;
    protected double intensity;

    public Point2D.Double getLocation() { return this.location; }
    public void setLocation(Point2D.Double value) { this.location = value; }

    public double getIntensity() { return this.intensity; }
    public void setIntensity(double value) { this.intensity = value; }

    abstract public void paint(Graphics g);
    abstract public int getSize();

    public void deplete(double strength) {
        this.intensity -= strength;
        this.intensity += Util.getGrowthRate();
    }
}
