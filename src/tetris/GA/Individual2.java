package tetris.GA;

import java.lang.Double;
import java.lang.String;

public class Individual2 {
    private String gene;
    private double fitness = Double.MAX_VALUE;
    private String info;

    public void setInfo(String info) {
        this.info = info;
    }

	public Individual2(String gene) {
		this.gene = gene;
	}

	public String getGene() {
		return gene;
	}
	
	public double getFitness(){
		return fitness;
	}

	void setFitness(double fitness) {
		this.fitness = fitness;
	}

    String getInfo() {
        return info;
    }
}