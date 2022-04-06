import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Settings {
    JFrame frame;

    JPanel level_panel;
    JPanel size_panel;
    JPanel rows_panel;
    JPanel main_panel;
    JPanel who_start;
    JPanel start_panel;

    JButton[] level_buttons;
    JButton[] board_size_buttons;
    JButton[] rows_to_start_with_buttons;
    JButton start;
    JButton[] who_start_buttons;

    int level=4;    //how many depth in ai
    int board_size=8; //the size of the board
    int rows_start=3; //how many rows to start with pieces
    player_enum starter=player_enum.HUMAN;  //who first play- human or ai

    JLabel the_levels;
    JLabel the_size;
    JLabel the_rows_start;
    JLabel the_who_start;

    GUI g;


    public Settings(){
        start_panel=new JPanel();
        level_panel= new JPanel();
        size_panel= new JPanel();
        rows_panel= new JPanel();
        who_start=new JPanel();

        level_buttons=new JButton[3];
        rows_to_start_with_buttons=new JButton[3];
        board_size_buttons=new JButton[3];
        who_start_buttons=new JButton[2];

        set_level();
        set_board_size();
        set_rows_to_start_with();
        set_who_starts();


        frame = new JFrame();
        frame.setSize(400, 400);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        main_panel=new JPanel();
        main_panel.setLayout(new BoxLayout(main_panel,BoxLayout.Y_AXIS));
        main_panel.add(level_panel);
        main_panel.add(size_panel);
        main_panel.add(rows_panel);
        main_panel.add(who_start);


        start=new JButton();
        start.setText("start");

        start.setPreferredSize(new Dimension(100,50));
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); //close this frame
                g= new GUI(level,board_size,rows_start, starter);  //start the game
            }
        });

        start_panel.add(start);
        main_panel.add(start_panel);
        frame.add(main_panel);


    }

    private void set_level(){
        the_levels=new JLabel();
        the_levels.setText("level:");
        the_levels.setSize(50,50);

        level_buttons[0]=new JButton();
        level_buttons[1]=new JButton();
        level_buttons[2]=new JButton();
        level_buttons[1].setBackground(Color.GREEN); // default choice


        level_buttons[0].setText("easy");
        level_buttons[0].setPreferredSize(new Dimension(100, 50));
        level_buttons[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                level=1;
                level_buttons[0].setBackground(Color.GREEN);
                level_buttons[1].setBackground(null);
                level_buttons[2].setBackground(null);
            }
        });

        level_buttons[1].setText("medium");
        level_buttons[1].setPreferredSize(new Dimension(100, 50));
        level_buttons[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                level=4;
                level_buttons[1].setBackground(Color.GREEN);
                level_buttons[0].setBackground(null);
                level_buttons[2].setBackground(null);
            }
        });


        level_buttons[2].setText("hard");
        level_buttons[2].setPreferredSize(new Dimension(100, 50));
        level_buttons[2].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                level=7;
                level_buttons[2].setBackground(Color.GREEN);
                level_buttons[0].setBackground(null);
                level_buttons[1].setBackground(null);
            }
        });

        level_panel.add(the_levels);
        level_panel.add(level_buttons[0]);
        level_panel.add(level_buttons[1]);
        level_panel.add(level_buttons[2]);



    }

    private void set_board_size(){
        the_size=new JLabel();
        the_size.setText("size:");
        the_size.setSize(50,50);

        board_size_buttons[0]=new JButton();
        board_size_buttons[1]=new JButton();
        board_size_buttons[2]=new JButton();

        board_size_buttons[0].setBackground(Color.GREEN); // default choice


        board_size_buttons[0].setText("8x8");
        board_size_buttons[0].setPreferredSize(new Dimension(100, 50));
        board_size_buttons[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board_size=8;
                board_size_buttons[0].setBackground(Color.GREEN);
                board_size_buttons[1].setBackground(null);
                board_size_buttons[2].setBackground(null);
            }
        });

        board_size_buttons[1].setText("10x10");
        board_size_buttons[1].setPreferredSize(new Dimension(100, 50));
        board_size_buttons[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board_size=10;
                board_size_buttons[1].setBackground(Color.GREEN);
                board_size_buttons[0].setBackground(null);
                board_size_buttons[2].setBackground(null);
            }
        });


        board_size_buttons[2].setText("12x12");
        board_size_buttons[2].setPreferredSize(new Dimension(100, 50));
        board_size_buttons[2].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board_size=12;
                board_size_buttons[2].setBackground(Color.GREEN);
                board_size_buttons[0].setBackground(null);
                board_size_buttons[1].setBackground(null);
            }
        });

        size_panel.add(the_size);
        size_panel.add(board_size_buttons[0]);
        size_panel.add(board_size_buttons[1]);
        size_panel.add(board_size_buttons[2]);
    }

    private void set_rows_to_start_with(){
        the_rows_start=new JLabel();
        the_rows_start.setText("rows:");
        the_rows_start.setSize(50,50);

        rows_to_start_with_buttons[0]=new JButton();
        rows_to_start_with_buttons[1]=new JButton();
        rows_to_start_with_buttons[2]=new JButton();

        rows_to_start_with_buttons[2].setBackground(Color.GREEN); // default choice


        rows_to_start_with_buttons[0].setText("1");
        rows_to_start_with_buttons[0].setPreferredSize(new Dimension(100, 50));
        rows_to_start_with_buttons[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rows_start=1;
                rows_to_start_with_buttons[0].setBackground(Color.GREEN);
                rows_to_start_with_buttons[1].setBackground(null);
                rows_to_start_with_buttons[2].setBackground(null);
            }
        });

        rows_to_start_with_buttons[1].setText("2");
        rows_to_start_with_buttons[1].setPreferredSize(new Dimension(100, 50));
        rows_to_start_with_buttons[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rows_start=2;
                rows_to_start_with_buttons[1].setBackground(Color.GREEN);
                rows_to_start_with_buttons[0].setBackground(null);
                rows_to_start_with_buttons[2].setBackground(null);
            }
        });


        rows_to_start_with_buttons[2].setText("3");
        rows_to_start_with_buttons[2].setPreferredSize(new Dimension(100, 50));
        rows_to_start_with_buttons[2].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rows_start=3;
                rows_to_start_with_buttons[2].setBackground(Color.GREEN);
                rows_to_start_with_buttons[0].setBackground(null);
                rows_to_start_with_buttons[1].setBackground(null);
            }
        });

        rows_panel.add(the_rows_start);
        rows_panel.add(rows_to_start_with_buttons[0]);
        rows_panel.add(rows_to_start_with_buttons[1]);
        rows_panel.add(rows_to_start_with_buttons[2]);
    }

    private void set_who_starts(){
        the_who_start= new JLabel();
        the_who_start.setText("first move:");
        the_who_start.setSize(50,50);

        who_start_buttons[0]= new JButton();
        who_start_buttons[1]= new JButton();

        who_start_buttons[0].setBackground(Color.GREEN); // default

        who_start_buttons[0].setText("human");
        who_start_buttons[0].setPreferredSize(new Dimension(100, 50));
        who_start_buttons[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                starter= player_enum.HUMAN;
                who_start_buttons[0].setBackground(Color.GREEN);
                who_start_buttons[1].setBackground(null);
            }
        });

        who_start_buttons[1].setText("computer");
        who_start_buttons[1].setPreferredSize(new Dimension(100, 50));
        who_start_buttons[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                starter= player_enum.AI;
                who_start_buttons[1].setBackground(Color.GREEN);
                who_start_buttons[0].setBackground(null);
            }
        });

        who_start.add(the_who_start);
        who_start.add(who_start_buttons[0]);
        who_start.add(who_start_buttons[1]);

    }


}
