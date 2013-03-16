package tetris.players;

import java.awt.*;


public class GeneticAI2 extends AbstractAI{

    private float clears;
    private float nrOfHoles;
    private float bumps;
    private float touchWall;
    private float nonFullLines;
    private float fullLines;
    private float wells;
    private float height;

    public GeneticAI2(float v_clears, float v_nrOfHoles, float v_bumps, float v_touchWall, float v_nonFullLines, float v_fullLines, float v_wells, float v_height) {
        clears = v_clears;
        nrOfHoles = v_nrOfHoles;
        bumps = v_bumps;
        touchWall = v_touchWall;
        nonFullLines = v_nonFullLines;
        fullLines = v_fullLines;
        wells = v_wells;
        height = v_height;
    }

    @Override
    public double rateBoard(Color[][] board) {
        int w_maxHeight = GetMaxHeight(board);

        int w_clear = ClearedRows(board);
        int w_nrOfWholes = GetHoles(board, w_maxHeight);
        int w_bumps = Unevenness(board);
        int w_touchesWall = EdgesTouchingWall(board);
        int w_nrOfNonFullLines = GetNonFullLines(board, w_maxHeight);
        int w_nrOfFullLine = FullLines(board);
        int w_wells = GetWells(board);

        double ratevalue = 0;

        //good things
        ratevalue += -1 * w_clear * clears;
        ratevalue += -1 * w_touchesWall * touchWall;

        //bad things
        ratevalue += w_nrOfWholes * nrOfHoles;
        ratevalue += w_bumps * bumps;
        ratevalue += w_nrOfNonFullLines * nonFullLines;
        ratevalue += w_nrOfFullLine * fullLines;
        ratevalue += w_wells * wells;
        ratevalue += -1 * w_maxHeight * height;

        return ratevalue;
    }

    public int GetMaxHeight(Color[][] board) {
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

    public int GetHoles(Color[][] board, int maxHeight) {
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

    public int GetNonFullLines(Color[][] board, int maxHeight) {
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

    public int EdgesTouchingWall(Color[][] board) {
        int touches = 0;
        //left wall
        for (Color[] aBoard1 : board) {
            if (aBoard1[0] != null) {
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

    public int ClearedRows(Color[][] b) {
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

    // number of closed single holes
    public int FullLines(Color[][] b) {
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

    public int Unevenness(Color[][] board) {
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


    public int GetWells(Color[][] board) {
        int wells = 0;

        //first block from the top
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
