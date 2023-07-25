import java.io.*;
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

    public static ArrayList<Integer> heuristic4 = new ArrayList<>();
    public static ArrayList<Double> heuristic5 = new ArrayList<>();

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
                System.out.println();
                return 3 * calculateProvided(target, 5);
            case 7:
                return calculateProvided(target, 6);
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
        }
        else if (xStart < xTarget) {
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
                return h4helper(target);
            case 6:
                System.out.println(((3.4572 * Math.abs(xStart - xTarget)) +
                        (2.5421 * Math.abs(yStart - yTarget)) +
                        (-0.8762 *
                                Math.sqrt(Math.pow(Math.abs(target.xPos - this.xPos), 2) + Math.pow(Math.abs(target.yPos - this.yPos), 2))) +
                        5.9052));
                return ((3.4572 * Math.abs(xStart - xTarget)) +
                        (2.5421 * Math.abs(yStart - yTarget)) +
                        (-0.8762 *
                                Math.sqrt(Math.pow(Math.abs(target.xPos - this.xPos), 2) + Math.pow(Math.abs(target.yPos - this.yPos), 2))) +
                        5.9052);
            default:
                return (int) this.timeRemainingEstimate;
        }
    }

    double h4helper(Node target){
        // acquire desired x and y positioning
        int xTarget = target.xPos;
        int yTarget = target.yPos;

        // start x and y pos
        int xStart = this.xPos;
        int yStart = this.yPos;

        // take note of robot positioning at current node
        Direction startDirection = this.robot.robotDirection;
        Direction robotDirection = startDirection;

        if (xStart > xTarget) {
            if (robotDirection != Direction.WEST)
                return Math.max(Math.abs(yStart - yTarget), Math.abs(xStart - xTarget)) + 1;
        }
        else if (xStart < xTarget) {
            if (robotDirection != Direction.EAST)
                return Math.max(Math.abs(yStart - yTarget), Math.abs(xStart - xTarget)) + 1;
        }
        if (yStart > yTarget) {
            if (robotDirection != Direction.NORTH)
                return Math.max(Math.abs(yStart - yTarget), Math.abs(xStart - xTarget)) + 1;
        } else if (yStart < yTarget) {
            if (robotDirection != Direction.SOUTH)
                return Math.max(Math.abs(yStart - yTarget), Math.abs(xStart - xTarget)) + 1;
        }
        return Math.max(Math.abs(yStart - yTarget), Math.abs(xStart - xTarget));
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

    public static List<String> actions = new ArrayList<>();

    public static List<Integer> xDistance = new ArrayList<>();
    public static List<Integer> yDistance = new ArrayList<>();
    public static List<Double> values = new ArrayList<>();

    public static List<Double> hypotenuse = new ArrayList<>();
    public static List<Double> AEstimate = new ArrayList<>();

    public static List<String> dataLines = new ArrayList<>();
    public static List<String> dataLines2 = new ArrayList<>();

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

        actions.add("At the Start");

        for (Node node : nodes) {

            actions = Robot.calculateTurn(node, actions);

            if (node.turn){
                if (actions.get(actions.size()-1).equals("After Turning Around")){
                    actions.remove(actions.size()-1);
                    values.add((double)node.parent.difficulty / 2);
                    xDistance.add(Math.abs(target.xPos - node.parent.xPos));
                    yDistance.add(Math.abs(target.yPos - node.parent.yPos));
                    hypotenuse.add(Math.sqrt(Math.pow(Math.abs(target.xPos - node.xPos), 2) + Math.pow(Math.abs(target.yPos - node.yPos), 2)));
                    heuristic4.add(Math.abs(target.xPos - node.xPos) + Math.abs(target.yPos - node.yPos));
                    AEstimate.add(node.timeTraveled + Math.abs(node.yPos - target.yPos) + Math.abs(node.xPos - target.xPos));
                }
                values.add((double)node.parent.difficulty / 2);
                xDistance.add(Math.abs(target.xPos - node.parent.xPos));
                yDistance.add(Math.abs(target.yPos - node.parent.yPos));
                hypotenuse.add(Math.sqrt(Math.pow(Math.abs(target.xPos - node.xPos), 2) + Math.pow(Math.abs(target.yPos - node.yPos), 2)));
                heuristic4.add(Math.abs(target.xPos - node.xPos) + Math.abs(target.yPos - node.yPos));
                AEstimate.add(node.timeTraveled + Math.abs(node.yPos - target.yPos) + Math.abs(node.xPos - target.xPos));
            }

            if (node.bash) {
                actions.add("After Bash");
                actions.add("After Moving Forward");
                values.add(3.0);

                GameState.getInstance().incrementNumActions(2);

                if (node.robot.robotDirection == Direction.NORTH || node.robot.robotDirection == Direction.SOUTH){
                    yDistance.add(Math.abs(target.yPos - node.yPos) + 1);
                    xDistance.add(Math.abs(target.xPos - node.xPos));
                    hypotenuse.add(Math.sqrt(Math.pow(Math.abs(target.xPos - node.xPos), 2) + Math.pow(Math.abs(target.yPos - node.yPos), 2)));
                    heuristic4.add(Math.abs(target.xPos - node.xPos) + Math.abs(target.yPos - node.yPos));
                    AEstimate.add(node.timeTraveled + Math.abs(node.yPos - target.yPos) + Math.abs(node.xPos - target.xPos));
                }
                if (node.robot.robotDirection == Direction.WEST || node.robot.robotDirection == Direction.EAST) {
                    xDistance.add(Math.abs(target.xPos - node.xPos) + 1);
                    yDistance.add(Math.abs(target.yPos - node.yPos));
                    hypotenuse.add(Math.sqrt(Math.pow(Math.abs(target.xPos - node.xPos), 2) + Math.pow(Math.abs(target.yPos - node.yPos), 2)));
                    heuristic4.add(Math.abs(target.xPos - node.xPos) + Math.abs(target.yPos - node.yPos));
                    AEstimate.add(node.timeTraveled + Math.abs(node.yPos - target.yPos) + Math.abs(node.xPos - target.xPos));
                }
            }

            if(!node.bash && node != n){
                actions.add("After Moving Forward");
                GameState.getInstance().incrementNumActions(1);
            }

            values.add((double)node.difficulty);
            xDistance.add(Math.abs(target.xPos - node.xPos));
            yDistance.add(Math.abs(target.yPos - node.yPos));
            hypotenuse.add(Math.sqrt(Math.pow(Math.abs(target.xPos - node.xPos), 2) + Math.pow(Math.abs(target.yPos - node.yPos), 2)));
            heuristic4.add(Math.abs(target.xPos - node.xPos) + Math.abs(target.yPos - node.yPos));
            AEstimate.add(node.timeTraveled + Math.abs(node.yPos - target.yPos) + Math.abs(node.xPos - target.xPos));
        }
        System.out.println("Starting at the start node, the robot performed these moves: ");
        for (String s : actions) {
            System.out.println(s);
        }

        double costSoFar = 0;
        double sum = sum(values);

        for (int i = 0; i < actions.size(); i++){
            costSoFar += values.get(i);
            dataLines.add(actions.get(i) + "," + xDistance.get(i) + "," +
                    yDistance.get(i) + "," +
                    hypotenuse.get(i) + "," + (sum - costSoFar));
        }

        double branchingF = Math.pow((double)GameState.getNumNodesExpanded(),(1/(double)GameState.getNumActions()));

        dataLines2.add(AStar.board + "," + GameState.getNumNodesExpanded() + "," + GameState.getNumActions() + "," + (100 - target.timeTraveled) + "," + branchingF);

        System.out.println();
        System.out.println("Score: \n" + (100 - target.timeTraveled));
        System.out.println();
        System.out.println("Number of actions performed:");
        System.out.println(GameState.getNumActions());
        System.out.println();
        System.out.println("Number of nodes expanded:");
        System.out.println(GameState.getNumNodesExpanded());
        System.out.println();

        values = new ArrayList<>();
        actions = new ArrayList<>();
        xDistance = new ArrayList<>();
        yDistance = new ArrayList<>();
        heuristic4 = new ArrayList<>();
        heuristic5 = new ArrayList<>();
        hypotenuse = new ArrayList<>();
        AEstimate = new ArrayList<>();

        GameState.numActions = 0;
        GameState.numNodesExpanded = 0;
    }

    public static void writeCSV() throws IOException {
        File csvOutputFile = new File("assignment3Data.csv");
        FileWriter fw = new FileWriter(csvOutputFile);
        BufferedWriter bw = new BufferedWriter(fw);

        bw.write("State,xDistance,yDistance,Heuristic 4,Heuristic 5,hypotenuse,A* Estimate,Cost to Goal");
        bw.newLine();

        for(int i=0;i<dataLines.size();i++)
        {
            bw.write(dataLines.get(i));

            bw.newLine();
        }

        bw.close();
        fw.close();
    }

    public static void writeCSV2(Node target) throws IOException {
        File csvOutputFile = new File("Heuristic7Results.csv");
        FileWriter fw = new FileWriter(csvOutputFile);
        BufferedWriter bw = new BufferedWriter(fw);

        bw.write("Board,Nodes Expanded,Actions,Score,Branching Factor");
        bw.newLine();

        for(int i=0;i<dataLines2.size();i++)
        {
            bw.write(dataLines2.get(i));

            bw.newLine();
        }

        bw.close();
        fw.close();
    }

    static double sum(List<Double> l){
        double s = 0;
        for (double i : l)
            s+=i;
        return s;
    }
}