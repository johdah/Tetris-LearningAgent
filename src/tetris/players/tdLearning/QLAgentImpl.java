package tetris.players.tdLearning;

import tetris.players.tdLearning.interfaces.IAction;
import tetris.players.tdLearning.interfaces.IState;

import java.text.DecimalFormat;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Johan
 * Date: 2013-03-14
 * Time: 12:38
 */
public class QLAgentImpl extends AbstractAgent {
    /**
     * Learning parameters
     */
    public double epsilon, alpha, gamma, lambda;

    /**
     * Problem parameters
     */
    private int rows, cols, actions;

    private GridStateImpl prevState;
    private GridActionImpl prevAction;

    /**
     * A 3-dimensional array to hold the current state-action value
     * estimates. The indices of the first and second dimensions
     * correspond to the x and y-coordinates (respectively) of a
     * GridStateImpl. The third-dimension indices correspond to the
     * numerical constants that represent the possible actions from
     * that state, as defined in the GridEnvir class. */
    public double[][][] qEst;

    /**
     * A 3-dimensional array to hold the eligibility trace values. The
     * indices of the first and second dimensions correspond to the x
     * and y-coordinates (respectively) of a GridStateImpl. The
     * third-dimension indices correspond to the numerical constants
     * that represent the possible actions from that state, as defined
     * in the GridEnvir class. */
    protected double[][][] elig;


    /**
     * <code>rng</code> - a random number generator.
     */
    private Random rng = new Random();

    /**
     * A <code>DecimalFormat</code> instance for formatting the state
     * value estimates.  */
    private DecimalFormat df = new DecimalFormat("+00.0000;-00.0000");

    /**
     * Creates a new <code>TDATabQLGrid</code> instance.
     *
     * @param epsilon a <code>double</code> value, the probability
     * that an agent explores; assume agent employs an E-greedy policy.  
     * @param alpha a <code>double</code> value, the learning rate of
     * the agent.
     * @param gamma a <code>double</code> value, the discount rate of
     * learning.  
     * @param lambda a <code>double</code> value, the trace-decay 
     * parameter.  
     */
    public QLAgentImpl(double epsilon, double alpha, double gamma,
                             double lambda){
        this.epsilon = epsilon;
        this.alpha = alpha;
        this.gamma = gamma;
        this.lambda = lambda;
        rows = GridStateImpl.NUM_ROWS;
        cols = GridStateImpl.NUM_COLUMNS;
        actions = GridStateImpl.NUM_ACTIONS;
    }


    /**
     * Initializes an instance of the agent by constructing the
     * necessary data structures for learning (including the data
     * structures for the Q-estimates and eligibility trace values).
     *
     * @param a a <code>Object[]</code> value */
    public void init( Object[] a){
        elig = new double[rows][cols][actions];
        qEst = new double[rows][cols][actions];
    }

    public IAction startTrial(IState state)
    {
        // reset eligibilities
        elig = new double[rows][cols][actions];
        prevState = (GridStateImpl) state;
        prevAction = getAction((GridStateImpl) state);
        return prevAction;
    }

    public IAction step(IState nextState, double reward)
    {
        GridStateImpl state = (GridStateImpl) nextState;
        GridActionImpl action = getAction(state);
        GridActionImpl greedyAction = getGreedyAction(state);
        boolean greedyPicked = false;
        if (qEst[state.y][state.x][action.dir]
                == qEst[state.y][state.x][greedyAction.dir]) {
            greedyAction = action;
            greedyPicked = true;
        }
        double error = reward - qEst[prevState.y][prevState.x][prevAction.dir];
        if (!state.isTerminal())
            error += gamma * qEst[state.y][state.x][greedyAction.dir];
        elig[prevState.y][prevState.x][prevAction.dir]++;
        // or, for replacing traces:
        // elig[prevState.x][prevState.y][prevAction.dir] = 1;
        for (int row = 0; row < rows; row++)
            for (int col = 0; col < cols; col++)
                for (int act = 0; act < actions; act++) {
                    qEst[row][col][act] += alpha * error * elig[row][col][act];
                    if (greedyPicked)
                        elig[row][col][act] *= gamma * lambda;
                    else
                        elig[row][col][act] = 0;
                }
        prevState = state;
        prevAction = action;
        return action;
    }


    /**
     * Return the epsilon-greedy action according to current Q-estimates.
     *
     * @param state a <code>GridStateImpl</code> value
     * @return a <code>GridActionImpl</code> value
     */
    public GridActionImpl getAction(GridStateImpl state)
    {
        if (rng.nextDouble() < epsilon)
            // random exploratory action
            return new GridActionImpl(rng.nextInt(actions));
        else
            // current estimated optimal action
            return getGreedyAction(state);
    }


    /**
     * Return the greedy action according to current Q-estimates.
     *
     * @param state a <code>GridStateImpl</code> value
     * @return a <code>GridActionImpl</code> value
     */
    public GridActionImpl getGreedyAction(GridStateImpl state)
    {
        int bestAct = 0;
        double bestQ = qEst[state.y][state.x][bestAct];
        for (int act = 1; act < actions; act++)
            if (qEst[state.y][state.x][act] > bestQ) {
                bestQ = qEst[state.y][state.x][act];
                bestAct = act;
            }
        return new GridActionImpl(bestAct);
    }


    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int bestAct = 0;
                double bestQ = qEst[row][col][bestAct];
                double avgQ = bestQ;
                for (int act = 1; act < actions; act++) {
                    if (qEst[row][col][act] > bestQ)
                        bestQ = qEst[row][col][act];
                    avgQ += qEst[row][col][act];
                }
                avgQ /= actions;
                double vEst = epsilon * avgQ + (1 - epsilon) * bestQ;
                sb.append(df.format(vEst));
                sb.append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
