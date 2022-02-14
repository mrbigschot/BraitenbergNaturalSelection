package fitness;

import java.util.Comparator;

public class FitnessComparator implements Comparator<Evaluable> {
    
    @Override
    public int compare(Evaluable o1, Evaluable o2) {
        if (o1.getFitness() > o2.getFitness()) {
            return -1;
        } else if (o1.getFitness() < o2.getFitness()) {
            return 1;
        } else {
            return 0;
        }
    }
    
}