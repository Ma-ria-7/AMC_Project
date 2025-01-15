

package Projeto;

import java.util.*;
import java.io.*;

public class WeightedGraph implements Serializable {
  
	private static final long serialVersionUID = 1L;
	private List<List<Edge>> adj; // Lista de adjacência para armazenar os pesos das arestas
    private int nnodes;           // Número de nós no grafo

    // Construtor: inicializa o grafo com um número específico de nós com adjacência vazia para cada n Complexidade: O(n)
    public WeightedGraph(int nnodes) {
        this.nnodes = nnodes;
        adj = new ArrayList<>(nnodes);//Cria uma lista de adjacência com nnodes elementos 
        for (int i = 0; i < nnodes; i++) {//Para cada elemento da lista de adjacência, cria uma lista vazia
            adj.add(new ArrayList<>());
        }
    }
    
	public int getNnodes() {
		return nnodes;
	}

    // Método para adicionar uma aresta ponderada entre dois nós
    public void add(int node1, int node2, double weight) {//Adiciona uma aresta ponderada entre dois nós Complexidade: O(1)
        adj.get(node1).add(new Edge(node1, node2, weight));//Adiciona uma aresta entre node1 e node2 com peso weight
        adj.get(node2).add(new Edge(node2, node1, weight)); // Grafo não direcionado: adjacência simétrica
    }
    
    public LinkedList<Integer> offspring(int s) {//Retorna os filhos do nós Complexidade: O(d), onde d é o nº de descendentes
        LinkedList<Integer> o = new LinkedList<>();//Cria uma lista de inteiros vazia 
        for (Edge edge : adj.get(s)) {//Para cada aresta que tem s como nó de origem, adiciona o nó de destino à lista de filhos
            o.add(edge.getTarget());//Adiciona o nó de destino à lista de filhos
        }
        return o;
    }

    // Retorna o peso da aresta entre dois nós (0 se não houver aresta)
    public double getWeight(int node1, int node2) {//Retorna o peso da aresta entre dois nós Complexidade: O(n)
        for (Edge edge : adj.get(node1)) {//Para cada aresta que tem node1 como nó de origem	
            if (edge.getTarget() == node2) {//Se a aresta tem node2 como nó de destino, retorna o peso da aresta
                return edge.getWeight();
            }
        }
        return 0.0;//Se não houver aresta entre os nós, retorna 0
    }

    // Método para obter todas as arestas como objetos Edge (representação completa)
    public List<Edge> getEdges() {//Retorna todas as arestas como objetos Edge Complexidade: O(n^2)
        List<Edge> edges = new ArrayList<>();//Cria uma lista de arestas vazia
        for (int i = 0; i < nnodes; i++) {//Para cada nó no grafo, adiciona todas as arestas conectadas ao nó à lista de arestas
            for (Edge edge : adj.get(i)) {
                if (i < edge.getTarget()) { // Evita duplicar arestas
                    edges.add(edge);
                }
            }
        }
        return edges;//Retorna a lista de arestas do grafo
    }

    // Método para obter a Árvore Geradora Máxima (MST) usando o algoritmo de Prim modificado
    public List<int[]> getMaximumSpanningTree() {//Retorna a Árvore Geradora Máxima (MST) usando o algoritmo de Prim modificado Complexidade: O(m*log(n))
        boolean[] visited = new boolean[nnodes]; // Rastreamento de nós visitados
        List<int[]> mstEdges = new ArrayList<>(); // Lista de arestas da MST
        PriorityQueue<Edge> maxHeap = new PriorityQueue<>(Comparator.comparingDouble(Edge::getWeight).reversed()); // Heap para obter a maior aresta disponível 

        visited[0] = true; // Começa pelo nó 0
        // Adiciona todas as arestas conectadas ao nó inicial na fila de prioridade
        maxHeap.addAll(adj.get(0));//

        // Algoritmo de Prim modificado para MST máxima
        while (!maxHeap.isEmpty()) {//Enquanto a fila de prioridade não estiver vazia 
            Edge edge = maxHeap.poll(); // Remove a aresta de maior peso disponível
            int u = edge.getSource(), v = edge.getTarget();

            if (visited[v]) continue; // Ignora se o nó já foi visitado

            visited[v] = true; // Marca o nó como visitado
            mstEdges.add(new int[]{u, v}); // Adiciona a aresta à MST

            // Adiciona novas arestas conectadas ao nó recém-visitado
            for (Edge nextEdge : adj.get(v)) {
                if (!visited[nextEdge.getTarget()]) {//Se o nó de destino da aresta não foi visitado, adiciona a aresta à fila de prioridade
                    maxHeap.add(nextEdge);
                }
            }
        }

        return mstEdges; // Retorna a lista de arestas da MST
    }

    
    public LinkedList<int[]> arestas() {//Retorna uma lista de arestas na ordem de visitação (BFS) Complexidade: O(n^2)
        LinkedList<int[]> lista = new LinkedList<>(); // Cria uma lista de arestas vazia 
        boolean[] visited = new boolean[this.nnodes]; // Rastreamento de nós visitados 
        Queue<Integer> q = new LinkedList<>(); // Fila para visitar os nós em ordem BFS 
        q.add(0); // Começa pelo nó 0

        while (!q.isEmpty()) {//Enquanto a fila não estiver vazia
            int first = q.remove(); // Remove o primeiro nó da fila
            if (!visited[first]) {
                visited[first] = true; // Marca o nó como visitado
                for (Edge edge : adj.get(first)) {
                    int i = edge.getTarget();
                    if (!visited[i]) {
                        lista.add(new int[]{first, i}); // Adiciona a aresta ao resultado
                        q.add(i); // Adiciona o nó vizinho à fila
                    }
                }
            }
        }
        return lista; // Retorna a lista de arestas na ordem de visitação
    }

    // Classe interna Edge para representar uma aresta no grafo
    public class Edge implements Serializable {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int source;  // Nó de origem
        private int target;  // Nó de destino
        private double weight; // Peso da aresta

        // Construtor: inicializa uma aresta com os nós de origem/destino e seu peso
        public Edge(int source, int target, double weight) {
            this.source = source;
            this.target = target;
            this.weight = weight;
        }

        public int getSource() {//Retorna o nó de origem
            return source;
        }

        public int getTarget() {//Retorna o nó de destino
            return target;
        }

        public double getWeight() {//Retorna o peso da aresta
            return weight;
        }

        // Representação em String da aresta
        @Override
        public String toString() {
            return "Edge{" + "source=" + source + ", target=" + target + ", weight=" + weight + '}';
        }

        // Método equals para comparar duas arestas (baseado nos nós de origem/destino)
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Edge edge = (Edge) obj;
            return source == edge.source && target == edge.target;
        }

        @Override
        public int hashCode() {//Método hashCode para calcular o hash da aresta (baseado nos nós de origem/destino)
            return 31 * source + target;
        }
    }
}



        

    



	
	
	

	
	
    


