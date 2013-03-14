package tetris.players;

import java.awt.Color;

/**
 * Allows us set a candidate solution and calculate an individual's fitness
 *
 * @author Johan Dahlberg <info@johandahlberg.com>
 */
public class FitnessCalc {
    static byte[] solution = new byte[64];
    static Color[][] board;

    private static double calcWeight(byte[] bytes) {
        int weight = 0, posVal = 1;

        for(int i = bytes.length-1; i > -1; i--) {
            if(i != 7) posVal *= 2;

            if(bytes[i] == 1) {
                weight += posVal;
            }
        }

        // The lowest possible = -64
        //return (weight - 64) / 25;
        return weight / 10; // 1200 = 10
    }

    /**
     *
     * @param board {@link Color}[][]
     * @param maxHeight
     * @return edges
     */
    public static int getEdgesNotTouchingFloor(Color[][] board, int maxHeight) {
        int touches = 0;
        for (int w = 0; w < board[0].length; w++) {
            if (board[0][w] != null) {
                touches++;
            }
        }

        return board[0].length - touches;
    }

    /**
     *
     * @param board {@link Color}[][]
     * @param maxHeight
     * @return edges
     */
    public static int getEdgesNotTouchingWall(Color[][] board, int maxHeight) {
        int edges = 0;
        for (int h = board.length - maxHeight; h < board.length; h++) {
            if(board[h][0] == null) edges++;
            if(board[h][ board[0].length-1 ] == null) edges++;
        }

        return edges;
    }

    /**
     * Calculate individuals fittness by comparing it tou our candidate solution
     * @param individual
     * @return fitness
     */
    public static int getFitness(Individual individual) {
        int fitness = 0;
        byte[] nrOfWholesBytes = new byte[8];
        byte[] maxHeightBytes = new byte[8];
        byte[] nrOfNonFullLinesBytes = new byte[8];
        byte[] noEdgesNotTouchingFloorBytes = new byte[8];
        byte[] noEdgesNotTouchingWallBytes = new byte[8];
        byte[] noOfBlockadesBytes = new byte[8];
        byte[] placePenaltyBytes = new byte[8];

        // Loop throuch our individuals genes and compare them to our candidates
        for(int i = 0; i < individual.size(); i++) {
			/*if(individual.getGene(i) == solution[i]) {
				fitness++;
			}*/
            if(i >= 0 && i < 8) {
                nrOfWholesBytes[i % 8] = individual.getGene(i);
            } else if(i >= 8 && i < 16) {
                maxHeightBytes[i % 8] = individual.getGene(i);
            } else if(i >= 16 && i < 24) {
                nrOfNonFullLinesBytes[i % 8] = individual.getGene(i);
            } else if(i >= 24 && i < 32) {
                noEdgesNotTouchingFloorBytes[i % 8] = individual.getGene(i);
            } else if(i >= 32 && i < 40) {
                noEdgesNotTouchingWallBytes[i % 8] = individual.getGene(i);
            } else if(i >= 40 && i < 48) {
                noOfBlockadesBytes[i % 8] = individual.getGene(i);
            } else if(i >= 48 && i < 56) {
                placePenaltyBytes[i % 8] = individual.getGene(i);
            }
        }

        double nrOfWholesWeight = calcWeight(nrOfWholesBytes);
        double maxHeightWeight = calcWeight(maxHeightBytes);
        double nrOfNonFullLinesWeight = calcWeight(nrOfNonFullLinesBytes);
        double noEdgesNotTouchingFloorWeight = calcWeight(noEdgesNotTouchingFloorBytes);
        double noEdgesNotTouchingWallWeight = calcWeight(noEdgesNotTouchingWallBytes);
        double noOfBlockadesWeight = calcWeight(noOfBlockadesBytes);
        double placePenaltyWeight = calcWeight(placePenaltyBytes);

        int maxHeight = getMaxHeight(board);
        int nrOfWholes = getHoles(board, maxHeight);
        int nrOfNonFullLines = getNonFullLines(board, maxHeight);
        int noOfEdgesNotTouchingFloor = getEdgesNotTouchingFloor(board, maxHeight);
        int noOfEdgesNotTouchingWall = getEdgesNotTouchingWall(board, maxHeight);
        int noOfBlockades = getNoOfBlockades(board, maxHeight);
        int placePenalty = getPlacePenalty(board, maxHeight);

        /**
         * 1800 = 3, 1, 0.5, 0.5, 2, 3
         */
        nrOfWholes *= 3;
        maxHeight *= 1;
        noOfEdgesNotTouchingFloor *= 0.5;
        noOfEdgesNotTouchingWall *= 0.5;
        noOfBlockades *= 2;
        placePenalty *= 3;

        fitness += nrOfWholes * nrOfWholesWeight;
        fitness += maxHeight * maxHeightWeight;
        fitness += nrOfNonFullLines * nrOfNonFullLinesWeight;
        fitness += noOfEdgesNotTouchingFloor * noEdgesNotTouchingFloorWeight;
        fitness += noOfEdgesNotTouchingWall * noEdgesNotTouchingWallWeight;
        fitness += noOfBlockades * noOfBlockadesWeight;
        fitness += placePenalty  * placePenaltyWeight;

        return fitness;
    }

