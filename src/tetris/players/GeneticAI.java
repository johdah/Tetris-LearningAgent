/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris.players;

import tetris.Individual;
import tetris.players.AbstractAI;

import java.awt.Color;
import java.util.Random;
/**
 *
 * @author Administratör
 */
public class GeneticAI extends AbstractAI {

    private final int k = 8; //Bits per variable
    private final int n = 2; //Number of variables
    private final int MAX_POPULATIONSIZE = 1000;
    private final double tournamentselectionparameter = 0.7;
    private final int tournamentSize = 4;
    private final int MAX_GENERATION = 100;


    private static final double uniformRate = 0.5;
    private final double probabilityCrossover = 0.8;

    private Individual[] population;
    private Individual[] newPopulation;
    private int currentGeneration = 0;

    private int m; //Size of the gene
    
    @Override
    double rateBoard(Color[][] board) {
        Random rnd = new Random();
        int populationSize = rnd.nextInt(MAX_POPULATIONSIZE);
        while(populationSize == 0)
            populationSize = rnd.nextInt(MAX_POPULATIONSIZE);

        initalizePopulation(populationSize);


        for(currentGeneration = 0; currentGeneration < MAX_GENERATION; currentGeneration++){
            evaluateIndividuals();
            //Create next generation
            createNextGeneration();

        }

        //Get the one that is teh best
        Individual fittest = null;
        try{
            fittest = population[0];
            for(Individual i : population){
                if(i.getFitness() > fittest.getFitness()){
                    fittest = i;
                }
            }
        }catch (Exception e){
           e.printStackTrace();
        }


        //System.out.println("Fittest: "+fittest.getFitness());
        return fittest.getFitness();

        //throw new UnsupportedOperationException("Not supported yet.");
    }

    private void createNextGeneration(){
        for(int i = 0; i < newPopulation.length; i++){
            Individual[] pair = tournament();
            Individual newIndiv = crossover(pair[0], pair[1]);
            mutate(newIndiv);
            newPopulation[i] = newIndiv;
        }

        for(int j = 0; j < population.length; j++){
            population[j] = newPopulation[j];
        }

    }

    private void evaluateIndividuals(){
        for(int i = 0; i < population.length; i++){
            Individual currentIndividual = population[i];
            currentIndividual.decodeChromosone();
            currentIndividual.evaluate();
            population[i] = currentIndividual;
        }
    }
    
    private void initalizePopulation(int N){
        m = k*n; //Length of the string
        population = new Individual[N];
        newPopulation = new Individual[N];

        for(int i = 0; i < N; i++){
            population[i] = new Individual(m);
            population[i].generateIndividual(m);
        }
        
    }
    
    private Individual[] tournament(){
        //Slumpa ett tal r inom intervallet 0-1.
        //Välj den individ som har värde närmast tournamentselectionparamenter
        //ELLER
        /*
         I det generella fallet, väljs j individer 
från populationen där den bästa individen selekteras med sannolikhet Ptour. Om denna 
individen inte selekteras, återupprepas proceduren för de kvarvarande j-1 individerna, 
återigen med en sannolikhet Ptour för att selektera den bästa individen. Parametern j kallas 
för "the tournament size". En större tournament size leder till större konkurrens bland 
individerna. Ni får testa er fram med olika värden på "tournament size", t.ex. 2-4.
         */

        Individual[] winningPair = new Individual[2];

        winningPair[0] = tournamentSelection();
        winningPair[1] = tournamentSelection();
        return winningPair;
        //newPopulation.saveIndividual(i, newIndiv);
	
    }

    /**
     * Mutate individual
     * @param a
     * @return newSol
     */
    private void mutate(Individual a){

        double mutationRate = 1.00/m;

        for(int i = 0; i < a.size(); i++){
            if(Math.random() <= mutationRate){
                if(a.getGene(i) == 0)
                    a.setGene(i,(byte)1);
                else
                    a.setGene(i,(byte)0);
            }
        }

    }


    /**
    * Crossover individuals
    * @param a
    * @param b
    * @return newSol
    */   
    private Individual crossover(Individual a, Individual b){
        Individual newSol = new Individual(m);

        Random rnd = new Random();

        if(rnd.nextInt((int) ((1-tournamentselectionparameter) * 10)) == 0){
            // Loop through genes
            for(int i = 0; i < a.size(); i++) {
                // Crossover
                if(Math.random() <= uniformRate) {
                    newSol.setGene(i, a.getGene(i));
                } else {
                    newSol.setGene(i, b.getGene(i));
                }
            }

            return newSol;
        }
        return a;
    }
    
    /**
     * Select individual for crossover
     * @return
     */
    private Individual tournamentSelection() {
        // Create a tournament population
		
        //Population tournament = new Population(tournamentSize, false);
        Individual[] tournament = new Individual[tournamentSize];
            
        // For each place in the tournament get a random individual
        for(int i = 0; i < tournamentSize; i++) {
            int randomId = (int) (Math.random() * population.length);
            tournament[i] = population[randomId];
        }
	
        //Get fittest;
        
        //The individual with best fitness will be selected with a sannolikhet with tournamentselectionparameter

        Individual fittest = tournament[0];
        
        // Loop through individuals to find fittest
        for(int i = 0; i < tournamentSize; i++) {
            Random rand = new Random();
            
            
            if(i < tournamentSize-1 && rand.nextInt((int) (tournamentselectionparameter * 10)) == 0){
                if(fittest.getFitness() <= tournament[i].getFitness()) {
                    fittest = tournament[i];
                }    
            }else{
                if(i < tournamentSize-1)
                    fittest = tournament[i+1];
            }
            
        }
		
	
        //Individual fittest = tournament.getFittest();
        return fittest;
    }
    
}
