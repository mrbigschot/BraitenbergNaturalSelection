package fitness;

public interface Evaluable {

    public static int LENGTH = 100;

    // Genetic Algorithm methods needed
    public byte[] getDNA();
    public int getFitness();
    public void setFitness(int fitness);

    public void initTraits();
    public void implementTraits();

    public void evaluateFitness();
    public void addFitness(int value);   
    public void subtractFitness(int value);

}