
package Projeto;



import java.util.*;
import java.io.*;



public class Algoritmo implements Serializable {

	  
	private static final long serialVersionUID = 1L;


	  
	  public static double[] c_prob(Dataset D) {//Método para calcular a probabilidade a priori de cada classe Complexidade: O(n)
		      double[] r = new double[D.max(D.maxs.length-1)+1];//Cria um vetor de doubles com o número de classes
	    for (int i = 0; i < r.length; i++) {//Calcula a probabilidade de cada classe
	      r[i] = D.Fiber(i).size() / D.size();
	    }
	    return r;//Retorna o vetor de probabilidades a priori
	  }
	  
	  public static double accuracy(Dataset D)  {//Método para calcular a precisão do classificador Complexidade: O(n)
		    int countrights = 0;//Inicializa a variável countrights com 0
		    Classifier c = new Classifier(Chow_Liu.MRFsetup(D), c_prob(D),D.getMaxs(),D.getMins());//Cria um classificador com base nos MRFTs e nas probabilidades a priori
		    

		    // Itera sobre cada linha do dataset
		    for (int i = 0; i < D.size(); i++) {//Para cada linha no dataset
		        int[] vetor = Arrays.copyOfRange(D.datalist.get(i), 0, D.maxs.length - 1); // vai buscar todas as colunas, exceto a última, onde está escrita a classificação
		        int real = D.datalist.get(i)[D.maxs.length - 1]; // é o valor da última coluna (classificação real)
		        int predicted = c.classify(vetor); // previsão do classificador

		        if (predicted == real) { // verifica se a previsão está correta
		            countrights++;
		        }
		    }
		    double percentagem_accuracy = countrights * 100 / D.size();//Calcula a percentagem de previsões corretas
		    
		    return  percentagem_accuracy;//Retorna a percentagem de previsões corretas
		    }
		    
              
                
		
	  
	  
	  }
	