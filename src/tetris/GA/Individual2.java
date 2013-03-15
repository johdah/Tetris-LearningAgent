package tetris.GA;

import java.lang.Double;
import java.lang.String;

public class Individual2 {
    private String _info;
    private String _gene;
    private double _fitness = Double.MAX_VALUE;

    public Individual2(String gene) {
        _gene = gene;
    }
    public String getGene() {
        return _gene;
    }

    public double getFitness(){
        return _fitness;
    }

    void setFitness(double fitness) {
        _fitness = fitness;
    }

    public String getInfo() {
        return _info;
    }
    public void setInfo(String info) {
        _info = info;
    }
}