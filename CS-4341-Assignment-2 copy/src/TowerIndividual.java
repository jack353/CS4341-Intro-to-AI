import java.util.ArrayList;
import java.util.Random;

public class TowerIndividual {

    public float fitness = 0;
    int totalCost = 0;
    double cumProb = 0;

    ArrayList<TowerPiece> tower = new ArrayList<>();

    public TowerIndividual(){
        randomFill();
    }

    void randomFill(){
        Random rg = new Random();
        int height = rg.nextInt(ga.towerInput.size()) + 1;
        ArrayList<Integer> used = new ArrayList<>();
        while(height != 0){
            int random = rg.nextInt(ga.towerInput.size());
            if (!used.contains(random)) {
                tower.add(ga.towerInput.get(random));
                height--;
            }
        }
    }

    void calcFitness(){
        getTotalCost();
        if (tower.size() > 1)
            checkTopAndBottom();
    }

    void getTotalCost(){
        totalCost = 0;
        for (TowerPiece p : tower)
            totalCost += p.cost;
    }

    void checkTopAndBottom(){
        if (tower.get(0).type.equals("Door") && tower.get(tower.size()-1).type.equals("Lookout")){
            if(tower.size() > 2)
                checkWalls();
            else
                checkWAndS();
        }
        else
            fitness = 0;
    }

    void checkWalls(){
        boolean invalid = false;
        for (int i = 1; i < tower.size()-1; i++){
            if (!tower.get(i).type.equals("Wall")) {
                invalid = true;
                fitness = 0;
                break;
            }
        }
        if (!invalid)
            checkWAndS();
    }

    void checkWAndS(){
        int prevW = tower.get(0).width;
        boolean valid = true;
        for (int i = 0; i < tower.size(); i++){
            if (tower.get(i).width > prevW || tower.get(i).strength < (tower.size() - i-1)){
                valid = false;
                fitness = 0;
                break;
            }
            prevW = tower.get(i).width;
        }
        if (valid)
            getScore();
    }

    void getScore(){
        fitness = (10 + (tower.size() * tower.size()) -  totalCost);
    }

    void setCumProb(float n){
        cumProb = n;
    }

}
