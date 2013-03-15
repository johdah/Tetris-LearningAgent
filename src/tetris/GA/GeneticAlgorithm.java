package tetris.GA;


public class GeneticAlgorithm {
    private int _mutationChance;
    private int _populationSize;
    private IProblem _problem;
    private int _generationLimit;

    public static void main(String[] args) {
        GeneticAlgorithm ga = new GeneticAlgorithm(100, 50, new TetrisProblem(), 1000);

        Individual2 indi = ga.PerformRun();

        System.out.println("Best fitness: " + indi.getFitness());
        System.out.println("Information:\n" + indi.getInfo());
    }

    public GeneticAlgorithm(int popSize,int mutationChance, IProblem problem, int generationLimit) {
        _populationSize = popSize;
        _mutationChance = mutationChance;       
        _problem = problem;
        _generationLimit = generationLimit;
    }

    private Individual2 PerformRun() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
}