package vehicles;

import fitness.Evaluable;

/*
 *  Written by Paul Schot
 *  Each int in traitValue represents one of the traits of a PreyVehicle
 *  traitValue[0] = speed
 *  traitValue[1] = appetite
 *  traitValue[2] = sight
 */
public class PredatorTraitDeterminer {

    public static final int SPEED = 0;
    public static final int APPETITE = 1;
    public static final int SIGHT = 2;
    
    private static final int GENE_LENGTH = 10;
    private static final int NUM_GENES = 3;
    
    private static final int[] traitValue = new int[NUM_GENES];

    public static void determine(PredatorVehicle v) {
        findGenes(v);
        for (int i = 0; i < NUM_GENES; i++) {
            System.out.println("TraitValue: " + traitValue[i]);
        }
    }

    private static int evaluateGene(Evaluable chromo, int whichGene) {
        byte[] bits = chromo.getDNA();
        int length = bits.length;
        int distance = length / NUM_GENES;
        int start = whichGene * distance;

        int returnMe = 0;

        for (int i = start; i < start + GENE_LENGTH; i++) {
            returnMe += bits[i];
        }

        return returnMe;
    }

    private static void findGenes(Evaluable chromo) {
        for (int i = 0; i < NUM_GENES; i++) {
            traitValue[i] = evaluateGene(chromo, i);
        }
    }

    public static int[] getValue(Evaluable chromo) {
        findGenes(chromo);
        return traitValue;
    }

    @Override
    public String toString() {
        return "Trait Determiner";
    }

}