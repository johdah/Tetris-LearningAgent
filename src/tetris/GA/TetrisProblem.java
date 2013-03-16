package tetris.GA;

import tetris.AITester;
import tetris.players.GeneticAI2;

import java.lang.*;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TetrisProblem implements IProblem {

    float _clears;
    float _nrOfHoles;
    float _bumps;
    float _wallTouches;
    float _nonFullLines;
    float _fullLines;
    float _wells;
    float _height;
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
            GeneticAI2 gai = new GeneticAI2(_clears, _nrOfHoles, _bumps, _wallTouches, _nonFullLines, _fullLines, _wells, _height);
            for (int i = 0; i < samples; i++) {
                testscore = testscore + ait.GetTestGameScore(gai);  //takes more time, but might be worth it
            }
            testscore = testscore / samples;

        } catch (InterruptedException ex) {
            Logger.getLogger(TetrisProblem.class.getName()).log(Level.SEVERE, null, ex);
        }

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

        _clears = stringToFloat(genome.substring(0, length));
        _nrOfHoles = stringToFloat(genome.substring(length, 2 * length));
        _bumps = stringToFloat(genome.substring(2 * length, 3 * length));
        _wallTouches = stringToFloat(genome.substring(3 * length, 4 * length));
        _nonFullLines = stringToFloat(genome.substring(4 * length, 5 * length));
        _fullLines = stringToFloat(genome.substring(5 * length, 6 * length));
        _wells = stringToFloat(genome.substring(6 * length, 7 * length));
        _height = stringToFloat(genome.substring(7 * length, genome.length()));
    }

    @Override
    public String getInfo(Individual2 individual) {
        String s = "";
        s += "Clears:      " + _clears
                + "\nNrOfWholes:  " + _nrOfHoles
                + "\nBumps:       " + _bumps
                + "\nTouchWall:   " + _wallTouches
                + "\nNonFullLines:" + _nonFullLines
                + "\nFullLines:   " + _fullLines
                + "\nWells:       " + _wells
                + "\nHeight:       " + _height;
        return s;
    }

    private float getFloat() {
        Random r = new Random();

        return r.nextFloat();
    }

}