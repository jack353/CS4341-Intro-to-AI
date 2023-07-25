import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ga {

    //setup to take proper command line arguments
    // puzzle#, filename, #seconds

    public static final float[] input = new float[40];

    public static final ArrayList<TowerPiece> towerInput = new ArrayList<>();

    public static void main(String[] args) {

        int puzzle = Integer.parseInt(args[0]);

        if(args.length != 3){
            System.out.println("An error occurred.");
            System.out.println("Invalid number of input.");
            System.out.println("Please input a puzzle #, filename, and # of seconds.");
        } else if(puzzle != 1 && puzzle != 2) {
            System.out.println("An error occurred.");
            System.out.println("Invalid puzzle input.");
            System.out.println("Please input puzzle number 1 or 2.");
        }

        int time = Integer.parseInt(args[2]);

        if (puzzle == 1)
            loadPuzzleOne(args[1], time);
        else
            loadPuzzleTwo(args[1], time);
    }

    static void solveP1(int time){
        long startTime = System.nanoTime();
            BinPopulation p = new BinPopulation(20);
            p.generateIndividuals();
            while((System.nanoTime() - startTime) / 1000000000 < time){
                p.findFitnesses();
                p.sortIndividuals();
                p.cull();
                p.getProbabilites();
                p.haveKids();
            }
            p.printBest();

        long endTime = System.nanoTime();
        long elapsedTime = (endTime - startTime) / 1000000000;
        System.out.println(elapsedTime);
    }

    static void loadPuzzleOne(String f, int time){
        int counter = 0;

        File myObj = new File(f);
        Scanner myReader = null;

        try {
            myReader = new Scanner(myObj);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();

            float num = Float.parseFloat(data);

            input[counter] = num;
            counter ++;
        }
        myReader.close();

        solveP1(time);
    }

    static void loadPuzzleTwo(String f, int time){
        File myObj = new File(f);
        Scanner myReader = null;

        try {
            myReader = new Scanner(myObj);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            String[] dataSplit = data.split("\t");
            TowerPiece p = new TowerPiece(dataSplit[0], Integer.valueOf(dataSplit[1]), Integer.valueOf(dataSplit[2]), Integer.valueOf(dataSplit[3]));
            towerInput.add(p);
        }
        myReader.close();

        solveP2(time);
    }

    static void solveP2(int time){
        long startTime = System.nanoTime();
        TowerPopulation p = new TowerPopulation(20);
        p.generateIndividuals();
        while((System.nanoTime() - startTime) / 1000000000 < time){
            p.findFitnesses();
            p.sortIndividuals();
            p.cull();
            p.getProbabilites();
            p.haveKids();
        }
        p.printBest();
    }
}
