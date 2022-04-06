import java.util.*;
public class Player{
    protected player_enum name;
    protected List<Piece> pieces;


    public Player(player_enum o){
        this.name=o;
        this.pieces=new ArrayList<Piece>();

    }

    public boolean is_Lost(){
        //return true if no more pieces.
        if(pieces.size()==0){
            return true;
        }
        return false;
    }

    public void add_list(Piece p){
        this.pieces.add(p);
    }

    public List<Piece> get_pieces(){return pieces;}

    public void del_from_list(Piece p){
        pieces.remove(p);
    }

    public String get_pieces_String(){
        String s="";
        for(Piece p:pieces){
            s+= p.get_row() +" "+ p.get_col()+"\n";
        }
        return s;
    }               //need to delete, maybe use this in the bugging

}