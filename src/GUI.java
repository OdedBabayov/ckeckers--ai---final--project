import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Stack;

public class GUI {
    JFrame frame;
    JPanel option_panel;
    JPanel buttons_panel;
    JButton[][] buttons;
    JButton restart;
    JButton undo;
    Game game;
    boolean first_peek=true;
    int row_source, col_source, row_des, col_des;


    int level; //level of ai
    int board_size;  //size of the board
    int rows_start;  //how many rows to start with
    player_enum starter; //first the play

    int limit_turns_without_eat=40; //how many moves without eat are aloud before draw


    Stack<Game> memory;

    Image resized_IconBR;   //image for Black regular
    Image resized_IconBK;   //image for Black King
    Image resized_IconWR;   //image for White regular
    Image resized_IconWK;   //image for White King

    public GUI(int level,int board_size,int rows_start, player_enum starter ) {

        try {
            Image buttonIconBR=ImageIO.read(new File("C:\\Users\\odedb\\OneDrive\\Desktop\\blackchecker - Copy.gif"));
            Image buttonIconBK=ImageIO.read(new File("C:\\Users\\odedb\\OneDrive\\Desktop\\blackking - Copy.png"));
            Image buttonIconWR=ImageIO.read(new File("C:\\Users\\odedb\\OneDrive\\Desktop\\whitechecker - Copy.gif"));
            Image buttonIconWK=ImageIO.read(new File("C:\\Users\\odedb\\OneDrive\\Desktop\\whiteking - Copy.png"));
            int size;
            if (board_size==10){
                size=80;
            }
            else if (board_size==12){
                size=70;
            }
            else{
                size=100;
            }
            resized_IconBR = buttonIconBR.getScaledInstance(size,size,Image.SCALE_SMOOTH);
            resized_IconBK = buttonIconBK.getScaledInstance(size,size,Image.SCALE_SMOOTH);
            resized_IconWR = buttonIconWR.getScaledInstance(size,size,Image.SCALE_SMOOTH);
            resized_IconWK = buttonIconWK.getScaledInstance(size,size,Image.SCALE_SMOOTH);

        }
        catch (Exception e){
            System.out.println(e.toString());
            System.exit(0);
        }


        this.level=level;
        this.board_size=board_size;
        this.rows_start=rows_start;
        this.starter=starter;
        game = new Game(this.starter,this.level,this.board_size,this.rows_start,0);

        memory=new Stack<>();

        set_limit_turns_without_eat();

        option_panel = new JPanel();

        restart = new JButton();
        restart.setText("restart");
        restart.setPreferredSize(new Dimension(100, 50));
        listener_restart_button(restart);
        option_panel.add(restart);

        undo=new JButton();
        undo.setText("undo");
        undo.setPreferredSize(new Dimension(100, 50));
        listener_undo_button();
        option_panel.add(undo);

        frame = new JFrame();
        frame.setSize(1000, 1000);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        frame.add(option_panel, BorderLayout.NORTH);

        buttons_after_start_pushed();
        start_the_buttons_with_color();
        restart.setVisible(true);
    }

    private void buttons_after_start_pushed() {
        buttons_panel = new JPanel();
        buttons_panel.setSize(600, 600);
        buttons_panel.setLayout(new GridLayout(game.getBoard_status().side_length, game.getBoard_status().side_length));

        buttons = new JButton[game.getBoard_status().side_length][game.getBoard_status().side_length];
        for (int row = 0; row < game.getBoard_status().side_length; row++) {
            for (int col = 0; col < game.getBoard_status().side_length; col++) {
                buttons[row][col] = new JButton();
                SetButton_matrix(buttons[row][col], (row * game.getBoard_status().side_length) + col);
            }
        }
        frame.add(buttons_panel, BorderLayout.CENTER);
    }

