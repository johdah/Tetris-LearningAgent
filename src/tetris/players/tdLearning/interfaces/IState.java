package tetris.players.tdLearning.interfaces;

/**
 * State - An interface for implementing the input (i.e. sensory
 * information) that an agent receives from the environment.
 *
 * User: Johan
 * Date: 2013-03-14
 * Time: 10:55
 */
public interface IState {
    /** whether or not the state is a terminal state */
    boolean isTerminal();
}
