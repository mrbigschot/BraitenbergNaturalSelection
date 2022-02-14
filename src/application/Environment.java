package application;

// JDK imports
import java.awt.Graphics;
import java.awt.geom.Point2D;

// BNS imports
import vehicles.Vehicle;
import sensors.StimulusType;

public class Environment extends javax.swing.JPanel {

    private ApplicationWindow parent;
    private Controller theController;
    private BNSModel model;

    public Environment() {
        initComponents();
    }

    public Environment(ApplicationWindow parent) {
        this();
        this.parent = parent;
        this.model = new BNSModel(this);
        this.theController = new Controller(this, this.model);
        this.theController.start();
    }

    public void display() {
        this.parent.repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        this.model.paint(g);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
    public double getStimulusStrength(Vehicle v, Point2D.Double sensorLocation, StimulusType source) {
        return model.getStimulusStrength(sensorLocation, v, source);
    }
}
