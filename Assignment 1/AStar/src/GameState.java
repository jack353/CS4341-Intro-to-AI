import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameState {
    private static GameState instance = null;
    private static int numActions = 0;
    private static int numNodesExpanded = 0;

    /**
     * The constructor for GameState.
     */
    private GameState() {

    }

    /**
     * Getter for instance.
     *
     * @return the current instance of GameState
     */
    public static GameState getInstance() {
        if (instance == null) {
            instance = new GameState();
        }
        return instance;
    }

    public static void setInstance() {
        instance = null;
    }

    public void incrementNumActions(int x) {
        numActions += x;
    }

    public void incrementNodesExpanded() {
        numNodesExpanded++;
    }

    public static int getNumActions() {
        return numActions;
    }

    public static int getNumNodesExpanded() {
        return numNodesExpanded;
    }

}

