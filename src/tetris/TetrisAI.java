/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tetris;

import java.awt.Color;
import tetris.Figure;

/**
 *
 * @author RIK
 */
public abstract class TetrisAI {
    
    abstract double rateBoard(Color[][] board);
    
        public void makeBestMove(Color[][] board, Figure figure) {
        if (figure == null) {
            return;
        }
        figure.saveState();
        figure.unMark();
        int b_rot = 0, b_x = 0, b_y = 0;
        double minScore = 1000000;

        for (int rot = 0; rot < 4; rot++) {
            for (int x = 0; x < board.length; x++) {
                if (figure.canMoveTo(x, figure.yPos, rot)) {

                    figure.xPos = x;
                    figure.orientation = rot;

                    while (figure.canMoveTo(x, figure.yPos + 1, rot)) {
                        figure.yPos++;
                    }
                    double score = rateFigure(figure, board);
                    if (score < minScore) {
                        minScore = score;
                        b_x = figure.xPos;
                        b_rot = figure.orientation;
                        b_y = figure.yPos;
                    }
                    if (figure.canMoveTo(x + 1, figure.yPos, rot)) {
                        figure.xPos++;
                        score = rateFigure(figure, board);
                        if (score < minScore) {
                            minScore = score;
                            b_x = figure.xPos;
                            b_rot = figure.orientation;
                            b_y = figure.yPos;
                        }
                        figure.xPos--;
                    }
                    if (figure.canMoveTo(x - 1, figure.yPos, rot)) {
                        figure.xPos--;
                        score = rateFigure(figure, board);
                        if (score < minScore) {
                            minScore = score;
                            b_x = figure.xPos;
                            b_rot = figure.orientation;
                            b_y = figure.yPos;
                        }
                        figure.xPos++;
                    }
                    
                    figure.undo();
                }
            }
        }
        System.out.println(" Score:" + minScore);
        figure.undo();
        figure.xPos = b_x;
        figure.orientation = b_rot;
        figure.yPos = b_y;
        figure.mark();
    }
        
            private void printBoard(Color[][] b) {
        System.out.println("");
        for (int h = 0; h < b.length; h++) {
            for (int w = 0; w < b[0].length; w++) {
                if (b[h][w] == null) {
                    System.out.print("0 ");
                } else {
                    System.out.print("X ");
                }

            }
            System.out.println("");

        }
    }

    public double rateFigure(Figure figure,Color[][] board){
           double score = Double.MAX_VALUE;
           figure.mark();
           if (figure.isAllVisible()) 
                 score = rateBoard(board);
           figure.unMark();
           return score;
    }
}
