import java.util.List;
import java.util.Scanner;
import java.util.*;

public class Game {
    private Player human;
    public Ai ai;
    private Board board_status;
    private player_enum turn;
    private int count_turns_without_eat;

    public Game(player_enum o, int level,int board_size,int rows_start, int count_turns_without_eat){
        this.human=new Player(player_enum.HUMAN);
        this.ai=new Ai(player_enum.AI,level);
        this.board_status=new Board(board_size,rows_start);
        this.turn=o;
        this.initial_game();
        this.count_turns_without_eat=count_turns_without_eat;
    }

    private void initial_game(){
        Piece [][] b= board_status.get_board();
        for (int i=0;i< board_status.side_length ;i++)
        {
            for (int j=0;j< board_status.side_length ;j++)
            {
                if (b[i][j]!=null){
                    switch (b[i][j].whom_piece()){
                        case AI: ai.add_list(b[i][j]);
                                continue;
                        case HUMAN: human.add_list(b[i][j]);
                            continue;
                    }
                }
            }
        }
    }

    public boolean check_if_can_play(){
        switch (turn){
            case HUMAN:{
                List<Piece> pl=human.get_pieces();
                for(Piece p:pl){
                    if (check_piece_can_move(p)==true){
                        return true;
                    }
                }
                return false;
            }
            case AI:{
                List<Piece> pl=ai.get_pieces();
                for(Piece p:pl){
                    if (check_piece_can_move(p)==true){
                        return true;
                    }
                }
                return false;
            }
        }
        return false;
    }

    private boolean check_piece_can_move(Piece p){
        //check if the piece that the function got, has any move to do
        int row=p.get_row();
        int col=p.get_col();
        int[] nears_y = (board_status.get_board())[row][col].y_movements();  //where he can move in y
        int[] nears_x = (board_status.get_board())[row][col].x_movements();  //where he can move in x
        for (int i:nears_y){
            for (int j:nears_x){
                if (board_status.can_go_there(row,col,row+i,col+j))
                    return true;
            }
        }
        return false;
    }

    public int update_game(int row_source,int col_source,int row_des,int col_des){
        //this function updates the logic of the game
        //gets: information about where from want to move and information where from want to go. the informations are valid and good.
        //returns 1 if eaten while the update, else 2
        boolean eat= board_status.should_eat(row_des,col_des);
        int row=row_des-row_source;
        int col=col_des-col_source;
        int final_row=row_des;   //where the piece is at the end of the move
        int final_col=col_des;  //where the piece is at the end of the move
        if (eat){

            this.set_count_turns_without_eat(0); //we ate, so count_turns_without_eat need to be 0

            switch (turn){
                case AI:{
                    human.del_from_list(board_status.get_board()[row_des][col_des]); //ai ate human piece - should delete from human
                    break;
                }
                case HUMAN:{
                    ai.del_from_list(board_status.get_board()[row_des][col_des]);  //human ate ai piece - should delete from ai
                    break;
                }
            }
            final_row=row_des+row;      //we jumped over a piece
            final_col=col_des+col;      //we jumped over a piece
            board_status.set_board(final_row,final_col,board_status.get_board()[row_source][col_source]); //put the source piece after we 'jump' the piece that eated

            board_status.set_board(row_des,col_des,null);   //the piece there were eaten

            board_status.set_board(row_source,col_source,null);      //the source place is now empty

            board_status.get_board()[final_row][final_col].set_row(final_row);  //change the row and col features of the piece
            board_status.get_board()[final_row][final_col].set_col(final_col);
            if (final_row==board_status.side_length-1||final_row==0)
                board_status.get_board()[final_row][final_col].set_king(true);  //check if it is a king now
            return 1;

        }
        else{

            this.set_count_turns_without_eat(this.get_count_turns_without_eat()+1); //we didnt ate so count_turns_without_eat should be +1

            board_status.set_board(final_row,final_col,board_status.get_board()[row_source][col_source]); //put the source piece one foreword
            board_status.get_board()[final_row][final_col].set_row(final_row);    //change the row and col features of the piece
            board_status.get_board()[final_row][final_col].set_col(final_col);
        }

        board_status.set_board(row_source,col_source,null); //put null where the player was already

        if (final_row==board_status.side_length-1||final_row==0)
            board_status.get_board()[final_row][final_col].set_king(true);   //check if it is a king now
        return 2;


    }

    public void change_turn(){

        if (this.turn==player_enum.HUMAN)
            this.turn =player_enum.AI;
        else
            this.turn=player_enum.HUMAN;
    }

    public boolean check_if_lost(){
        if (turn==player_enum.HUMAN)
            return human.is_Lost();
        return ai.is_Lost();
    }

    public Board getBoard_status(){
        return this.board_status;
    }

    public player_enum getTurn(){
        return this.turn;
    }

