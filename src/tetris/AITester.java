/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris;

/**
 *
 * @author Fredrik
 */
public class AITester {
    public int GetTestGameScore(TetrisAI ai) throws InterruptedException{     
        Game game = new Game(ai);
  
        int score = game.GetTestAIScore();
        game.quit();

        return score;
    }
}
