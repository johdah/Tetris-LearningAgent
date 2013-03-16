package tetris.GA;

import tetris.AITester;
import tetris.players.GeneticAI2;

import java.lang.*;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TetrisProblem implements IProblem {
    private float clears;
    private float nrOfHoles;
    private float bumps;
    private float touchWall;
    private float nonFullLines;
    private float fullLines;
    private float wells;
    private float height;

    private AITester ait;

    private int samples = 2;

    public TetrisProblem() {
        ait = new AITester();
    }

    @Override
    public Individual2 CreateRandomIndividual() {
        String genome = floatToString(GetRandomFloat())
                + floatToString(GetRandomFloat())
                + floatToString(GetRandomFloat())
                + floatToString(GetRandomFloat())
                + floatToString(GetRandomFloat())
                + floatToString(GetRandomFloat())
                + floatToString(GetRandomFloat())
                + floatToString(GetRandomFloat());
        ParseGenome(genome);
        return new Individual2(genome);
    }

    @Override
    public double CalcFitness(Individual2 individual) {
        ParseGenome(individual.GetGene());
        int testscore = 0;
        GeneticAI2 gai;
        try {
            gai = new GeneticAI2(clears, nrOfHoles, bumps, touchWall, nonFullLines, fullLines, wells, height);
            for (int i = 0; i < samples; i++) {
                testscore += ait.GetTestGameScore(gai);  //takes more time, but might be worth it.
            }
            testscore = testscore / samples;

        } catch (InterruptedException ex) {
            Logger.getLogger(TetrisProblem.class.getName()).log(Level.SEVERE, null, ex);
        }

        return testscore;
    }

    @Override
    public String GetInfo(Individual2 individual) {
        String result = "";
        result += "Clears:      " + clears
                + "\nNrOfHoles:  " + nrOfHoles
                + "\nBumps:       " + bumps
                + "\nTouchWall:   " + touchWall
                + "\nNonFullLines:" + nonFullLines
                + "\nFullLines:   " + fullLines
                + "\nWells:       " + wells
                + "\nHeight       " + height;
        return result;
    }

    @Override
    public boolean CriteriaSatisfied(List<Individual2> population) {
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

    public void ParseGenome(String genome) {
        int length = genome.length() / 8;

        clears = stringToFloat(genome.substring(0, length));
        nrOfHoles = stringToFloat(genome.substring(length, 2 * length));
        bumps = stringToFloat(genome.substring(2 * length, 3 * length));
        touchWall = stringToFloat(genome.substring(3 * length, 4 * length));
        nonFullLines = stringToFloat(genome.substring(4 * length, 5 * length));
        fullLines = stringToFloat(genome.substring(5 * length, 6 * length));
        wells = stringToFloat(genome.substring(6 * length, 7 * length));
        height = stringToFloat(genome.substring(7 * length, genome.length()));
    }

    private float GetRandomFloat() {
        Random r = new Random();

        float f = r.nextFloat();
        return f;
    }

}