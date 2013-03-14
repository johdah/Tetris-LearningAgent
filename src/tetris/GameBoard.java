/*
 * @(#)SquareBoard.java
 *
 * This work is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as 
 * published by the Free Software Foundation; either version 2 of 
 * the License, or (at your option) any later version.
 *
 * This work is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details.
 *
 * Copyright (c) 2003 Per Cederberg. All rights reserved.
 */

package tetris;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.Hashtable;

/**
 * A Tetris square board. The board is rectangular and contains a grid
 * of colored squares. The board is considered to be constrained to
 * both sides (left and right), and to the bottom. There is no 
 * constraint to the top of the board, although colors assigned to 
 * positions above the board are not saved.
 *
 * @version  1.2
 * @author   Per Cederberg, per@percederberg.net
 */
public class GameBoard extends Object {

    /**
     * The board width (in squares)
     */
    private int  width = 0;

    /**
     * The board height (in squares).
     */
    private int  height = 0;

    /**
     * The square board color matrix. This matrix (or grid) contains
     * a color entry for each square in the board. The matrix is 
     * indexed by the vertical, and then the horizontal coordinate.
     */
    private Color[][]  matrix = null;

    /**
     * An optional board message. The board message can be set at any
     * time, printing it on top of the board.
     */
    private String  message = null;

    /**
     * The number of lines removed. This counter is increased each 
     * time a line is removed from the board.
     */
    private int  removedLines = 0;

    /**
     * The graphical sqare board component. This graphical 
     * representation is created upon the first call to 
     * getComponent().
     */
    private GameBoardComponent component = null;

    /**
     * Creates a new square board with the specified size. The square
     * board will initially be empty.
     *
     * @param width     the width of the board (in squares)
     * @param height    the height of the board (in squares)
     */
    public GameBoard(int width, int height) {
        this.width = width;
        this.height = height;
        this.matrix = new Color[height][width];
        clear();
    }
    
    public Color[][] getBoard(){
        return matrix;
    }

