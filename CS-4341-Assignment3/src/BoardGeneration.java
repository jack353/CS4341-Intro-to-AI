import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class BoardGeneration {

    public static void main(String[] args) throws Exception {
        int width = 290;
        int height = 290;

        int startX = 0;
        int startY = 0;
        int endX = width - 1;
        int endY = height - 1;

        ArrayList<String> boards = new ArrayList<>();

        for (int i = 1; i < 601; i++)
            boards.add("board" + i + ".txt");

        for(String s : boards) {
            System.out.println(s);
            StringBuilder out = new StringBuilder();

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (x == startX && y == startY) {
                        out.append('S').append('\t');
                    } else if (x == endX && y == endY) {
                        out.append('G').append('\t');
                    } else {
                        int num = ThreadLocalRandom.current().nextInt(1, 9);
                        out.append(num).append('\t');
                    }
                }

                out.append('\n');
            }

            Files.write(Paths.get("boards/" + s), out.toString().getBytes(StandardCharsets.UTF_8));

        }
    }
}
