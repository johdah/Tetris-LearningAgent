package tetris.GA;

import java.util.List;
import java.util.Random;

public class MathProblem implements IProblem {

    @Override
    public Individual2 createRandomIndividual() {
        return new Individual2(floatToString(new Random().nextFloat()));
    }

    @Override
    public double calcFitness(Individual2 individual) {
        float x = stringToFloat(individual.getGene());

        //x^4 - x^3 – 4x^2 + 2x+5
        return (Math.pow(x, 4) - Math.pow(x, 3) - 4 * Math.pow(x, 2) + 2 * x + 5);
    }

    @Override
    public boolean criteriaSatisfied(List<Individual2> population) {
        return false;
    }

    @Override
    public String getInfo(Individual2 individual) {
        return "X=";
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


}
