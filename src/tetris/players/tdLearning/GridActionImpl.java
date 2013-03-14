package tetris.players.tdLearning;

import tetris.players.tdLearning.interfaces.IAction;

/**
 * GridAction.java
 *  This class creates <code>GridAction</code> objects to represent
 *  the possible moves (actions) an agent can select in a Grid World game.
 *
 * User: Johan
 * Date: 2013-03-14
 * Time: 11:19
 */
public class GridActionImpl implements IAction {

    /**
     * An integer representation of a <code>GridAction</code>
     * instance; the <code>GridState</code> class provides constants
     * to intepret the value of dir.  */
    public int dir;


    /**
     * Creates a new <code>GridAction</code> instance to represent
     * a move in a Grid World game.
     *
     * @param direction, an <code>int</code> value representing the
     * direction of the move (action) */
    public GridActionImpl (int direction){
        dir = direction;
    }

    public String toString()
    {
        switch (dir) {
            case GridStateImpl.NORTH:
                return "N";
            case GridStateImpl.SOUTH:
                return "S";
            case GridStateImpl.EAST:
                return "E";
            case GridStateImpl.WEST:
                return "W";
            default:
                return "?";
        }
    }

}
