package tetris.GA;

import java.lang.String;
import java.util.List;

public interface IProblem {
	public abstract Individual2 createRandomIndividual();
	public abstract double calcFitness(Individual2 individual);
	public abstract boolean criteriaSatisfied(List<Individual2> population);
        public abstract String getInfo(Individual2 individual);
}