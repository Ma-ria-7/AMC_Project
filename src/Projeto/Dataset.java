package Projeto;
import java.io.*;

import java.util.*;

public class Dataset implements Serializable {//Classe Dataset
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int[] maxs;
	ArrayList<int []> datalist;
	int[] mins;

	public Dataset() {//Construtor da classe Dataset que inicializa os atributos maxs e mins com null e cria um ArrayList de vetores de inteiros
		this.maxs = null;
		this.mins = null;
		this.datalist = new ArrayList<int []> ();
		
	}

	public void add(int[] v) {//Método para adicionar um vetor de inteiros ao ArrayList de vetores de inteiros Complexidade: O(n)
	    if (maxs == null) {//Se maxs for null, inicializa maxs e mins com o vetor v 
	        maxs = new int[v.length];
	        mins = new int[v.length];
	        for (int i = 0; i < v.length; i++) {
	            maxs[i] = v[i];
	            mins[i] = v[i];
	        }
	    } else {//Se maxs não for null, compara cada valor de v com o valor correspondente em maxs e mins, atualizando-os se necessário
	        for (int i = 0; i < v.length; i++) {
	            if (maxs[i] < v[i]) {
	                maxs[i] = v[i];
	            }
	            if (mins[i] > v[i]) {
	                mins[i] = v[i];
	            }
	        }
	    }
	    datalist.add(v);//Adiciona o vetor v ao ArrayList de vetores de inteiros
	}
	
	static int [] convert (String line) {//Função que converte uma string em um vetor de inteiros Complexidade: O(n)
		String cvsSplitBy= ",";//Separa os elementos da string por vírgula 
		String[] strings = line.split(cvsSplitBy);//Separa a string em um vetor de strings
		int[]stringToIntVec= new int [strings.length];//Cria um vetor de inteiros com o mesmo tamanho do vetor de strings 
		for (int i=0; i< strings.length;i++)//Converte cada string em um inteiro e armazena no vetor de inteiros
			stringToIntVec[i]=Integer.parseInt(strings[i]);
		return stringToIntVec;//Retorna o vetor de inteiros
		}
	
	public Dataset(String fich) {//Construtor da classe Dataset que lê um arquivo e armazena as linhas do arquivo no ArrayList de vetores de inteiros
		this.maxs = null;//Inicializa maxs com null
		this.datalist = new ArrayList<int []> ();//Cria um ArrayList de vetores de inteiros
		this.mins = null;//Inicializa mins com null
		String line;
		
		BufferedReader bf;
		try {
			bf = new BufferedReader(new FileReader(fich));
			while((line=bf.readLine())  !=null) {
				add(convert(line));
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	boolean condQ(int[] vars, int [] vals, int[] vec) {//Função que verifica se os valores de um vetor de inteiros satisfazem a condição de apresentarem esses valores nessas posições simultaneamente Complexidade: O(n)
		boolean b=true;
		for(int i=0; i<vars.length && b; i++) {
			if (vec[vars[i]] != vals[i]) 
				b=false;
		}
		return b;
		
	}
	
	public double count (int[] vars, int[] vals) {//Função que conta o número de vetores de inteiros que satisfazem a condição vista na função condQ Complexidade: O(n)
		return datalist.stream().filter(vec -> condQ(vars, vals, vec)).count(); 
	}
	
	public Dataset Fiber(int c) {//Função que retorna um Dataset com os vetores de inteiros que têm o valor c na última posição Complexidade: O(n)
		Dataset dr = new Dataset();
		for (int i=0; i<datalist.size(); i++) {//Adiciona ao Dataset os vetores de inteiros que têm o valor c na última posição
			int a[] = datalist.get(i);
			if(a[a.length-1] == c)
				dr.add(a);
		}
		return dr;
	}
	
	public String toString() {//Função que retorna uma string com a representação do Dataset Complexidade: O(n)
		String s ="";
		for (int i=0; i<datalist.size();i++) {
			s=s + Arrays.toString(datalist.get(i)) + ",\n";
			
		}
		return "Dataset maxs=" + Arrays.toString(maxs) + ", \ndatalist=\n" + s + "]";
	}

	public int getNumColunas() { // vai ver o numero de colunas complexidade O(1)
	    if (!datalist.isEmpty()) {
	    	int colunas = datalist.get(0).length ;
	        return colunas;  // Retorna o número de variáveis (colunas)
	    }
	    return 0;  // Retorna 0 se o datalist estiver vazio
	}
	
	
	 
	public double size() { // função que retorna o tamanho da lista de vetores de inteiros Complexidade: O(1)
        return datalist.size(); 
    }
	
	public int[] getMins() {// Função que retorna o vetor de inteiros mins Complexidade: O(1)
		return mins;
	}
	
	public int[] getMaxs() {// Função que retorna o vetor de inteiros maxs Complexidade: O(1)
		return maxs;
	}
	
	public int max(int i) {//Função que retorna o valor máximo da coluna i Complexidade: O(1)
		return maxs[i]; 
	}
	
	
	public int min(int i) {//Função que retorna o valor mínimo da coluna i Complexidade: O(1)
		return mins[i]; 
	}
	
	
	

	
	   public Set<Integer> getUniqueValues(int varIndex) {//Função que retorna um conjunto com os valores distintos encontrados na coluna varIndex Complexidade: O(n)
	    Set<Integer> uniqueValues = new HashSet<>();// Cria um conjunto para armazenar os valores únicos
	    for (int[] observation : datalist) {
	        if (varIndex >= 0 && varIndex < observation.length) {
	            uniqueValues.add(observation[varIndex]);
	        }
	    }
	    return uniqueValues;// Retorna o conjunto de valores únicos
	}
	}
	
	
	
	

