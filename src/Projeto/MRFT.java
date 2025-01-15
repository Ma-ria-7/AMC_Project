
// Classe que implementa o modelo MRFT (Markov Random Field Tree) para a rede bayesiana.
package Projeto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import Projeto.WeightedGraph.Edge;

public class MRFT implements Serializable {
    private static final long serialVersionUID = 1L; // Versão da serialização

    private Map<Edge, double[][]> potentials; // Mapa de potenciais para cada aresta
    private double delta = 0.2;               // Constante delta para suavização
    private Edge specialEdge;                 // Aresta especial para normalização
    private WeightedGraph graph;              // Grafo ponderado associado ao modelo
    private Dataset dataset;                  // Dataset associado ao modelo

    // Construtor: inicializa o MRFT com um grafo ponderado e um dataset
    public MRFT(WeightedGraph graph, Dataset dataset) {//Construtor da classe MRFT
        this.graph = graph;
        this.dataset = dataset;
        this.potentials = new HashMap<>();
        this.specialEdge = graph.getEdges().get(0); // Define a primeira aresta como especial por padrão
        calculatePotentials();                      // Calcula os potenciais ao criar a instância
    }

    // Define manualmente uma aresta especial, se necessário
    public void setSpecialEdge(Edge specialEdge) {
        this.specialEdge = specialEdge;
    }

    // Método privado para calcular os potenciais de todas as arestas
    private void calculatePotentials() {//Método para calcular os potenciais de todas as arestas Complexidade: O(n^2)
        // Itera sobre todos os nós do grafo
        for (int node = 0; node < graph.getNnodes(); node++) {//Para cada nó no grafo 
            LinkedList<Integer> offspring = graph.offspring(node);//Obtém os filhos do nó
            for (int target : offspring) {//Para cada filho do nó
                Edge edge = graph.new Edge(node, target, graph.getWeight(node, target));
                int numValuesSource = dataset.max(node);//Obtém o número de valores possíveis para o nó de origem
                int numValuesTarget = dataset.max(target);//Obtém o número de valores possíveis para o nó de destino

                double[][] edgePotentials = new double[numValuesSource][numValuesTarget];//Cria uma matriz de potenciais com dimensões numValuesSource x numValuesTarget

                // Calcula os potenciais para cada combinação de valores dos nós
                for (int i = 0; i < numValuesSource; i++) {
                    for (int j = 0; j < numValuesTarget; j++) {
                        double countJoint = dataset.count(new int[]{node, target}, new int[]{i, j}) + delta;

                        if (edge.equals(specialEdge)) {
                            double normalization = Math.log(dataset.size() + (delta * numValuesSource * numValuesTarget));
                            edgePotentials[i][j] = Math.log(countJoint) - normalization;
                        } else {
                            double countSource = dataset.count(new int[]{node}, new int[]{i}) + delta * numValuesTarget;
                            edgePotentials[i][j] = Math.log(countJoint) - Math.log(countSource);
                        }
                    }
                }

                
                potentials.put(edge, edgePotentials);//Adiciona a matriz de potenciais ao mapa de potenciais
            }
        }
    }

   
    public double prob(int[] variaveis) {//Método para calcular a probabilidade para uma dada configuração de variáveis Complexidade: O(n)
        double logProbability = 0.0;

        for (Edge edge : graph.getEdges()) {//Para cada aresta no grafo 
            int sourceValue = variaveis[edge.getSource()];//Obtém o valor da variável de origem
            int targetValue = variaveis[edge.getTarget()];//Obtém o valor da variável de destino

            double[][] edgePotentials = potentials.get(edge);//Obtém a matriz de potenciais para a aresta
            if (sourceValue < edgePotentials.length && targetValue < edgePotentials[sourceValue].length) {//Verifica se os valores estão dentro dos limites da matriz
                logProbability += edgePotentials[sourceValue][targetValue];//Adiciona o logaritmo do potencial à probabilidade, usamos os logaritmos para evitar underflow(ocorre quando multiplicamos muitas probabilidades pequenas)
            }
        }

        return Math.exp(logProbability);  // Retorna a probabilidade calculada (exponencial do logaritmo) 
    }
    
	public int Numbcol() {// Método para obter o número de colunas Complexidade: O(1)
		return dataset.getNumColunas();// Retorna o número de colunas do dataset
	}

    
}








