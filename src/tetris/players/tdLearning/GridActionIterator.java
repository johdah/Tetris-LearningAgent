package tetris.players.tdLearning;

import java.util.Iterator;
import java.util.Vector;

/**
 * GridActionIterator.java
 * A class for creating and using an iterator that returns all possible
 *  actions for an agent for the Grid World reinforcement learning
 *  problem.  The default implementations of the methods assume that
 *  from any Grid World state, every action is possible and that the
 *  <code>GridActionImpl</code> instances are represented/constructed using
 *  integers ranging from 0,...,GridEnvir.NUM_ACTIONS-1; the meanings of these
 *  integers are assumed to be defined by the GridEnvir class.
 *
 * User: Johan
 * Date: 2013-03-14
 * Time: 12:43
 */
public class GridActionIterator implements Iterator {
    /**
     * A vector containing the possible GridActions the agent can take
     * from an arbitrary state.  */
    Vector acts = new Vector();


    /**
     * The index of the element in the vector that should be returned
     * on the next call to next().  */
    int index = 0;


    /**
     * Creates a new <code>GridActIterator</code> instance.
     *
     * @param numActions an <code>int</code> value, the number of
     * moves possible from each Grid World state.  */
    public GridActionIterator(int numActions){
        for(int actInt = 0; actInt < numActions; actInt++)
            acts.add(new GridActionImpl(actInt));
    }


    /**
     * Returns true if the iterator has more elements.
     *
     * @return a <code>boolean</code> value */
    public boolean hasNext(){
        if(acts.size() == 0 || index == acts.size())
            return false;
        else
            return true;
    }


    /**
     * Returns the next element in the iterator.  Cannot be called if
     * hasNext() == false;
     *
     * @return an <code>Object</code> value, a <code>GridAction</code>
     * instance. */
    public Object next(){
        index++;
        return acts.elementAt(index - 1);

    }

    /**
     * Removes from the underlying collection the last element
     * returned by the iterator.  This method can be called only once
     * per call to next(); it must be called immediately AFTER the
     * call to next(). */
    public void remove(){
        if(acts.size() > 0){
            index--;
            acts.remove(index);
        }
    }
}
