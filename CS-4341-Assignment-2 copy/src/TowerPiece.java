public class TowerPiece {

    String type;
    int width;
    int strength;
    int cost;

    public TowerPiece(String t, int w, int s, int c){
        type = t;
        width = w;
        strength = s;
        cost = c;
    }

    public String toString(){
        return(type + ", " + String.valueOf(width) + ", " + String.valueOf(strength) + ", " + String.valueOf(cost));
    }

    public boolean equals(TowerPiece p){
        if (this.type.equals(p.type) && this.strength == p.strength && this.width == p.width && this.cost == p.cost)
            return true;
        return false;
    }
}
