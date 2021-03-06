package tetris.players;

import java.awt.*;


public class GeneticAI2 extends AbstractAI {
    private double _clears;
    private double _nrOfHoles;
    private double _bumps;
    private double _wallTouches;
    private double _nonFullLines;
    private double _fullLines;
    private double _wells;
    private double _height;

    public GeneticAI2(double clears, double nrOfHoles, double bumps, double wallTouches, double nonFullLines, double fullLines, double wells, double height) {
        _clears = clears;
        _nrOfHoles = nrOfHoles;
        _bumps = bumps;
        _wallTouches = wallTouches;
        _nonFullLines = nonFullLines;
        _fullLines = fullLines;
        _wells = wells;
        _height = height;
    }

    @Override
    public double rateBoard(Color[][] board) {
        int maxHeight = getMaxHeight(board);
        int nrOfWholes = getHoles(board, maxHeight);
        int nrOfNonFullLines = getNonFullLines(board, maxHeight);
        int touchesWall = edgesTouchingWall(board);
        int wells = getWells(board);
        int clear = clearedRows(board);
        int fullLines = fullLines(board);
        int bumps = bumpiness(board);

        double rateValue = 0;

        //good things
        rateValue += -1 * clear * _clears;
        rateValue += -1 * touchesWall * _wallTouches;

        //bad things
        rateValue += nrOfWholes * _nrOfHoles;
        rateValue += bumps * _bumps;
        rateValue += nrOfNonFullLines * _nonFullLines;
        rateValue += fullLines * _fullLines;
        rateValue += wells * _wells;
        rateValue += -1 * maxHeight * _height;

        return rateValue;
    }

    public int getMaxHeight(Color[][] board) {
        int height = board.length;
        int maxHeight = 0;
        for (int w = 0; w < board[0].length; w++) {
            for (int h = 0; h < board.length; h++) {
                if (board[h][w] != null) {
                    if (height - h > maxHeight) {
                        maxHeight = height - h;
                    }
                    break;
                }
            }
        }

        return maxHeight;
    }

    public int getHoles(Color[][] board, int maxHeight) {
        int nrOfWholes = 0;
        for (int h = board.length - maxHeight; h < board.length; h++) {
            for (int w = 0; w < board[0].length && h > 0; w++) {
                if (board[h][w] == null) {//&& board[h-1][w]!=null) {
                    for (int y = h - 1; y >= 0; y--) {
                        if (board[y][w] != null) {
                            nrOfWholes++;
                            break;
                        }
                    }
                }
            }
        }
        return nrOfWholes;
    }

    public int getNonFullLines(Color[][] board, int maxHeight) {
        int nrOfLines = 0;
        for (int h = board.length - maxHeight; h < board.length; h++) {
            for (int w = 0; w < board[0].length; w++) {
                if (board[h][w] == null) {
                    nrOfLines++;
                    break;
                }
            }
        }

        return nrOfLines;
    }
    
    public int edgesTouchingWall(Color[][] board) {
        int touches = 0;
        //left wall
        for (Color[] aBoard : board) {
            if (aBoard[0] != null) {
                touches++;
            }
        }
        //right wall
        for (Color[] aBoard : board) {
            if (aBoard[board[0].length - 1] != null) {
                touches++;
            }
        }
        return touches;
    }

    private void printBoard(Color[][] b) {
        System.out.println("");
        for (Color[] aB : b) {
            for (int w = 0; w < b[0].length; w++) {
                if (aB[w] == null) {
                    System.out.print("0 ");
                } else {
                    System.out.print("X ");
                }
            }
            System.out.println("");
        }
    }

    public int clearedRows(Color[][] b) {
        int clear = 0;
        boolean rowClear;

        for (Color[] aB : b) {
            rowClear = true;
            for (int w = 0; w < b[0].length; w++) {
                if (aB[w] != null) {
                    rowClear = false;
                }
            }
            if (rowClear) {
                clear++;
            }
        }
        return clear;
    }


    public int fullLines(Color[][] b) {
        int nrHoles = 0;
        for (int h = 0; h < b.length; h++) {
            for (int w = 0; w < b[0].length - 1; w++) {
                if (b[h][w] == null && h < 19 && h > 0) {
                    if (b[h + 1][w] != null && b[h - 1][w] != null) {
                        if (w > 0 && b[h][w + 1] != null && b[h][w - 1] != null) {
                            nrHoles++;
                        }
                    }
                }
            }
        }
        return nrHoles;
    }

    public int bumpiness(Color[][] board) {
        int[] peaks = new int[board[0].length];

        for (int w = 0; w < board[0].length; w++) {
            for (int h = 0; h < board.length; h++) {
                if (board[h][w] != null) {
                    peaks[w] = board.length - h;
                    break;
                }
            }
        }

        int totalDiff = 0;
        for (int i = 0; i < peaks.length - 1; i++) {
            totalDiff += Math.abs(peaks[i] - peaks[i + 1]);
        }
        return totalDiff;
    }


    public int getWells(Color[][] board) {
        int wells = 0;

        //find first block from the top
        for (int w = 0; w < board[0].length; w++) {
            for (int h = 0; h < board.length; h++) {
                //found
                if (h > 2 && (board[h][w] != null || h == board.length - 1)) {

                    if (w == 0)//leftmost, check two steps right
                    {
                        if (board[h - 2][w + 1] != null) {
                            wells++;
                        }
                    } else if (w == board[0].length - 1)//rightmost, check two steps left
                    {
                        if (board[h - 2][w - 1] != null) {
                            wells++;
                        }
                    } else //check both sides
                    {
                        if (board[h - 2][w - 1] != null && board[h - 2][w + 1] != null) {
                            wells++;
                        }
                    }
                    break;
                }
            }
        }
        return wells;
    }
}
