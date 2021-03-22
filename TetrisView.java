package Tetris;

import Tetris.TheShapes.Tetromino;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class TetrisView extends JPanel implements ActionListener {
// Instance  Field variables for the tetrisView .

    final int BoardWidth = 10;
    final int BoardHeight = 20;

    Timer timer;
    boolean isFallingDone = false;
    boolean startTet = false;
    boolean pauseTet = false;
    int deletedNumOfLine = 0;
    int currentX = 0;
    int currentY = 0;
    JLabel ScoreLocation;
    Tetris.TheShapes currentPiece;
    Tetris.TheShapes.Tetromino[] TetrisView;
    int counter = 0;


    public TetrisView(TetrisViewMain parent) {

        currentPiece = new Tetris.TheShapes ();
        timer = new Timer (1000, this); //does one or more action events after every one second
        timer.start (); // gets called in the start method.
        ScoreLocation = parent.getScoreLocation (); // gets called in the parent class
        TetrisView = new Tetris.TheShapes.Tetromino[BoardWidth * BoardHeight];
        setFocusable (true);         // gets call after the board having keyboard input
        addMouseListener (new MouseMethods ()); // adding a mouseListener
        addKeyListener (new CAdapter ());         // adding keyListener
        clearTheBoard ();                          // method call to clear the board
    }

    public void actionPerformed(ActionEvent e) {        // checking whether the falling has finished
        if (isFallingDone) {
            isFallingDone = false;
            newPiece ();
        } else {
            downOneLine ();         //if not finish piece goes down one line
        }
    }


    int square_Width() {
        return (int) getSize ().getWidth () / BoardWidth;
    }

    int square_Height() {
        return (int) getSize ().getHeight () / BoardHeight;
    }

    Tetris.TheShapes.Tetromino shapeAt(int x, int y) {
        return TetrisView[(y * BoardWidth) + x];
    }


    public void start()  //  method that calls other methods and field Variables to start the games
    {
        if (pauseTet)
            return;

        startTet = true;
        isFallingDone = false;
        deletedNumOfLine = 0;
        clearTheBoard ();

        newPiece ();
        timer.start ();
    }

    private void delay_Timer() // timer method that is used for controlled movement of the piece
    {
        if (!startTet)
            return;

        pauseTet = !pauseTet;
        if (pauseTet) {
            timer.stop ();
        } else {
            timer.start ();
            ScoreLocation.setText (String.valueOf (deletedNumOfLine));
        }
        repaint ();
    }

    public void paint(Graphics g)  // draws all the objects on the board
    {
        super.paint (g);

        Dimension size = getSize ();
        int topOfBoard = (int) size.getHeight () - BoardHeight * square_Height ();


        for (int i = 0; i < BoardHeight; ++i) {   //painting all the shapes and remains of the shape been dropped.
            for (int j = 0; j < BoardWidth; ++j) {
                Tetromino shape = shapeAt (j, BoardHeight - i - 1);
                if (shape != Tetris.TheShapes.Tetromino.ShapeBlanck)
                    drawSquare (g, 0 + j * square_Width (),
                            topOfBoard + i * square_Height (), shape);
            }
        }

        if (currentPiece.getShape () != Tetromino.ShapeBlanck) {  //the falling piece gets painted
            for (int i = 0; i < 4; ++i) {
                int x = currentX + currentPiece.x (i);
                int y = currentY - currentPiece.y (i);
                drawSquare (g, 0 + x * square_Width (),
                        topOfBoard + (BoardHeight - y - 1) * square_Height (),
                        currentPiece.getShape ());
            }
        }
    }

    private void dropItDown() //use  when pressing the space for the piece to drop.
    {
        int newY = currentY;
        while (newY > 0) {
            if (!move (currentPiece, currentX, newY - 1))
                break;
            --newY;
        }
        dropPiece ();
    }

    private void downOneLine() //method that gets called for when method isfallingdone is not done.
    {
        if (!move (currentPiece, currentX, currentY - 1))
            dropPiece ();
    }


    private void clearTheBoard() // fills the board with the empty ShapeBlanck , used for collision detection.
    {
        for (int i = 0; i < BoardHeight * BoardWidth; ++i)
            TetrisView[i] = Tetris.TheShapes.Tetromino.ShapeBlanck;
    }

    private void dropPiece() // puts the falling pieces into the board array
    {
        for (int i = 0; i < 4; ++i) {
            int x = currentX + currentPiece.x (i);
            int y = currentY - currentPiece.y (i);
            TetrisView[(y * BoardWidth) + x] = currentPiece.getShape ();
        }

        deleteFull_Lines ();

        if (!isFallingDone)
            newPiece ();
    }

    private void newPiece() // creates a new tetris piece by called setRandom method.
    {
        currentPiece.setRandomShape ();
        currentX = BoardWidth / 2 + 1;
        currentY = BoardHeight - 1 + currentPiece.minY ();

        if (!move (currentPiece, currentX, currentY)) {    // if there is no movement to the init position the game ends.
            currentPiece.setShape (Tetris.TheShapes.Tetromino.ShapeBlanck);
            timer.stop ();
            startTet = false;
            ScoreLocation.setText (String.valueOf ("Final Score is" + "  " + (counter) + "  " + "Points"));//it outputs the overall game score
        }
    }

    private boolean move(TheShapes new_Piece, int new_X, int new_Y) //moves the pieces and returns false if it reaches the board boundaries
    {
        for (int i = 0; i < 4; ++i) {
            int x = new_X + new_Piece.x (i);
            int y = new_Y - new_Piece.y (i);
            if (x < 0 || x >= BoardWidth || y < 0 || y >= BoardHeight)
                return false;
            if (shapeAt (x, y) != TheShapes.Tetromino.ShapeBlanck)
                return false;
        }

        currentPiece = new_Piece;
        currentX = new_X;
        currentY = new_Y;
        repaint ();
        return true;
    }

    private void deleteFull_Lines() // checks for any full lines and deletes it
    {
        int countFullLines = 0;

        for (int i = BoardHeight - 1; i >= 0; --i) {
            boolean full_Lines = true;

            for (int j = 0; j < BoardWidth; ++j) {
                if (shapeAt (j, i) == TheShapes.Tetromino.ShapeBlanck) {
                    full_Lines = false;
                    break;
                }
            }

            if (full_Lines) {
                countFullLines = countFullLines + 10;   // adds 10 point to the score anytime a row is deleted
                for (int k = i; k < BoardHeight - 1; ++k) {
                    for (int j = 0; j < BoardWidth; ++j)
                        TetrisView[(k * BoardWidth) + j] = shapeAt (j, k + 1);

                }
                counter = counter + 10;  // maintains the record of the score.
            }

        }

        if (countFullLines > 0) {
            deletedNumOfLine += countFullLines;
            ScoreLocation.setText (String.valueOf (deletedNumOfLine));
            isFallingDone = true;
            currentPiece.setShape (TheShapes.Tetromino.ShapeBlanck);
            repaint ();
        }
    }

    private void drawSquare(Graphics g, int x, int y, TheShapes.Tetromino shape)//method that draws the four square
    {
        Color colors[] = {new Color (0, 0, 0), new Color (255, 0, 0),
                new Color (0, 128, 0), new Color (0, 0, 225),
                new Color (255, 255, 0), new Color (255, 0, 255),
                new Color (255, 152, 203), new Color (0, 255, 255)
        };


        Color color = colors[shape.ordinal ()];

        g.setColor (color);
        g.fillRect (x + 1, y + 1, square_Width () - 2, square_Height () - 2);

        g.setColor (color.brighter ());    // top left drawn with brighter colours
        g.drawLine (x, y + square_Height () - 1, x, y);
        g.drawLine (x, y, x + square_Width () - 1, y);

        g.setColor (color.darker ());     // bottom right drawn with darker colours
        g.drawLine (x + 1, y + square_Height () - 1,
                x + square_Width () - 1, y + square_Height () - 1);
        g.drawLine (x + square_Width () - 1, y + square_Height () - 1,
                x + square_Width () - 1, y + 1);
    }


    class MouseMethods extends MouseAdapter { //mouse control by a MouseAdapter
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton () == MouseEvent.BUTTON1) {
                    move (currentPiece, currentX - 1, currentY);

            }
            if (e.getButton () == MouseEvent.BUTTON3) {
                    move (currentPiece, currentX + 1, currentY);
            }
            if (e.getButton () == MouseEvent.BUTTON2) {
                move (currentPiece.rotateRight (), currentX, currentY);

            }
        }
    }

    class CAdapter extends KeyAdapter {      // key board control by a a keyAdapter
        public void keyPressed(KeyEvent e) {

            if (!startTet || currentPiece.getShape () == Tetromino.ShapeBlanck) {
                return;
            }

            int keycode = e.getKeyCode ();

            if (keycode == 'p' || keycode == 'p') {
                delay_Timer ();
                return;
            }

            if (pauseTet)
                return;

            switch (keycode) {
                case KeyEvent.VK_LEFT:
                    move (currentPiece, currentX - 1, currentY);
                    break;
                case KeyEvent.VK_RIGHT:
                    move (currentPiece, currentX + 1, currentY);
                    break;
                case KeyEvent.VK_DOWN:
                    move (currentPiece.rotateLeft (), currentX, currentY);
                    break;
                case KeyEvent.VK_UP:
                    move (currentPiece.rotateRight (), currentX, currentY);
                    break;
                case KeyEvent.VK_SPACE:
                    dropItDown ();
                    break;
                case 'd':
                    downOneLine ();
                    break;
                case 'D':
                    downOneLine ();
                    break;
            }

        }
    }
}