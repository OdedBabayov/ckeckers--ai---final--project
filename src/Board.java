import java.util.*;
public class Board {
    public static int side_length = 8;
    private Piece[][] board;
    private int rows_to_start_with = 3;

    public Board(int board_size,int rows_start) {
        this.side_length=board_size;
        this.rows_to_start_with=rows_start;
        board = new Piece[side_length][side_length];
        this.reset();
    }


    public void reset() {

        for (int i = 0; i < side_length; i++) {
            for (int j = 0; j < side_length; j++) {
                board[i][j] = null;
            }
        }
        boolean flag = false;

        for (int i = 0; i < rows_to_start_with; i++) {
            for (int j = 0; j < side_length; j++) {
                if (flag) {
                    board[i][j] = new Piece(player_enum.AI, i, j);
                    flag = false;
                } else
                    flag = true;
            }
            flag = !flag;
        }


        flag = true;

        for (int i = side_length - 1; i > side_length - rows_to_start_with - 1; i--) {
            for (int j = 0; j < side_length; j++) {
                if (flag) {
                    board[i][j] = new Piece(player_enum.HUMAN, i, j);
                    flag = false;
                } else
                    flag = true;
            }
            flag = !flag;
        }


    }

    public String toString() {
        String s = "";
        for (int i = 0; i < side_length; i++) {
            for (int j = 0; j < side_length; j++) {
                if (board[i][j] == null) {
                    s += " 0 , ";
                } else
                    s += board[i][j].toString() + ", ";
            }
            s += "\n";

        }
        return s;
    }

    public boolean can_go_there(int row_source, int col_source, int row_des, int col_des) {
        // function that check if a piece can move from [row_source,col_source] to [row_des,col_des]
        //there is for sure piece in [row_source,col_source]
        if (row_des > side_length - 1 || row_des < 0 || col_des > side_length - 1 || col_des < 0) {         // if destination is not on board
            return false;
        }

        int[] nears_y = board[row_source][col_source].y_movements();  //where he can move in y
        int[] nears_x = board[row_source][col_source].x_movements();  //where he can move in x
        boolean can = false;

        for (int x : nears_x) {
            if (col_des - col_source == x) {
                can = true;
                break;
            }
        }

        if (can == false)
            return can;

        can = false;

        for (int y : nears_y) {
            if (row_des - row_source == y) {
                can = true;
                break;
            }
        }

        if (can == false) {
            return false;
        }
        // for now, the piece can go to that destination, only left to check if the place is protected

        if (this.board[row_des][col_des] == null) {           //the destination is free
            return true;
        }

        //the destination is not free - there is a piece there
        if (this.board[row_des][col_des].whom_piece() == this.board[row_source][col_source].whom_piece()) {
            return false;
        } //the destination is taken by my own piece

        //the destination is taken by the opponent piece

        int move_col = col_des - col_source;
        int move_row = row_des - row_source;


        if (row_des + move_row > side_length - 1 || row_des + move_row < 0 || col_des + move_col > side_length - 1 || col_des + move_col < 0) {    //if there is wall which protect the enemy piece
            return false;
        }


        if (this.board[row_des + move_row][col_des + move_col] != null)    //if there is another piece which protect the enemy piece
            return false;


        return true; //the piece can go there!!!!
    }

    public boolean should_eat(int row_des, int col_des) {
        //I know that I can move to this destination and there is not a piece there which is my own
        //קוראים לפעולה הזאת רק אחרי שקראו לפעולה "האם יכול לזוז לשם" שנמצאת במחלקה הזאת.
        //בגלל זה ידוע לי כבר שאני כן יכול לזוז במקום יעד שלי ושהוא לא תפוס על ידי אותו השחקן שתורו כעת.
        // הפעולה "האם יכול לזוז לשם" תחזיר אמת במידה ויש במקום היעד כלי של היריב שאפשר לאכול או אם אין שום חתיכה שלו.
        // לכן יש צורך לבדוק אם מקום היעד הוא חתיכה של היריב או לא.
        if (this.board[row_des][col_des] != null)
            return true;
        return false;
    }

    public Piece[][] get_board() {
        return board;
    }

    public void set_board(int row, int col, Piece p) {
        this.board[row][col] = p;
    }


    public Board(Board other) {
        this.board = new Piece[side_length][side_length];
        for (int i = 0; i < this.side_length; i++) {
            for (int j = 0; j < this.side_length; j++) {
                if (other.get_board()[i][j] != null) {
                    this.board[i][j] = new Piece(other.get_board()[i][j]);
                }
            }
        }

    }
}
