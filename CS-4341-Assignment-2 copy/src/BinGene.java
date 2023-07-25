import java.util.ArrayList;

public class BinGene {

    ArrayList<Float> bin = new ArrayList<>();

    public void add(float n){
        bin.add(n);
    }

    public float get(int n){
        return bin.get(n);
    }

    public void setBin(ArrayList<Float> b){
        bin = b;
    }

    public void setValue(int i, float n){
        bin.set(i, n);
    }
}