    public boolean first_turn_check_if_place_valid(int row, int col){
        //checks if the source place is valid to play from
        //returns: true if so, else false
        if (board_status.get_board()[row][col]==null){
            return false;
        }
        if (board_status.get_board()[row][col].whom_piece()!=turn){
            return false;
        }
        return true;
    }



    public int get_score_for_mini_max(){
        //returns the score for minimax

        if (this.turn == player_enum.HUMAN){
            if (this.check_if_win()==true ||this.check_if_opp_can_play()== false){  //ai cant move or human won so return worst value
                return Integer.MIN_VALUE;
            }
            if (this.check_if_can_play()==false){  // human cant play - ai won
                return Integer.MAX_VALUE;
            }
        }
        else{ //its ai turn
            if (this.check_if_win()==true ||this.check_if_opp_can_play()== false){  // ai won or human cant move
                return Integer.MAX_VALUE;
            }
            if (this.check_if_can_play()==false){       //ai cant move- human won
                return Integer.MIN_VALUE;
            }
        }

        return score(player_enum.AI)-score(player_enum.HUMAN);


    }

    public boolean check_if_opp_can_play(){

        switch (turn){
            case HUMAN:{
                List<Piece> pl=ai.get_pieces(); // its human turn, should check if ai can play
                for(Piece p:pl){
                    if (check_piece_can_move(p)==true){
                        return true;
                    }
                }
                return false;
            }
            case AI:{
                List<Piece> pl=human.get_pieces();  // its ai turn, should check if human can play
                for(Piece p:pl){
                    if (check_piece_can_move(p)==true){
                        return true;
                    }
                }
                return false;
            }
        }
        return false;
    }

    public boolean check_if_win(){
        //returns true if the correct player won, else false
        if (turn == player_enum.HUMAN)
            return ai.is_Lost();
        return human.is_Lost();


    }

    public ArrayList<Game> possible_matrix_moves_for_ai(){
        //returns all possible moves for ai - returns deep copy matrixes which are from 'game' class
        ArrayList<Game> result = new ArrayList<Game>();
        for (Piece p :ai.get_pieces()){
            result.addAll(possible_matrix_after_moving_one_piece(p));
        }
        return result;
    }

    public ArrayList<Game> possible_matrix_moves_for_human(){
        //returns all possible moves for human - returns deep copy matrixes which are from 'game' class
        ArrayList<Game> result = new ArrayList<Game>();
        for (Piece p :human.get_pieces()){
            result.addAll(possible_matrix_after_moving_one_piece(p));
        }
        return result;
    }

    public ArrayList<Game> possible_matrix_after_moving_one_piece(Piece p) {
        //returns all possible moves for one piece - returns deep copy matrixes which are from 'game' class
        int new_row;
        int new_col;
        Game new_game;
        ArrayList<Game> result = new ArrayList<>();
        for (int x : p.x_movements()) {   //x is col
            for (int y : p.y_movements()) {   //y is row
                new_col = x + p.get_col();
                new_row = y + p.get_row();
                if (board_status.can_go_there(p.get_row(), p.get_col(), new_row, new_col) == true) {
                    new_game = new Game(this);  //deep copy
                    if(new_game.update_game(p.get_row(), p.get_col(), new_row, new_col)==1){//this function returns 1 or 2 - if eaten or not
                        //if ate
                        new_game.set_count_turns_without_eat(0);
                    }
                    else{
                        new_game.set_count_turns_without_eat(this.count_turns_without_eat+1);
                    }

                    new_game.change_turn(); // need to change turns now- player made his move

                    result.add(new_game);
                }
            }
        }
        return result;
    }

    public Game(Game other){
        this.human=new Player(player_enum.HUMAN);

        this.ai=new Ai(player_enum.AI,other.ai.Get_depth());

        this.board_status=new Board(other.board_status);

        this.turn=other.getTurn();

        this.initial_game();    //in order to give the players lists the proper values.

        this.count_turns_without_eat=other.count_turns_without_eat;
    }

    public int score(player_enum o){
        //returns the amount of pieces for player 'o'
        //king is 2 points, regular is 1 point

        int score=0;
        if (o==player_enum.HUMAN){
            for (Piece p :human.get_pieces()){
                if (p.getKing()){
                    score+=10;
                }
                else{
                    if (p.get_row()<this.getBoard_status().side_length/2){
                        score+=7;
                    }
                    else{
                        score+=5;
                    }
                }
            }
        }
        else{
            for (Piece p :ai.get_pieces()){
                if (p.getKing()){
                    score+=10;
                }
                else{
                    if (p.get_row()>=this.getBoard_status().side_length/2){
                        score+=7;
                    }
                    else{
                        score+=5;
                    }
                }
            }
        }
        return score;
    }

    public void set_count_turns_without_eat(int count_turns_without_eat){
        this.count_turns_without_eat=count_turns_without_eat;
    }

    public int get_count_turns_without_eat(){
        return this.count_turns_without_eat;
    }



}
