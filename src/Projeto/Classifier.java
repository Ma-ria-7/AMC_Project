package Projeto;

import java.io.Serializable;

public class Classifier implements Serializable {
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private MRFT[] mrfts; // Array de MRFTs, um para cada classe
	private double[] prob; // Array de probabilidades a priori de cada classe
	private int[] maxs; // Array de máximos de cada variável
	private int[] mins; // Array de mínimos de cada variável

    public Classifier(MRFT[] MRFs, double[] prob, int[] maxs, int[] mins) {
        this.mrfts = MRFs;
        this.prob = prob;
        this.maxs = maxs;
        this.mins = mins;
    }

    public int getNumColunas() {
        return mrfts[0].Numbcol();
    }
    
    public int[] getMaxs() {
    	return maxs;
    }
    
	public int[] getMins() {
		return mins;
	}
    
    public int max(int i) {
        return maxs[i]; // retorna o valor máximo da medição
    }
    
	public int min(int i) {
		return mins[i]; // retorna o valor mínimo da medição
	}

	 public int classify(int[] variáveis) {//Método para classificar uma observação com base nas probabilidades calculadas pelos MRFT e nas probabilidades a priori
	        double[] probabilidades = new double[mrfts.length]; // Array para armazenar as probabilidades de cada classe
	        for (int i = 0; i < mrfts.length; i++) {// 
	        	
//pC(c) (frequência com que a classe c aparece nos dados) x pMc(x1,…, xn) (probabilidade de observar os valores das variáveis dada uma classe c)
	            probabilidades[i] = prob[i] * mrfts[i].prob(variáveis);
	            
	        }
	        
//queremos a classe que maximiza a multiplicação anterior
	        
	        
	        int ClasseProvável = 0;//Inicializa a variável ClasseProvável com 0
	        
	        for (int i = 1; i < probabilidades.length; i++) {//Itera sobre as probabilidades calculadas para encontrar a classe com a maior probabilidade
	        	
	            if (probabilidades[i] > probabilidades[ClasseProvável]) {//Se a probabilidade da classe i for maior que a probabilidade da classe ClasseProvável atualiza a variável ClasseProvável
	            	
	                ClasseProvável = i;
	            }
	        }

	        return ClasseProvável;//Retorna a classe provável 
	 }
	 

	 
}


