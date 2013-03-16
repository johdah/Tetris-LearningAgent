/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris;

import tetris.players.AbstractAI;

public class AITester {
    public int GetTestGameScore(AbstractAI ai) throws InterruptedException{
        Game game = new Game(ai);
  
        int score = game.testAI();
        game.quit();

        return score;
    }
}
