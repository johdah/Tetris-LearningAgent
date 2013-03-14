/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris.players;

/**
 *
 * @author Administratör
 */
public class Individual {
    	/* Constants */
        //public static int defaultGeneLenght = 64;
	private static int d = 10; //intervall
        
        
        private byte[] genes;
	// Cache
	private double fitness = 0;
        private double weight1,weight2;
        
        private int geneLen;
	
        public Individual(int length){
            genes = new byte[length];
        }
        
        /**
	 * Create a random individual
	 */
        
	public void generateIndividual(int length) {
            genes = new byte[length];
            for(int i = 0; i < length; i++) {
			byte gene = (byte) Math.round(Math.random());
			genes[i] = gene;
		}
                this.geneLen = length;
	}

        public void decodeChromosone(){
            geneLen = genes.length;
            double x1 = 0-d+((2*d)/(1-Math.pow(2, geneLen/2)));
            double x2 = 0-d+((2*d)/(1-Math.pow(2, geneLen/2)));
            
            double a = 0;
            
            //x1
            for(int i = 0; i < geneLen / 2; i++){
                a += (Math.pow(2, 0-i)*genes[i]);
            }
            x1 = x1 * a;
            
            //x2
            a = 0;
            for(int j = geneLen/2; j < geneLen;j++){
                a += (Math.pow(2, 0-j)*genes[j]);
            }
            x2 = x2 * a;
            weight1 = x1;
            weight2 = x2;
        }
        
        public double getWeight1(){
            return weight1;
        }
        
        public double getWeight2(){
            return weight2;
        }
        
        public double getFitness(){
            return fitness;
        }
        
        public void evaluate(){
            fitness = FitnessCalc.getFitness(this);
        }
	/**
	 * Get fitness
	 * @return fitness
	 */
        /*
	public int getFitness() {
		if(fitness == 0) {
			fitness = FitnessCalc.getFitness(this);
		}
		
		return fitness;
	}
	*/
	/**
	 * Get gene
	 * @param index
	 * @return genes[index]
	 */
	public byte getGene(int index) {
		return genes[index];
	}
	
	/**
	 * use this if you want to create individuals with different gene lengths
	 * @param length
	 */
	public static void setDefaultGeneLength(int length) {
	//	defaultGeneLenght = length;
	}
	
	/**
	 * Set gene
	 * @param index
	 * @param value
	 */
	public void setGene(int index, byte value) {
		genes[index] = value;
		fitness = 0;
	}

	/**
	 * Get genes size
	 * @return
	 */
	public int size() {
		return genes.length;
	}
	
	/**
	 * To String
	 */
	@Override
	public String toString() {
		String geneString = "";
		
		for(int i = 0; i < size(); i++) {
			geneString += getGene(i);
		}
		
		return geneString;
	}
}