    /**
     * Checks if a specified square is empty, i.e. if it is not 
     * marked with a color. If the square is outside the board, 
     * false will be returned in all cases except when the square is 
     * directly above the board.
     *
     * @param x         the horizontal position (0 <= x < width)
     * @param y         the vertical position (0 <= y < height)
     * 
     * @return true if the square is emtpy, or
     *         false otherwise
     */
    public boolean isSquareEmpty(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return x >= 0 && x < width && y < 0;
        } else {
            return matrix[y][x] == null;
        }
    }

    /**
     * Checks if a specified line is empty, i.e. only contains 
     * empty squares. If the line is outside the board, false will
     * always be returned.
     *
     * @param y         the vertical position (0 <= y < height)
     * 
     * @return true if the whole line is empty, or
     *         false otherwise
     */
    public boolean isLineEmpty(int y) {
        if (y < 0 || y >= height) {
            return false;
        }
        for (int x = 0; x < width; x++) {
            if (matrix[y][x] != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if a specified line is full, i.e. only contains no empty
     * squares. If the line is outside the board, true will always be 
     * returned.
     *
     * @param y         the vertical position (0 <= y < height)
     * 
     * @return true if the whole line is full, or
     *         false otherwise
     */
    public boolean isLineFull(int y) {
        if (y < 0 || y >= height) {
            return true;
        }
        for (int x = 0; x < width; x++) {
            if (matrix[y][x] == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the board contains any full lines.
     *
     * @return true if there are full lines on the board, or
     *         false otherwise
     */
    public boolean hasFullLines() {
        for (int y = height - 1; y >= 0; y--) {
            if (isLineFull(y)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a graphical component to draw the board. The component 
     * returned will automatically be updated when changes are made to
     * this board. Multiple calls to this method will return the same
     * component, as a square board can only have a single graphical
     * representation.
     * 
     * @return a graphical component that draws this board
     */
    public Component getComponent() {
        if (component == null) {
            component = new GameBoardComponent(this);
        }
        return component;
    }

    /**
     * Returns the board height (in squares). This method returns, 
     * i.e, the number of vertical squares that fit on the board.
     * 
     * @return the board height in squares
     */
    public int getBoardHeight() {
        return height;
    }

    /**
     * Returns the board width (in squares). This method returns, i.e,
     * the number of horizontal squares that fit on the board.
     * 
     * @return the board width in squares
     */
    public int getBoardWidth() {
        return width;
    }

    /**
     * Returns the number of lines removed since the last clear().
     * 
     * @return the number of lines removed since the last clear call
     */
    public int getRemovedLines() {
        return removedLines;
    }

    /**
     * Returns the color of an individual square on the board. If the 
     * square is empty or outside the board, null will be returned.
     *
     * @param x         the horizontal position (0 <= x < width)
     * @param y         the vertical position (0 <= y < height)
     * 
     * @return the square color, or null for none
     */
    public Color getSquareColor(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return null;
        } else {
            return matrix[y][x];
        }
    }

    /**
     * Changes the color of an individual square on the board. The 
     * square will be marked as in need of a repaint, but the 
     * graphical component will NOT be repainted until the update() 
     * method is called.
     *
     * @param x         the horizontal position (0 <= x < width)
     * @param y         the vertical position (0 <= y < height)
     * @param color     the new square color, or null for empty
     */
    public void setSquareColor(int x, int y, Color color) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return;
        }
        matrix[y][x] = color;
        if (component != null) {
            component.invalidateSquare(x, y);
        }
    }

    /**
     * Sets a message to display on the square board. This is supposed 
     * to be used when the board is not being used for active drawing, 
     * as it slows down the drawing considerably.
     *
     * @param message  a message to display, or null to remove a
     *                 previous message
     */
    public void setMessage(String message) {
        this.message = message;
        if (component != null) {
            component.redrawAll();
        }
    }

    /**
     * Clears the board, i.e. removes all the colored squares. As 
     * side-effects, the number of removed lines will be reset to 
     * zero, and the component will be repainted immediately.
     */
    public void clear() {
        removedLines = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                this.matrix[y][x] = null;
            }
        }
        if (component != null) {
            component.redrawAll();
        }
    }

    /**
     * Removes all full lines. All lines above a removed line will be 
     * moved downward one step, and a new empty line will be added at 
     * the top. After removing all full lines, the component will be 
     * repainted.
     * 
     * @see #hasFullLines
     */
    public void removeFullLines() {
        boolean repaint = false;

        // Remove full lines
        for (int y = height - 1; y >= 0; y--) {
            if (isLineFull(y)) {
                removeLine(y);
                removedLines++;
                repaint = true;
                y++;
            }
        }

        // Repaint if necessary
        if (repaint && component != null) {
            component.redrawAll();
        }
    }

    /**
     * Removes a single line. All lines above are moved down one step, 
     * and a new empty line is added at the top. No repainting will be 
     * done after removing the line.
     *
     * @param y         the vertical position (0 <= y < height)
     */
    private void removeLine(int y) {
        if (y < 0 || y >= height) {
            return;
        }
        for (; y > 0; y--) {
            for (int x = 0; x < width; x++) {
                matrix[y][x] = matrix[y - 1][x];
            }
        }
        for (int x = 0; x < width; x++) {
            matrix[0][x] = null;
        }
    }

    /**
     * Updates the graphical component. Any squares previously changed 
     * will be repainted by this method.
     */
    public void update() {
        component.redraw();
    }

    /**
     *
     * @return
     */
    public int getHeight() {
        return this.height;
    }

    /**
     *
     * @return
     */
    public Color[][] getMatrix() {
        return this.matrix;
    }

    /**
     *
     * @return
     */
    public String getMessage() {
        return this.message;
    }

    /**
     *
     * @return
     */
    public int getWidth() {
        return this.width;
    }
}
