import java.util.ArrayList;
import java.util.Random;

public class Ai extends  Player{
    private int depth; // how many levels the thinking will be

    public Ai(player_enum o,int depth){
        super(o);
        this.depth=depth;
    }

    public Game ai_move(Game cur_game){
        //get: current game
        //returns game after ai moved
        ArrayList<Game> possibilities = cur_game.possible_matrix_moves_for_ai();

        return miniMax(possibilities);
    }

    public Game miniMax(ArrayList<Game> possibilities){
        //get: possibilities for the current game
        //return new game after ai moved, if cant move return null
        ArrayList<Game> best_possibilities=new ArrayList<>();
        int best_score = Integer.MIN_VALUE;
        for (Game g : possibilities){
            int val= minMax_alpha_beta(g ,this.depth,Integer.MIN_VALUE,Integer.MAX_VALUE);
            if (val>best_score){
                best_score=val;
                best_possibilities.clear();
            }
            if (val==best_score){
                best_possibilities.add(g);
            }
        }
        if (best_possibilities.size()==0){
            return null;
        }

/*
        for (Game gg : best_possibilities){
            System.out.println("the board:\n"+gg.getBoard_status().toString());
            System.out.println("and the score for this boared by alpha beta is: "+best_score);
        }
        System.out.println("new \n\n\n");

 */


        return random_possibility(best_possibilities);
        //return best_possibilities.get(0); // need to change so it will choose a random one
    }

    public int minMax_alpha_beta(Game g,int depth, int alpha, int beta){
        if (depth == 0  || g.check_if_can_play()==false || g.check_if_win()==true || g.check_if_opp_can_play()==false){ //if game over or no more thinking for the ai
            return g.get_score_for_mini_max();
        }
        ArrayList<Game> possibilities=new ArrayList<>();
        if (g.getTurn()==player_enum.HUMAN){
            //possibilities for human moves
            possibilities=g.possible_matrix_moves_for_human();;
        }
        else{
            //possibilities for ai moves
            possibilities=g.possible_matrix_moves_for_ai();;
        }

        if (g.getTurn()==this.name){
            //try to maximize score
            int max=Integer.MIN_VALUE;
            for (Game possibility : possibilities ){
                max=Math.max(max,minMax_alpha_beta(possibility,depth-1,alpha,beta)) ;
                alpha= Math.max(max, alpha);
                if (alpha>=beta){
                    break;
                }
            }
            return max;
        }
        else
        {//try to minimize score
            int min=Integer.MAX_VALUE;
            for (Game possibility : possibilities ){
                min=Math.min(min,minMax_alpha_beta(possibility,depth-1,alpha,beta)) ;
                beta= Math.min(min, beta);
                if (alpha>=beta){
                    break;
                }
            }
            return min;
        }
    }

    private Game random_possibility(ArrayList<Game> best_possibilities){
        Random rand = new Random();
        int i = rand.nextInt(best_possibilities.size());
        return best_possibilities.get(i);
    }

    public int Get_depth(){
        return this.depth;
    }

}
