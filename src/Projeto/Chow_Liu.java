package Projeto;

import java.util.Set;

public class Chow_Liu {

	
	private static double Mutuainfo(Dataset D,int i, int j) {//Método para calcular a informação mútua entre duas variáveis Complexidade: O(n^2)
		double peso = 0;//Inicializa a variável peso com 0
		double totalSize = D.datalist.size();

		if (totalSize == 0) {
			throw new IllegalStateException("Dataset is empty.");//Se o dataset estiver vazio, lança uma exceção
		}

		Set<Integer> valuesI = D.getUniqueValues(i);//Obtém os valores únicos da coluna i
		Set<Integer> valuesJ = D.getUniqueValues(j);//Obtém os valores únicos da coluna j

		for (Integer u : valuesI) {//Para cada valor u na coluna i
			for (Integer v : valuesJ) {//Para cada valor v na coluna j
				int[] vars = { i, j };
				int[] vals = { u, v };
				double pij = D.count(vars, vals) / totalSize;//Calcula a probabilidade conjunta de u e v

				if (pij != 0) {
					double pi = D.count(new int[] { i }, new int[] { u }) / totalSize;//Calcula a probabilidade de u
					double pj = D.count(new int[] { j }, new int[] { v }) / totalSize;//Calcula a probabilidade de v
					peso += pij * (Math.log((pij / (pi * pj))) / Math.log(2));//Calcula a informação mútua entre u e v
				}
			}
		}
		return peso;
	}
	
	public static WeightedGraph grafo_pesado(Dataset D) {//Método para criar um grafo ponderado com base no dataset D Complexidade: O(n^2)
		WeightedGraph g = new WeightedGraph(D.maxs.length - 1);//Cria um grafo com o número de nós igual ao número de váriaveis(nº de colunas do dataset menos 1)
		for (int i = 0; i < D.maxs.length - 1; i++) {
			for (int j = i + 1; j < D.maxs.length - 1; j++) {
				g.add(i, j, Mutuainfo(D, i, j));
			}
		}
		return g;
	}
	
	public static MRFT[] MRFsetup(Dataset D) {//Método para criar um MRFT para cada classe do dataset D Complexidade: O(n)
	    MRFT[] MRFs = new MRFT[D.max(D.maxs.length-1)+1];//Cria um vetor de MRFTs com o número de classes
	    for (int i = 0; i <MRFs.length; i++) {
	    
	      MRFs[i] = new MRFT(grafo_pesado(D.Fiber(i)), D.Fiber(i));
	    }
	    return MRFs;
	  }
	
	
}
