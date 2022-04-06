public class Piece {

    private player_enum player;
    private boolean king;
    private int row_info;
    private  int col_info;

    public Piece(player_enum o, int row,int col){
        player=o;
        king=false;
        this.col_info=col;
        this.row_info=row;
    }

    public int[] y_movements(){
        int[] result = new int[]{};
        if (king){
            result = new int[]{-1,1};
        }
        else{
            switch (player){
                case AI:
                    result = new int[]{1};
                    break;
                case HUMAN:
                    result = new int[]{-1};
                    break;
            }
        }
        return result;
    }

    public int[] x_movements(){
        return new int[]{-1,1};
    }

    public String toString(){
        switch (player){
            case HUMAN:if (king){return "hhh";}
                return "HHH";
            case AI:if (king){return "aiai";}
                return "AAA";
        }
        return player.toString();
    }

    public void set_king(boolean b){
        this.king=b;
    }

    public player_enum whom_piece(){
        return player;
    }

    public int get_row(){
        return row_info;
    }

    public int get_col(){
        return col_info;
    }

    public void set_row(int row){
        this.row_info=row;
    }

    public void set_col(int col){
        this.col_info=col;
    }

    public boolean getKing(){return king;}



    public Piece(Piece other){
        this.player=other.player;
        this.king=other.king;
        this.row_info=other.row_info;
        this.col_info=other.col_info;
    }
}


