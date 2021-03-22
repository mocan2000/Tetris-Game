package Tetris;

import java.util.Random;
import java.lang.Math;


public class TheShapes {
    //  Instantiating field variables
    //enum data type to group the set of constants together
    enum Tetromino {
        ShapeBlanck, Shape_S, Shape_Z, Shape_Line,
        Shape_T, Shape_Square, Shape_L, Shape_J
    }

    private Tetris.TheShapes.Tetromino piece_Shape;
    private int coordinates[][];              ///*this array holds the coordinates on the board*/


    public TheShapes() {            //constructor method holding

        coordinates = new int[4][2];    // array holding holding block coordinates of the block
        setShape (Tetris.TheShapes.Tetromino.ShapeBlanck); // setShape sets the shapes of all the shapes

    }

    public void setShape(Tetris.TheShapes.Tetromino shape) {    // setShape method sets the shapes of all the  Tetromino shapes

        int[][][] tableCoords = new int[][][]{              // the table coordinates that create all the 7 Tetromino shapes
                {{0, 0}, {0, 0}, {0, 0}, {0, 0}},
                {{0, -1}, {0, 0}, {-1, 0}, {-1, 1}},
                {{0, -1}, {0, 0}, {1, 0}, {1, 1}},
                {{0, -1}, {0, 0}, {0, 1}, {0, 2}},
                {{-1, 0}, {0, 0}, {1, 0}, {0, 1}},
                {{0, 0}, {1, 0}, {0, 1}, {1, 1}},
                {{-1, -1}, {0, -1}, {0, 0}, {0, 1}},
                {{1, -1}, {0, -1}, {0, 0}, {0, 1}}
        };

        for (int i = 0; i < 4; i++) {                  // the nested for loop stores value of coordinatis for the block
            for (int j = 0; j < 2; ++j) {
                coordinates[i][j] = tableCoords[shape.ordinal ()][i][j];
            }
        }
        piece_Shape = shape;  // This variable stores one of the shapes at a time.

    }

    private void setX(int index, int x) {
        coordinates[index][0] = x;
    }  //setting first element on row index [0-3] to X

    private void setY(int index, int y) {
        coordinates[index][1] = y;
    }  //setting second element on row index [0-3] to Y

    public int x(int index) {
        return coordinates[index][0];
    }           //returning first element on row index [0-3] to X

    public int y(int index) {
        return coordinates[index][1];
    }           //returning second element on row index [0-3] to Y

    public Tetris.TheShapes.Tetromino getShape() {
        return piece_Shape;
    }

    public void setRandomShape()        //this method creates random shapes
    {
        Random r = new Random ();                // random number generator used and the setShape gets called
        int x = Math.abs (r.nextInt ()) % 7 + 1;
        Tetris.TheShapes.Tetromino[] values = Tetris.TheShapes.Tetromino.values ();
        setShape (values[x]);
    }

    public int minY() {             // returns the minimum Y coordinate
        int k = coordinates[0][1];
        for (int i = 0; i < 4; i++) {
            k = Math.min (k, coordinates[i][1]);
        }
        return k;
    }

    public Tetris.TheShapes rotateLeft() {   // rotate piece to the right
        if (piece_Shape == Tetris.TheShapes.Tetromino.Shape_Square)
            return this;

        Tetris.TheShapes result = new Tetris.TheShapes ();
        result.piece_Shape = piece_Shape;

        for (int i = 0; i < 4; ++i) {
            result.setX (i, y (i));
            result.setY (i, -x (i));
        }
        return result;
    }

    public Tetris.TheShapes rotateRight() {     //rotates piece to the right
        if (piece_Shape == Tetris.TheShapes.Tetromino.Shape_Square)
            return this;

        Tetris.TheShapes result = new Tetris.TheShapes ();
        result.piece_Shape = piece_Shape;

        for (int i = 0; i < 4; ++i) {
            result.setX (i, -y (i));
            result.setY (i, x (i));
        }
        return result;
    }
}

