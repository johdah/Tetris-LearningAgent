package tetris.GA;

import java.lang.String;
import java.util.List;
import tetris.GA.Individual2;

public interface IProblem {
    public abstract Individual2 CreateRandomIndividual();
    public abstract double CalcFitness(Individual2 individual);
    public abstract boolean CriteriaSatisfied(List<Individual2> population);
    public abstract String GetInfo(Individual2 individual);
}