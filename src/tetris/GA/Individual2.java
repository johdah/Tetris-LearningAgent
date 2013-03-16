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
    public String GetGene() {
        return _gene;
    }

    public double GetFitness(){
        return _fitness;
    }

    void SetFitness(double fitness) {
        _fitness = fitness;
    }

    public String GetInfo() {
        return _info;
    }
    public void SetInfo(String info) {
        _info = info;
    }
}