import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class AStar {

    public static String board;

    public static void main(String[] args) {
        // Main code block initiating execution

        for (int i = 1; i < 11; i++) {
            String file = "boards/board" + i + ".txt";

            board = "board" + i;

            int heuristic = Integer.parseInt(args[1]);

            try {
                GraphUtils.makeGraph(file, heuristic);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}