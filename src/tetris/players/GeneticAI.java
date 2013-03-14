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
    private static final double uniformRate = 0.5;
    
    private Individual[] population;
    private int m; //Size of the gene
    
    @Override
    double rateBoard(Color[][] board) {
        Random rnd = new Random();
        initalizePopulation(rnd.nextInt(MAX_POPULATIONSIZE));
        evaluateIndividuals();
        
        
        
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    private void evaluateIndividuals(){
        for(int i = 0; i < population.length; i++){
            Individual currentIndividual = population[i];
            
            population[i].decodeChromosone();
            population[i].evaluate();
            
        }
        
        tournament();
    }
    
    private void initalizePopulation(int N){
        m = k*n; //Length of the string
        population = new Individual[N];
        
        for(int i = 0; i < N; i++){
            population[i] = new Individual(m);
            population[i].generateIndividual(m);
        }
        
    }
    
    private void tournament(){
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
        
        Individual indiv1 = tournamentSelection();
        Individual indiv2 = tournamentSelection();
        Individual newIndiv = crossover(indiv1, indiv2);
        //newPopulation.saveIndividual(i, newIndiv);
	
    }


   /**
    * Crossover individuals
    * @param a
    * @param b
    * @return newSol
    */   
    private Individual crossover(Individual a, Individual b){
        Individual newSol = new Individual(m);
		
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
