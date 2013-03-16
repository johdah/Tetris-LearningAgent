package tetris.GA;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm {
    double bestFitness;
    double avgFitness;
    private IProblem problem;
    private int mutationChance;
    private int generationLimit;
    private int popSize;
    private List<Individual2> population;
    private List<Individual2> nextGen;

    public static void main(String[] args) {

        GeneticAlgorithm ga = new GeneticAlgorithm(50, 500, new TetrisProblem(), 1000);

        Individual2 indi = ga.run();

        System.out.println("Best fitness: " + indi.fitness);
        System.out.println("Information:\n" + indi.getInfo());
    }

    public GeneticAlgorithm(int mutationChance, int popSize, IProblem problem, int generationLimit) {
        this.mutationChance = mutationChance;
        this.popSize = popSize;
        this.problem = problem;
        this.generationLimit = generationLimit;
    }

    public Individual2 run() {
        population = new ArrayList<Individual2>();
        int generation = 0;

        for (int i = 0; i < popSize; i++) {
            population.add(problem.createRandomIndividual());
        }

        long time = System.currentTimeMillis();
        long totalTime = 0;
        while (true) {
            int percentDone = 0;
            avgFitness = 0;
            int progress = 1;
            System.out.print("Calculating... ");
            for (Individual2 individual : population) {
                individual.setFitness(problem.calcFitness(individual));
                individual.setInfo(problem.getInfo(individual));
                avgFitness += individual.fitness;
                if (progress == popSize / 10) {
                    percentDone += 10;
                    System.out.print(percentDone + "% ");
                    progress = 0;
                }
                progress++;
            }
            System.out.println("");
            Individual2 i = getBestIndividual(population);

            bestFitness = i.fitness * -1;
            avgFitness = ((-1 * avgFitness) / population.size());
            totalTime += ((System.currentTimeMillis() - time) / 1000);
            System.out.println("\n---- Generation " + generation + " Population: " + popSize + " Mutation chance: " + (mutationChance / 10) + "% ----");
            System.out.println("Calculation time: " + ((System.currentTimeMillis() - time) / 1000) + "(" + totalTime + ") s");
            System.out.println("Highest score:   " + (bestFitness));
            System.out.println("Average score:    " + (avgFitness));

            System.out.println("Weights\n" + i.getInfo());
            System.out.println("");

            //Terminate if termination criteria is met. Defined in IProblem
            if (problem.criteriaSatisfied(population) || generation >= generationLimit) {
                break;
            }

            nextGen = new ArrayList<Individual2>();

            while (population.size() > nextGen.size()) {
                crossover(runTournament(), runTournament());
            }

            population = nextGen;
            generation++;
            time = System.currentTimeMillis();
        }

        return getBestIndividual(population);
    }

    private void crossover(Individual2 parent1, Individual2 parent2) {
        //Find crossover point

        Random r = new Random();
        int genelength = parent1.getGene().length();
        int crossoverpoint = r.nextInt(genelength - 1);

        String parent1part1 = parent1.getGene().substring(0, crossoverpoint);
        String parent1part2 = parent1.getGene().substring(crossoverpoint, genelength);

        String parent2part1 = parent2.getGene().substring(0, crossoverpoint);
        String parent2part2 = parent2.getGene().substring(crossoverpoint, genelength);

        String child1 = parent1part1 + parent2part2;
        String child2 = parent2part1 + parent1part2;

        if (r.nextInt(1000) < mutationChance) {
            child1 = mutate(child1);
        }
        if (r.nextInt(1000) < mutationChance) {
            child2 = mutate(child2);
        }

        //Add the two new individuals to next generation.
        nextGen.add(new Individual2(child1));
        nextGen.add(new Individual2(child2));
    }

    private Individual2 runTournament() {
        List<Individual2> tournamentPopulation = new ArrayList<Individual2>();
        Random random = new Random();

        //Select random individual to place in tournament.
        for (int i = 0; i < 4; i++) {
            Individual2 individual = population.get(random.nextInt(population.size() - 1));
            tournamentPopulation.add(individual);
        }

        return getBestIndividual(tournamentPopulation);
    }

    private String mutate(String gene) {
        StringBuilder mutatedGene = new StringBuilder(gene);
        int index = new Random().nextInt(mutatedGene.length());

        switch (mutatedGene.charAt(index)) {
            case '0':
                mutatedGene.setCharAt(index, '1');
                break;
            case '1':
                mutatedGene.setCharAt(index, '0');
                break;
        }

        return mutatedGene.toString();
    }

    private Individual2 getBestIndividual(List<Individual2> individuals) {
        Individual2 bestIndividual = null;
        double maxFitness = Double.MAX_VALUE;
        for (Individual2 individual : individuals) {
            if (maxFitness > individual.getFitness()) {
                maxFitness = individual.getFitness();
                bestIndividual = individual;
            }
        }
        return bestIndividual;
    }
}