    private void SetButton_matrix(JButton b, int index) {
        buttons_panel.add(b);
        b.setName(String.valueOf(index));
        b.setVisible(true);
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (game.get_count_turns_without_eat()==limit_turns_without_eat){
                    //to many rounds without anyone eats
                    JOptionPane.showMessageDialog(null,  limit_turns_without_eat+" turns without anyone ate, its a draw");
                    System.exit(0);
                }
                if (game.getTurn() == player_enum.HUMAN) {
                    if (first_peek) {
                        if (game.check_if_can_play() == false) {
                            //if human dont have any moves
                            JOptionPane.showMessageDialog(null,  "you lost");
                            System.exit(0);
                        }
                        row_source = Integer.parseInt(b.getName()) / game.getBoard_status().side_length;
                        col_source = Integer.parseInt(b.getName()) % game.getBoard_status().side_length;
                        if (game.first_turn_check_if_place_valid(row_source, col_source) == false) {
                            //source place not good
                            JOptionPane.showMessageDialog(null, "this is not " + game.getTurn() + " piece");
                            return;
                        }
                        first_peek = false; //source place good, next button push will be second peek- the destination
                        b.setBackground(Color.GREEN);
                        set_color_where_can_piece_move();  // change the color to Yellow where the chosen piece can move to
                    }
                    else {//its second peek - the destination
                        first_peek = true;
                        row_des = Integer.parseInt(b.getName()) / game.getBoard_status().side_length;
                        col_des = Integer.parseInt(b.getName()) % game.getBoard_status().side_length;

                        if (game.getBoard_status().can_go_there(row_source, col_source, row_des, col_des) == false) {
                            change_button_color_like_the_matrix(row_source, col_source);
                            set_color_to_original_from_where_can_piece_move();   // change the color to Black where the chosen piece can move to
                            return;
                        }  //if the destination is not good, return the source button to his original color

                        //the move is legal

                        add_to_memory();    //save the board state before changing it

                        set_color_to_original_from_where_can_piece_move();  // change the color to Black where the chosen piece can move to

                        if (game.update_game(row_source, col_source, row_des, col_des) == 1) {   //if he ate in this turn
                            change_button_color_like_the_matrix(row_des, col_des); //change the color to black because we jumped over
                            row_des += row_des - row_source;    //the destination after the "jump"
                            col_des += col_des - col_source;    //the destination after the "jump"
                        }
                        change_button_color_like_the_matrix(row_source,col_source);//the move is legal so the source need to be change to black with no image

                        change_button_color_like_the_matrix(row_des, col_des); //change the color of the destination by the piece who now there

                        game.change_turn();

                        if (game.check_if_lost()) { //if ai has no pieces- he lost
                            JOptionPane.showMessageDialog(null,"you won");
                            System.exit(0);
                        }
                    }
                } else {//its AI turn
                    add_to_memory(); //save the board state for 'undo'
                    game = game.ai.ai_move(game);   // the board after ai did his turn
                    if (game == null) { //if no possibles moves for ai
                        JOptionPane.showMessageDialog(null, "you won!");
                        System.exit(0);
                    }
                    start_the_buttons_with_color(); //need to change the GUI like the new board
                }
            }

        });
    }

    private void listener_restart_button(JButton re) {

        re.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game = new Game(starter,level,board_size,rows_start,0);
                start_the_buttons_with_color();
                first_peek= true;
                memory= new Stack<>();
            }
        });
    }

    private void start_the_buttons_with_color() {

        //reset the all GUI board like the matrix in game

        for (int row = 0; row < game.getBoard_status().side_length; row++) {
            for (int col = 0; col < game.getBoard_status().side_length; col++) {
                    change_button_color_like_the_matrix(row, col);
                    if (row%2==0 && col%2==0 && game.getBoard_status().get_board()[row][col]==null){
                        buttons[row][col].setBackground(Color.white);
                    }
                if (row%2==1 && col%2==1 && game.getBoard_status().get_board()[row][col]==null){
                    buttons[row][col].setBackground(Color.white);
                }

            }
        }
        // ends of color the board with the pieces at start
    }

    private void change_button_color_like_the_matrix(int row, int col){
        /*
        get - row and col
        change the GUI buttons like the logical matrix in the back
         */
        if (game.getBoard_status().get_board()[row][col]==null){
            buttons[row][col].setIcon(null);
            buttons[row][col].setBackground(Color.black);
            return;
        }

        buttons[row][col].setBackground(Color.black);

        if (game.getBoard_status().get_board()[row][col].whom_piece()==player_enum.HUMAN) {
            if (game.getBoard_status().get_board()[row][col].getKing()){
                buttons[row][col].setIcon(new ImageIcon(resized_IconWK));
            }
            else{
                buttons[row][col].setIcon(new ImageIcon(resized_IconWR));
            }
        }
        else{ //its AI Piece
            if (game.getBoard_status().get_board()[row][col].getKing()){ //its AI king
                buttons[row][col].setIcon(new ImageIcon(resized_IconBK));

            }
            else{//its AI regular
                buttons[row][col].setIcon(new ImageIcon(resized_IconBR));

            }
        }
    }

    private void set_color_where_can_piece_move(){
        /*
        change the color of where the piece in row_source and col_source can move to, to Yellow color
         */
        for (int x : game.getBoard_status().get_board()[row_source][col_source].x_movements()){ // x is col
            for (int y : game.getBoard_status().get_board()[row_source][col_source].y_movements()){ // y is row
                if (game.getBoard_status().can_go_there(row_source,col_source,y+row_source,x+col_source))
                    buttons[y+row_source][x+col_source].setBackground(Color.YELLOW);
            }
        }
    }

    private void set_color_to_original_from_where_can_piece_move(){
        /*
        change the color of where the piece in row_source and col_source can move to, to Black color- the original color of the board
         */
        for (int x : game.getBoard_status().get_board()[row_source][col_source].x_movements()){ // x is col
            for (int y : game.getBoard_status().get_board()[row_source][col_source].y_movements()){ // y is row
                if (game.getBoard_status().can_go_there(row_source,col_source,y+row_source,x+col_source))
                    buttons[y+row_source][x+col_source].setBackground(Color.BLACK);
            }
        }
    }

    private void listener_undo_button(){
        undo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (memory.size()>0){
                    game=memory.pop();  //get the previous board
                    start_the_buttons_with_color(); //change the GUI like the new game
                }
                else{
                    JOptionPane.showMessageDialog(null, "memory empty"); // no previous moves
                }
                first_peek=true;
            }
        });
    }

    private void add_to_memory(){
        if (memory.size()>12){ //if the saved games is over 12, delete the oldest and push new one
            memory.remove(0); //remove the oldest saved
        }
        memory.push(new Game(this.game));
    }

    private void set_limit_turns_without_eat(){
        //limit_turns_without_eat  depend on the size of the board
        if (this.board_size==12)
            limit_turns_without_eat=60;     //12x12
        else if (this.board_size==10)
            limit_turns_without_eat=50;    //10x10
        else
            limit_turns_without_eat=40;     // 8x8

    }


}


