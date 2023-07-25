import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class Node implements Comparable<Node> {

    public static Node[][] graph = GraphUtils.getGraph();
    private static int idCounter = 0;
    public int id;
    public int difficulty;
    public boolean bash = false;
    public boolean turnedPreviously = false;
    public Node parent = null;
    public List<Edge> neighbors;
    public double AStarEstimate = Double.MAX_VALUE;
    public double timeTraveled = Double.MAX_VALUE;
    public double timeRemainingEstimate;
    public int xPos;
    public int yPos;
    Robot robot;
    public int turned2Prev;
    public boolean turn;

    Node(double h, int difficulty, int xPos, int yPos, Robot robot) {
        this.timeRemainingEstimate = h;
        this.id = idCounter++; // We may want to implement a different ID system
        this.neighbors = new ArrayList<>();
        this.difficulty = difficulty;
        this.xPos = xPos;
        this.yPos = yPos;
        this.robot = robot;
        this.turned2Prev = 0;
    }

    Node(double h, int difficulty, int xPos, int yPos) {
        this.timeRemainingEstimate = h;
        this.id = idCounter++; // We may want to implement a different ID system
        this.neighbors = new ArrayList<>();
        this.difficulty = difficulty;
        this.xPos = xPos;
        this.yPos = yPos;
        this.turned2Prev = 0;
    }


    @Override
    public int compareTo(Node n) {
        return Double.compare(this.AStarEstimate, n.AStarEstimate);
    }

    public static class Edge {
        Edge(int difficulty, Node node, Direction direction, boolean bash, boolean turn) {
            this.difficulty = difficulty;
            this.node = node;
            this.direction = direction;
            this.bash = bash;
            this.turn = turn;
        }

        public int difficulty;
        public Node node;
        public Direction direction;
        public boolean bash;
        public boolean turn;
    }

    public void addBranch(int difficulty, Node node, Direction direction, boolean bash, boolean turn) {
        Edge newEdge = new Edge(difficulty, node, direction, bash, turn);
        neighbors.add(newEdge);
    }

    public double calculateHeuristic(Node target, int mode) throws Exception {
        switch (mode) {

            case 1:
                // Mode 'zero' where always zero
                return 0;
            case 2:
                // Mode 'min' (vertical, horizontal) that takes the smaller
                return calculateProvided(target, 2);
            case 3:
                // Mode: 'max' (vertical, horizontal) that takes the larger
                return calculateProvided(target, 3);
            case 4:
                // Mode: 'sum' takes vertical + horizontal distance
                return calculateProvided(target, 4);
            case 5:
                // Hypotenuse calculation
                return calculateProvided(target, 5);
            case 6:
                // Mode: 'TBA' that is non-admissible by multiplying 'sum' by 3
                return 3 * calculateProvided(target, 5);
            default:
                return this.timeRemainingEstimate;

        }
    }

    public double calculateProvided(Node target, int mode) throws Exception {
        // acquire desired x and y positioning
        int xTarget = target.xPos;
        int yTarget = target.yPos;

        // start x and y pos
        int xStart = this.xPos;
        int yStart = this.yPos;

        // take note of robot positioning at current node
        Direction startDirection = this.robot.robotDirection;
        Direction robotDirection = startDirection;

        // calculate estimate of horizontal movements only
        int horizontalEstimate = 0;
        int verticalEstimate = 0;
        if (xStart > xTarget) {
            if (robotDirection != Direction.WEST)
                horizontalEstimate++;
            for (int i = xStart; i >= xTarget; i--) {
                horizontalEstimate += graph[yStart][i].difficulty;
            }
        } else if (xStart < xTarget) {
            if (robotDirection != Direction.EAST)
                horizontalEstimate++;
            for (int i = xStart; i <= xTarget; i++) {
                horizontalEstimate += graph[yStart][i].difficulty;
            }
        }
        // calculate estimate of vertical movements only
        if (yStart > yTarget) {
            if (robotDirection != Direction.NORTH)
                verticalEstimate++;
            for (int i = yStart; i >= yTarget; i--) {
                verticalEstimate += graph[i][xStart].difficulty;
            }
        } else if (yStart < yTarget) {
            if (robotDirection != Direction.SOUTH)
                verticalEstimate++;
            for (int i = yStart; i <= yTarget; i++) {
                verticalEstimate += graph[i][xStart].difficulty;
            }
        }

        // return minimum of the two
        switch (mode) {
            case 2:
                return Math.min(Math.abs(yStart - yTarget), Math.abs(xStart - xTarget));
            case 3:
                return Math.max(Math.abs(yStart - yTarget), Math.abs(xStart - xTarget));
            case 4:
                return Math.abs(yStart - yTarget) + Math.abs(xStart - xTarget);
            case 5:
                return Math.sqrt(verticalEstimate ^ 2 + horizontalEstimate ^ 2);
            default:
                return (int) this.timeRemainingEstimate;
        }
    }

    /**
     * aStar takes in a start node and an end node and uses the aStar algorithm to
     * determine the quickest path
     *
     * @param start
     * @param target
     * @return
     */
    public static Node aStar(Node start, Node target, int mode) throws Exception {
        // Priority Queue is just a heap built using priorities.
        PriorityQueue<Node> expanded = new PriorityQueue<>();
        PriorityQueue<Node> toExpand = new PriorityQueue<>();

        start.AStarEstimate = start.timeTraveled + start.calculateHeuristic(target, mode);
        toExpand.add(start);


        int iter = 0;

        while (!toExpand.isEmpty()) {
            Node n = toExpand.peek();
            GameState.getInstance().incrementNodesExpanded();

            if (n == target) {
                return n;
            }

            for (Node.Edge edge : n.neighbors) {
                Node node = edge.node;
                double totalWeight = n.timeTraveled + edge.difficulty;
                boolean bash = edge.bash;

                if (!toExpand.contains(node) && !expanded.contains(node)) {
                    node.parent = n;
                    node.robot = new Robot(edge.direction);
                    node.bash = bash;

                    node.timeTraveled = totalWeight;

                    node.AStarEstimate = node.timeTraveled + node.calculateHeuristic(target, mode);
                    toExpand.add(node);
                } else {
                    if (totalWeight < node.timeTraveled) {
                        node.parent = n;
                        node.robot.robotDirection = edge.direction;
                        node.bash = bash;

                        node.timeTraveled = totalWeight;

                        node.AStarEstimate = node.timeTraveled + node.calculateHeuristic(target, mode);

                        if (expanded.contains(node)) {
                            expanded.remove(node);
                            toExpand.add(node);
                        }
                    }
                }
            }

            toExpand.remove(n);
            expanded.add(n);
        }
        return null;
    }

    public static void printPath(Node target) {

        Node n = target;

        if (n == null)
            return;

        List<Node> nodes = new ArrayList<Node>();

        while (n.parent != null) {
            nodes.add(0, n);
            n = n.parent;
        }
        nodes.add(0, n);

        List<String> actions = new ArrayList<String>();

        for (Node node : nodes) {
            actions = Robot.calculateTurn(node, actions);

            if (node.bash) {
                actions.add("->\tBashed");
                actions.add("->\tMoved Forward");
                GameState.getInstance().incrementNumActions(2);
            }

            if(!node.bash && node != n){
                actions.add("->\tMoved Forward");
                GameState.getInstance().incrementNumActions(1);
            }
        }
        actions.add("->\tReached goal!!");
        System.out.println("Starting at the start node, the robot performed these moves: ");
        for (String s : actions) {
            System.out.println(s);
        }
        System.out.println();
        System.out.println("Score: \n" + (100 - target.timeTraveled));
        System.out.println();
        System.out.println("Number of actions performed:");
        System.out.println(GameState.getNumActions());
        System.out.println();
        System.out.println("Number of nodes expanded:");
        System.out.println(GameState.getNumNodesExpanded());
        System.out.println();
    }
}