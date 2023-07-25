import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AStar {

    public static void main(String[] args) {
        // Main code block initiating execution
        String file = args[0];
        int heuristic = Integer.parseInt(args[1]);

        try {
            GraphUtils.makeGraph(file, heuristic);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
