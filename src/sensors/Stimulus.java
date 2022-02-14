package sensors;

public class Stimulus {

    private double left, right;
    private double weight;
    private StimulusType source;
    
    public Stimulus() { }
    public Stimulus(double l, double r, StimulusType s) {
        this.left = l;
        this.right = r;
        this.source = s;
    }
    
    public double getLeft() { return left; }
    public void setLeft(double l) { this.left = l; }
    public double getRight() { return right; }
    public void setRight(double r) { this.right = r; }
    public double getWeight() { return weight; }
    public void setWeight(double w) { this.weight = w; }
   
    public StimulusType getSource() { return source; }
    
    public boolean oversaturated(double thresh) {
        return (left >= thresh || right >= thresh);
    }
    
    public void multiply(double d) {
        setLeft(left * d);
        setRight(right * d);
    }
    
    public double getAvgStim() {
        return (getLeft() + getRight()) / 2;
    }

}