    /**
     *
     * @param board {@link Color}[][]
     * @param maxHeight
     * @return nrOfHoles
     */
    public static int getHoles(Color[][] board, int maxHeight) {
        int nrOfHoles = 0;
        for (int h = board.length - maxHeight; h < board.length; h++) {
            for (int w = 0; w < board[0].length && h>0; w++) {
                if (board[h][w] == null) {
                    for(int y=h-1; y>=0;y--) {
                        if(board[y][w]!=null) {
                            nrOfHoles++;
                            break;
                        }
                    }

                }
            }
        }

        return nrOfHoles;
    }

    /**
     * Get optimum fitness
     * @return
     */
    public static int getMaxFitness() {
        //int maxFitness = solution.length;i
        int maxFitness = 128;
        return maxFitness;
    }

    /**
     *
     * @param board {@link Color}[][]
     * @return maxHeight
     */
    public static int getMaxHeight(Color[][] board) {
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

    /**
     *
     * @param board {@link Color}[][]
     * @param maxHeight
     * @return nrOfLines
     */
    public static int getNonFullLines(Color[][] board, int maxHeight) {
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

    public static int getNoOfBlockades(Color[][] board, int maxHeight){
        int penalty = 0, height = 0;

        for (int h = board.length - maxHeight; h < board.length; h++) {
            for (int w = 0; w < board[0].length; w++) {
                if(board[h][w] != null){
                    height = 0;
                    for(int i = h; h >= 0; i--) {
                        if(board[i][w] == null) {
                            height++;
                            penalty += height;
                        } else {
                            break;
                        }
                    }
                }
            }
        }

        return penalty;
    }

    public static int getPlacePenalty(Color[][] board, int maxHeight){
        int penalty = 0;

        for (int h = board.length - maxHeight; h < board.length; h++) {
            for (int w = 0; w < board[0].length; w++) {
                if (board[h][w] != null) {
                    penalty += h+1;
                    break;
                }
            }
        }

        return penalty;
    }


    /**
     * Set a new board
     * @param board
     */
    public static void setBoard(Color[][] newBoard) {
        board = newBoard;
    }

    /**
     * Set a candidate solution as a byte array
     * @param newSolution
     */
    public static void setSolution(byte[] newSolution) {
        solution = newSolution;
    }

    /**
     * To make it easier we can use this method to set our candidate solution with string of 0s and 1s.
     * @param newSolution
     */
    public static void setSolution(String newSolution) {
        solution = new byte[newSolution.length()];

        // Loop through each character of our string and save it in our byte array
        for(int i = 0; i < newSolution.length(); i++) {
            String character = newSolution.substring(i, i + 1);
            if(character.contains("0") || character.contains("1")) {
                solution[i] = Byte.parseByte(character);
            } else {
                solution[i] = 0;
            }
        }
    }
}