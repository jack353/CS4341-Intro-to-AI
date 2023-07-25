import java.util.List;

public class Robot {
    Direction robotDirection;

    public Robot(Direction robotDirection) {
        this.robotDirection = robotDirection;
    }

    /**
     * Function used to update the value of the bashPerformed variable. When a bash move is performed,
     * the robot's next move must be a forward move.
     *
     * @param bashed2Prev a boolean representing the new value of the bashPerformed variable
     */

    /**
     * Getter for bashPerformed.
     *
     * @return a boolean value representing the state of the bashPerformed variable
     */

    public static List<String> calculateTurn(Node node, List<String> actions) {
        if (node.parent != null && node.robot.robotDirection != node.parent.robot.robotDirection) {
            if (node.parent.robot.robotDirection == Direction.NORTH && node.robot.robotDirection == Direction.WEST ||
                    node.parent.robot.robotDirection == Direction.WEST && node.robot.robotDirection == Direction.SOUTH ||
                    node.parent.robot.robotDirection == Direction.SOUTH && node.robot.robotDirection == Direction.EAST ||
                    node.parent.robot.robotDirection == Direction.EAST && node.robot.robotDirection == Direction.NORTH) {
                GameState.getInstance().incrementNumActions(1);
                actions.add("After Left Turn");
                node.turn = true;
                return actions;

            } else if (node.parent.robot.robotDirection == Direction.NORTH && node.robot.robotDirection == Direction.EAST ||
                    node.parent.robot.robotDirection == Direction.WEST && node.robot.robotDirection == Direction.NORTH ||
                    node.parent.robot.robotDirection == Direction.SOUTH && node.robot.robotDirection == Direction.WEST ||
                    node.parent.robot.robotDirection == Direction.EAST && node.robot.robotDirection == Direction.SOUTH) {
                GameState.getInstance().incrementNumActions(1);
                actions.add("After Right Turn");
                node.turn = true;
                return actions;
            } else{
                actions.add("After Right Turn");
                actions.add("After Right Turn");
                actions.add("After Turning Around");
                node.turn = true;
                return actions;
            }

        }
        node.turn = false;
        return actions;
    }
}
