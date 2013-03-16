package tetris.players;

import java.awt.*;


public class GeneticAI2 extends AbstractAI {

    double weight1 = 0, weight2 = 0, weight3 = 0, weight4 = 0, weight5 = 0, weight6 = 0, weight7 = 0, weight8 = 0;

    public GeneticAI2(double var1float, double var2float, double var3float, double var4float, double var5float, double var6float, double var7float, double var8float) {
        weight1 = var1float;
        weight2 = var2float;
        weight3 = var3float;
        weight4 = var4float;
        weight5 = var5float;
        weight6 = var6float;
        weight7 = var7float;
        weight8 = var8float;

    }

    @Override
    public double rateBoard(Color[][] board) {

        int maxHeight = getMaxHeight(board);
        int nrOfWholes = getHoles(board, maxHeight);
        int nrOfNonFullLines = getNonFullLines(board, maxHeight);
        int touchesWall = edgesTouchingWall(board);
        int wells = getWells(board);
        int clear = cleardRows(board);
        int lonelyHoles = loneClosedHoles(board);
        int bumps = bumpiness(board);

        double ratevalue = 0;

        //good things
        ratevalue += -1 * clear * weight1;
        ratevalue += -1 * touchesWall * weight4;

        //bad things
        ratevalue += nrOfWholes * weight2;
        ratevalue += bumps * weight3;
        ratevalue += nrOfNonFullLines * weight5;
        ratevalue += lonelyHoles * weight6;
        ratevalue += wells * weight7;
        ratevalue += -1 * maxHeight * weight8;

        return ratevalue;
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

    public int getFullLines(Color[][] board, int maxHeight) {
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
        //vänstervägg
        for (int i = 0; i < board.length; i++) {
            if (board[i][0] != null) {
                touches++;
            }
        }
        //högervägg
        for (int i = 0; i < board.length; i++) {
            if (board[i][board[0].length - 1] != null) {
                touches++;
            }
        }
        return touches;
    }

    public int getClears(Color[][] board) {
        int clears = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == null) {
                    break;
                }
                if (j == 8) {
                    clears++;
                }
            }
        }
        return clears;
    }

    private void printBoard(Color[][] b) {
        System.out.println("");
        for (int h = 0; h < b.length; h++) {
            for (int w = 0; w < b[0].length; w++) {
                if (b[h][w] == null) {
                    System.out.print("0 ");
                } else {
                    System.out.print("X ");
                }
            }
            System.out.println("");
        }
    }

    // Antalet fria rader.
    public int cleardRows(Color[][] b) {
        int clear = 0;
        boolean rowClear;

        for (int h = 0; h < b.length; h++) {
            rowClear = true;
            for (int w = 0; w < b[0].length; w++) {
                if (b[h][w] != null) {
                    rowClear = false;
                }
            }
            if (rowClear == true) {
                clear++;
            }
        }
        return clear;
    }

    // Räkna antalet inlåsta singel hål.
    public int loneClosedHoles(Color[][] b) {
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
