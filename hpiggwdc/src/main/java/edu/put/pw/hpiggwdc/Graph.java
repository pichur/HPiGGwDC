package edu.put.pw.hpiggwdc;

import java.util.TreeMap;
import java.util.TreeSet;

public class Graph {
    private int numVertices;
    private int numEdges;
    private TreeMap<Integer, TreeSet<Integer>> adjacencyList;

    public Graph(int numVertices) {
        this.numVertices = numVertices;
        numEdges = 0;
        this.adjacencyList = new TreeMap<>();
        for (int i = 0; i < numVertices; i++) {
            adjacencyList.put(i, new TreeSet<>());
        }
    }

    public int getNumVertices() {
    	return numVertices;
    }
    
	public int getNumEdges() {
		return numEdges;
	}
	
    public boolean areConnected(Integer u, Integer v) {
        return u != null && v != null && adjacencyList.get(u).contains(v);
    }

	public void show() {
		for (int i = 0; i < numVertices; i++) {
			System.out.println("  " + i + " -> " + adjacencyList.get(i).toString());
		}
	}
	
	public boolean addEdge(Integer u, Integer v) {
		if (areConnected(u, v)) {
			return false;
		}
		numEdges++;
		adjacencyList.get(u).add(v);
		adjacencyList.get(v).add(u);
		return true;
	}
	
	public double getEdgeProbability() {
		return (double)numEdges / (double)((numVertices * (numVertices-1))/2);
	}

}
