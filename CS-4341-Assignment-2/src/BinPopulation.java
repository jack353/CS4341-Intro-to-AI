import java.util.ArrayList;
import java.util.Hashtable;

public class BinPopulation {
    int size;
    int generation;

    ArrayList<BinIndividual> individuals = new ArrayList<>();
    ArrayList<BinIndividual> children = new ArrayList<>();

    public BinPopulation(int s){
        size = s;
    }

    public void generateIndividuals() {
        for (int i = 0; i < size; i++) {
            individuals.add(new BinIndividual(generation));
        }
        generation = 1;
    }

    public void findFitnesses(){
        for (int i = 0; i < individuals.size(); i++)
            individuals.get(i).findFitness();
    }

    public  void cull(){
        int num = (int)Math.floor(individuals.size()/4);
        for (int i = 0; i < num; i++){
            individuals.remove(0);
        }
    }

    public void sortIndividuals() {
        // Outer loop
        for (int i = 0; i < individuals.size(); i++) {
            // Inner nested loop pointing 1 index ahead
            for (int j = i + 1; j < individuals.size(); j++) {

                // Checking elements
                BinIndividual temp;
                if (individuals.get(j).fitness < individuals.get(i).fitness) {

                    // Swapping
                    temp = individuals.get(i);
                    individuals.set(i, individuals.get(j));
                    individuals.set(j, temp);
                }
            }
        }
    }

    public void getProbabilites(){
        float cumulative = 0;
        scaleFitness();
        float sum = getFitSum();
        for (int i = 0; i < individuals.size(); i++){
            cumulative += (individuals.get(i).fitness/sum);
            individuals.get(i).setCumProb(cumulative);
        }
    }

    private float getFitSum(){
        float sum = 0;
        for (BinIndividual i : individuals){
            sum += i.fitness;
        }
        return sum;
    }

    void scaleFitness(){
        if (individuals.get(0).fitness < 0)
            addSmallest();
    }

    void addSmallest(){
        float add = Math.abs(individuals.get(0).fitness);
        for (BinIndividual i : individuals){
            i.setFitness(i.fitness + add);
        }
    }

    void haveKids(){
        generation+=1;
        addFittestTwo();

        while(children.size() < size){
            addTwoChildren();
        }
        individuals = children;
        children = new ArrayList<>();
    }

    void addFittestTwo() {
        children.add(individuals.get(individuals.size()-1));
        children.add(individuals.get(individuals.size()-2));
    }

    void addTwoChildren(){
        BinIndividual p1 = getParent();
        BinIndividual p2 = getParent();



        BinIndividual c1 = new BinIndividual(generation);
        BinIndividual c2 = new BinIndividual(generation);

        children.add(c1);
        children.add(c2);

        addBin(p1, p2, 0);
        addBin(p1, p2, 1);
        addBin(p1, p2, 2);
        addBin(p1, p2, 3);

        mutate(children.get(children.size()-2), children.get(children.size()-1));
    }

    BinIndividual getParent(){
        double p = Math.random();

        for (BinIndividual i : individuals){
            if (i.cumProb > p) {
                return (i);
            }
        }
        return(getParent());
    }

    void addBin(BinIndividual p1, BinIndividual p2, int bin){
        ArrayList<Float> c1B = new ArrayList<>();
        ArrayList<Float> c2B = new ArrayList<>();

        ArrayList<Float> p1B = p1.bins.get(bin).bin;
        ArrayList<Float> p2B = p2.bins.get(bin).bin;

        c1B.addAll(p1B.subList(0, p1B.size()/2));
        c1B.addAll(p2B.subList(p2B.size()/2, p2B.size()));

        children.get(children.size()-2).bins.get(bin).bin = c1B;

        c2B.addAll(p2B.subList(0, p2B.size()/2));
        c2B.addAll(p1B.subList(p1B.size()/2, p1B.size()));

        children.get(children.size()-1).bins.get(bin).bin = c2B;
    }

    void mutate(BinIndividual c1, BinIndividual c2){
        ArrayList<Float> c1Dupes = getDuplicate(c1);
        ArrayList<Float> c2Dupes = getDuplicate(c2);

        ArrayList<Float> c1Found = new ArrayList<>();
        ArrayList<Float> c2Found = new ArrayList<>();

        for (int bin = 0; bin < c1.bins.size(); bin++){
            for (int i = 0; i < c1.bins.get(bin).bin.size(); i++){
                if (c1Found.contains(c1.bins.get(bin).bin.get(i)))
                    children.get(children.size()-2).bins.get(bin).setValue(i,c2Dupes.remove(0));
                else
                    c1Found.add(c1.bins.get(bin).bin.get(i));
            }
        }

        for (int bin = 0; bin < c2.bins.size(); bin++){
            for (int i = 0; i < c2.bins.get(bin).bin.size(); i++){
                if (c2Found.contains(c2.bins.get(bin).bin.get(i)))
                    children.get(children.size()-1).bins.get(bin).setValue(i,c1Dupes.remove(0));
                else
                    c2Found.add(c2.bins.get(bin).bin.get(i));
            }
        }

    }

    ArrayList<Float> getDuplicate(BinIndividual c){
        ArrayList<Float> found = new ArrayList<>();
        ArrayList<Float> dups = new ArrayList<>();
        for (BinGene bg : c.bins){
            for (float n : bg.bin){
                if (found.contains(n))
                    dups.add(n);
                else
                    found.add(n);
            }
        }
        return(dups);
    }

    void printBest(){
        sortIndividuals();
        System.out.println("Best Score: " + individuals.get(individuals.size()-1).fitness + "\n");
        System.out.println("Bin 1: " + individuals.get(individuals.size()-1).bins.get(0).bin.toString());
        System.out.println("Bin 2: " + individuals.get(individuals.size()-1).bins.get(1).bin.toString());
        System.out.println("Bin 3: " + individuals.get(individuals.size()-1).bins.get(2).bin.toString());
        System.out.println("Bin 4: " + individuals.get(individuals.size()-1).bins.get(3).bin.toString() + "\n");
        System.out.println("Ran for " + generation + " generations");
        System.out.println("Best individual's generation: " + individuals.get(individuals.size()-1).gen);
    }
}