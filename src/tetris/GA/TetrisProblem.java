package tetris.GA;

import tetris.AITester;
import tetris.players.GeneticAI2;

import java.lang.*;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TetrisProblem implements IProblem {

    float weight1;
    float weight2;
    float weight3;
    float weight4;
    float weight5;
    float weight6;
    float weight7;
    float weight8;
    AITester ait;
    int samples = 2;

    public TetrisProblem() {
        ait = new AITester();
    }

    @Override
    public Individual2 createRandomIndividual() {
        String genome = floatToString(getFloat())
                + floatToString(getFloat())
                + floatToString(getFloat())
                + floatToString(getFloat())
                + floatToString(getFloat())
                + floatToString(getFloat())
                + floatToString(getFloat())
                + floatToString(getFloat());
        parseGenome(genome, 8);
        return new Individual2(genome);
    }

    @Override
    public double calcFitness(Individual2 individual) {
        parseGenome(individual.getGene(), 8);
        int testscore = 0;
        try {
            GeneticAI2 gai = new GeneticAI2(weight1, weight2, weight3, weight4, weight5, weight6, weight7, weight8);
            for (int i = 0; i < samples; i++) {
                testscore = testscore + ait.GetTestGameScore(gai);  //tar givetvis mer tid men jag tror det kan vara värt det. kanske. 
            }
            testscore = testscore / samples;

        } catch (InterruptedException ex) {
            Logger.getLogger(TetrisProblem.class.getName()).log(Level.SEVERE, null, ex);
        }
        //  return Integer.MAX_VALUE - testscore;
        return testscore * -1;
    }

    @Override
    public boolean criteriaSatisfied(List<Individual2> population) {
        return false;
    }

    public static float stringToFloat(String binaryString) {

        String absolute_number = binaryString.substring(1, binaryString.length());
        String oper = binaryString.substring(0, 1);
        float operInt;
        if (oper.equals("1")) {
            operInt = -1;
        } else {
            operInt = 1;
        }
        int intBits = Integer.parseInt(absolute_number, 2);
        return Float.intBitsToFloat(intBits) * operInt;
    }

    public static String floatToString(float number) {
        String operString;
        String binaryString;
        if (number < 0) {
            operString = "1";
            number = number * -1;
            binaryString = Integer.toBinaryString(Float.floatToIntBits(number));
        } else {
            operString = "0";
            binaryString = Integer.toBinaryString(Float.floatToIntBits(number));

        }
        return operString + binaryString;
    }

    public void parseGenome(String genome, int numberOfGenes) {
        int length = genome.length() / numberOfGenes;
        String var1 = genome.substring(0, length);
        String var2 = genome.substring(length, 2 * length);
        String var3 = genome.substring(2 * length, 3 * length);
        String var4 = genome.substring(3 * length, 4 * length);
        String var5 = genome.substring(4 * length, 5 * length);
        String var6 = genome.substring(5 * length, 6 * length);
        String var7 = genome.substring(6 * length, 7 * length);
        String var8 = genome.substring(7 * length, genome.length());


        weight1 = stringToFloat(var1);
        weight2 = stringToFloat(var2);
        weight3 = stringToFloat(var3);
        weight4 = stringToFloat(var4);
        weight5 = stringToFloat(var5);
        weight6 = stringToFloat(var6);
        weight7 = stringToFloat(var7);
        weight8 = stringToFloat(var8);
    }

    @Override
    public String getInfo(Individual2 individual) {
        String s = "";
        s += "Clears:      " + weight1
                + "\nNrOfWholes:  " + weight2
                + "\nBumps:       " + weight3
                + "\nTouchWall:   " + weight4
                + "\nNonFullLines:" + weight5
                + "\nFullLines:   " + weight6
                + "\nWells:       " + weight7
                + "\nHeight       " + weight8;
        return s;
    }

    private float getFloat() {
        Random r = new Random();

        return r.nextFloat();
    }

}