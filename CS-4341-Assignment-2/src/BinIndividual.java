import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class BinIndividual {

    public float fitness = 0;
    public int gen;
    double cumProb = 0;

    ArrayList<BinGene> bins = new ArrayList<>();

    public BinIndividual(int g){
        gen = g;
        bins.add(new BinGene());
        bins.add(new BinGene());
        bins.add(new BinGene());
        bins.add(new BinGene());
        fillBins(40);
    }

    public BinIndividual(ArrayList<BinGene> g){
        bins = g;
        findFitness();
    }

    void fillBins(int size){
        ArrayList<Integer> numbers = new ArrayList<>();
        Random randomGenerator = new Random();
        while (numbers.size() < size) {
            int random = randomGenerator.nextInt(size);
            if (!numbers.contains(random)) {
                numbers.add(random);
                bins.get((int) Math.floor((numbers.size() - 1) / 10)).add(ga.input[random]);
            }
        }
    }

    void findFitness(){
        fitness = calcBin1() + calcBin2() + calcBin3();
    }

    float calcBin1(){
        float total = 1;
        for (int i = 0; i < 10; i++){
            total = total * bins.get(0).get(i);
        }
        return total;
    }

    float calcBin2(){
        float total = 0;
        for (int i = 0; i < 10; i++){
            total = total + bins.get(1).get(i);
        }
        return total;
    }

    float calcBin3(){
        float total = Collections.max(bins.get(2).bin) - Collections.min(bins.get(2).bin);
        return(total);
    }

    void setCumProb(float n){
        cumProb = n;
    }

    void setFitness(float f){
        fitness = f;
    }

}