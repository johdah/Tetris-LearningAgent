package tetris.GA;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GeneticAlgorithm {
    private IProblem problem;
    private int mutationChance;
    private int populationSize;
    private int tournamentSize;
    private int generationLimit;
    
    private List<Individual2> population;
    private List<Individual2> nextGen;

    public static void main(String[] args) {
        //GeneticAlgorithm ga = new GeneticAlgorithm(10, 1000, new MathProblem(), 100,4);
        GeneticAlgorithm ga = new GeneticAlgorithm(10, 500, new TetrisProblem(), 100,4);

        Individual2 indi = ga.PerformRun();

        System.out.println("Best fitness: " + indi.GetFitness());
        System.out.println("Information:\n" + indi.GetInfo());
    }

    public GeneticAlgorithm(int mutationChance,int popSize, IProblem problem, int generationLimit, int tourSize) {
        this.populationSize = popSize;
        this.mutationChance = mutationChance;       
        this.problem = problem;
        this.generationLimit = generationLimit;
        this.tournamentSize = tourSize;
    }

    private Individual2 PerformRun() {    
    
        population = new ArrayList<Individual2>();
        int generation = 0;

        for (int i = 0; i < this.populationSize; i++) {
            population.add(problem.CreateRandomIndividual());
        }

        long time = System.currentTimeMillis();
        long totalTime = 0;
        while (true) {
            int percentDone = 0;
            int avgFitness = 0;
            int progress = 1;
            System.out.print("Calculating... ");
            for (Individual2 individual : population) {
                individual.SetFitness(problem.CalcFitness(individual));
                individual.SetInfo(problem.GetInfo(individual));
                avgFitness += individual.GetFitness();
                if (progress == populationSize / 10) {
                    percentDone += 10;
                    System.out.print(percentDone + "% ");
                    progress = 0;
                }
                progress++;
            }
            System.out.println("");
            Individual2 i = GetBestIndividual(population);

            double bestFitness = i.GetFitness();
            avgFitness = ((avgFitness) / population.size());
            totalTime += ((System.currentTimeMillis() - time) / 1000);
            System.out.println("\n---- Generation " + generation + " Population: " + populationSize + " Mutation chance: " + mutationChance  + "% ----");
            System.out.println("Calculation time: " + ((System.currentTimeMillis() - time) / 1000) + "(" + totalTime + ") s");
            System.out.println("Best score:   " + (bestFitness));
            System.out.println("Average score:    " + (avgFitness));

            System.out.println("Weights:\n" + i.GetInfo());
            System.out.println("");

            //Terminate if termination criteria is met. Defined in IProblem
            if (problem.CriteriaSatisfied(population) || generation >= generationLimit) {
                break;
            }

            this.nextGen = new ArrayList<Individual2>();

            while (population.size() > nextGen.size()) {
                Crossover(RunTournament(), RunTournament());
            }

            population = nextGen;
            generation++;
            time = System.currentTimeMillis();
        }

        return GetBestIndividual(population);
    }

    private void Crossover(Individual2 parent1, Individual2 parent2) {
        //Find crossoverpoint

        Random r = new Random();
        int geneLength = parent1.GetGene().length();
        int crossoverPoint = r.nextInt(geneLength - 1);

        String parent1part1 = parent1.GetGene().substring(0, crossoverPoint);
        String parent1part2 = parent1.GetGene().substring(crossoverPoint, geneLength);

        String parent2part1 = parent2.GetGene().substring(0, crossoverPoint);
        String parent2part2 = parent2.GetGene().substring(crossoverPoint, geneLength);

        String child1 = parent1part1 + parent2part2;
        String child2 = parent2part1 + parent1part2;

        if (r.nextInt(100) < mutationChance) {
            child1 = MutateGene(child1);
        }
        if (r.nextInt(100) < mutationChance) {
            child2 = MutateGene(child2);
        }

        //Add the two new individuals to next generation.
        nextGen.add(new Individual2(child1));
        nextGen.add(new Individual2(child2));
    }

    private Individual2 RunTournament() {
        List<Individual2> tournamentPopulation = new ArrayList<Individual2>();
        Random random = new Random();

        //Select random individual to place in tournament.
        for (int i = 0; i < tournamentSize; i++) {
            Individual2 individual = population.get(random.nextInt(population.size() - 1));
            tournamentPopulation.add(individual);
        }

        return GetBestIndividual(tournamentPopulation);
    }

    private String MutateGene(String gene) {
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

    private Individual2 GetBestIndividual(List<Individual2> individuals) {
        Individual2 bestIndividual = null;
        double maxFitness = Double.MAX_VALUE;
        for (Individual2 individual : individuals) {
            if (maxFitness > individual.GetFitness()) {
                maxFitness = individual.GetFitness();
                bestIndividual = individual;
            }
        }
        return bestIndividual;
    }
    
}