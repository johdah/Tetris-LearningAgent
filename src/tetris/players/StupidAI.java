/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris.players;

import tetris.players.AbstractAI;

import java.awt.Color;

/**
 *
 * @author RIK
 */
public class StupidAI extends AbstractAI {
    public double rateBoard(Color[][] board) {
        int maxHeight = getMaxHeight(board);
        int nrOfWholes = getHoles(board, maxHeight);
        int nrOfNonFullLines = getNonFullLines(board, maxHeight);
        return nrOfWholes*7 + maxHeight * 10 + nrOfNonFullLines *10;
    }

    public int getMaxHeight(Color[][] board) {
        int height = board.length;
        int maxHeight = 0;
        for (int w = 0; w < board[0].length; w++) {
            for (int h = 0; h < board.length; h++) {
                if (board[h][w] != null) {
                    if (height - h > maxHeight) {
                        maxHeight = height - h;
                    }
                    break;
                }
            }
        }
        return maxHeight;
    }

    public int getHoles(Color[][] board, int maxHeight) {
        int nrOfWholes = 0;
        for (int h = board.length - maxHeight; h < board.length; h++) {
            for (int w = 0; w < board[0].length && h>0; w++) {
                if (board[h][w] == null){//&& board[h-1][w]!=null) {
                    for(int y=h-1; y>=0;y--){
                        if(board[y][w]!=null){
                         nrOfWholes++;
                         break;
                        }
                    }
                    
                }
                    //nrOfWholes += 1;//Math.pow((h - (board.length - maxHeight) + 1),2);
                
            }
        }

        return nrOfWholes;
    }

    public int getNonFullLines(Color[][] board, int maxHeight) {
        int nrOfLines = 0;
        for (int h = board.length - maxHeight; h < board.length; h++) {
            for (int w = 0; w < board[0].length; w++) {
                if (board[h][w] == null) {
                    nrOfLines++;
                    break;
                }
            }
        }

        return nrOfLines;
    }
}
