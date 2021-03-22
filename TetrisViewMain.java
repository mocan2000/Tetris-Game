package Tetris;


import javax.swing.*;
import java.awt.*;

public class TetrisViewMain extends JFrame {

    JLabel ScoreLocation;   // field variable for the that holds the score


    private TetrisViewMain() {

        ScoreLocation = new JLabel (" 0");
        add (ScoreLocation, BorderLayout.NORTH);     // positioning and adding the score on the panel
        TetrisView board = new TetrisView (this);    //
        add (board);                     // adding board to the panel
        board.start ();              // calling the start method
        setSize (12 * 26 + 10, 26 * 23 + 25); // setting frame size
        setTitle ("Modou Njie 1401569");
        setDefaultCloseOperation (EXIT_ON_CLOSE);
    }

    JLabel getScoreLocation() {
        return ScoreLocation;
    } // a method call to the getScoreLocation

    public static void main(String[] args) {

        TetrisViewMain tv = new TetrisViewMain ();  // the TetrisVeiwmain method is instanciating tv as the frame
        tv.setLocationRelativeTo (null);
        tv.setVisible (true);

    }
}