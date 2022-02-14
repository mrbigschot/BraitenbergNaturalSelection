package miscellaneous;

// JDK imports
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;

public class FoodSource extends AbstractSource {

    int width = 16;
    int offset = width / 2;

    public FoodSource() { }

    public FoodSource(Point2D.Double location, double strength) {
        setIntensity(strength);
        setLocation(location);
    }
    
    @Override
    public int getSize() { return (int)((this.intensity / 5000.0) * this.width); }

    @Override
    public void paint(Graphics g) {
        int x = (int)getLocation().getX();
        int y = (int)getLocation().getY();
        g.setColor(new Color(150, 200, 150));
        g.fillOval(x - offset, y - offset, getSize(), getSize());
//        g.setColor(Color.BLACK);
//        g.drawString("" + (int) this.getIntensity(), x, y);
    }

}
