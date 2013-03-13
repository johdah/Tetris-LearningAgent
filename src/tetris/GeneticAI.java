/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris;

import java.awt.Color;

/**
 *
 * @author Fredrik
 */
public class GeneticAI extends TetrisAI {

    //Weights are good, I'm sure of it
    private double _weight1 = 0;
    
    public GeneticAI(double weight1){
        _weight1 = weight1;
    }
        
    @Override
    public double rateBoard(Color[][] board) {         
    
        return 1;
    }

}
