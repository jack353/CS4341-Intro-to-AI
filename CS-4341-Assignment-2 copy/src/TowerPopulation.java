import java.util.ArrayList;
import java.util.Random;

public class TowerPopulation {
    int size;
    ArrayList<TowerIndividual> individuals = new ArrayList<>();
    ArrayList<TowerIndividual> children = new ArrayList<>();

    public TowerPopulation(int s){
        size = s;
    }

    public void generateIndividuals() {
        for (int i = 0; i < size; i++) {
            individuals.add(new TowerIndividual());
        }
        checkZero();
    }

    void checkZero(){
        findFitnesses();
        sortIndividuals();
        if (individuals.get(individuals.size()-1).fitness == 0){
            individuals = new ArrayList<>();
            generateIndividuals();
        }
    }

    public void findFitnesses(){
        for (int i = 0; i < individuals.size(); i++)
            individuals.get(i).calcFitness();
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
                TowerIndividual temp;
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
        float sum = getFitSum();
        for (int i = 0; i < individuals.size(); i++){
            cumulative += (individuals.get(i).fitness/sum);
            individuals.get(i).setCumProb(cumulative);
        }
    }

    private float getFitSum(){
        float sum = 0;
        for (TowerIndividual i : individuals){
            sum += i.fitness;
        }
        return sum;
    }

    void haveKids(){
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
        TowerIndividual p1 = getParent();
        TowerIndividual p2 = getParent();

        TowerIndividual c1 = new TowerIndividual();
        TowerIndividual c2 = new TowerIndividual();

        children.add(c1);
        children.add(c2);

        crossOver(p1, p2);

        mutate(children.get(children.size()-2), children.get(children.size()-1));
    }

    TowerIndividual getParent(){
        double p = Math.random();

        for (TowerIndividual i : individuals){
            if (i.cumProb > p) {
                return (i);
            }
        }
        return(null);
    }

    void crossOver(TowerIndividual p1, TowerIndividual p2){
        Random rg = new Random();
        int p1M = rg.nextInt(p1.tower.size());
        int p2M = rg.nextInt(p2.tower.size());

        ArrayList<TowerPiece> c1 = new ArrayList<>();
        c1.addAll(p1.tower.subList(0, p1M));
        c1.addAll(p2.tower.subList(p2M, p2.tower.size()));

        children.get(children.size()-2).tower = c1;

        ArrayList<TowerPiece> c2 = new ArrayList<>();
        c2.addAll(p2.tower.subList(0, p2M));
        c2.addAll(p1.tower.subList(p1M, p1.tower.size()));

        children.get(children.size()-1).tower = c2;
    }

    void mutate(TowerIndividual c1, TowerIndividual c2){
        if (c1.tower.size() > ga.towerInput.size() || c2.tower.size() > ga.towerInput.size())
            mutateDel(c1, c2);
        else
            mutateSwap(c1, c2);
    }

    void mutateDel(TowerIndividual c1, TowerIndividual c2){
        ArrayList<TowerPiece> c1Found = new ArrayList<>();
        ArrayList<TowerPiece> c2Found = new ArrayList<>();

        for (int i = 0; i < c1.tower.size(); i++){
            if (c1Found.contains(c1.tower.get(i)))
                children.get(children.size()-2).tower.remove(i);
            else
                c1Found.add(c1.tower.get(i));
        }

        for (int i = 0; i < c2.tower.size(); i++){
            if (c2Found.contains(c2.tower.get(i)))
                children.get(children.size()-1).tower.remove(i);
            else
                c1Found.add(c2.tower.get(i));
        }
    }

    void mutateSwap(TowerIndividual c1, TowerIndividual c2){
        ArrayList<TowerPiece> c1Found = new ArrayList<>();
        ArrayList<TowerPiece> c2Found = new ArrayList<>();

        Random rg = new Random();

        for (int i = 0; i < c1.tower.size(); i++){
            if (c1Found.contains(c1.tower.get(i))) {
                int rand = rg.nextInt(ga.towerInput.size());
                while (c1.tower.contains(ga.towerInput.get(rand)))
                    rand = rg.nextInt(c1.tower.size());
                children.get(children.size() - 2).tower.set(i, ga.towerInput.get(rand));
                c1Found.add(ga.towerInput.get(rand));
            }
            else
                c1Found.add(c1.tower.get(i));
        }

        for (int i = 0; i < c2.tower.size(); i++){
            if (c2Found.contains(c2.tower.get(i))) {
                int rand = rg.nextInt(ga.towerInput.size());
                while (c2.tower.contains(ga.towerInput.get(rand)))
                    rand = rg.nextInt(c2.tower.size());
                children.get(children.size() - 1).tower.set(i, ga.towerInput.get(rand));
                c2Found.add(ga.towerInput.get(rand));
            }
            else
                c2Found.add(c2.tower.get(i));
        }
    }

    void printBest(){
        sortIndividuals();
        System.out.println("Best Score: " + individuals.get(individuals.size()-1).fitness + "\n");
        printTower(individuals.get(individuals.size()-1));
    }

    void printTower(TowerIndividual i){
        for (TowerPiece p : i.tower)
            System.out.println(p.toString() + "\n");
    }
}
