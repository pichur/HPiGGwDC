package edu.put.pw.hpiggwdc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GraphBuilder {
	
	private static Random random = new Random();
	
    public static Graph randomByDistance(int numVertices, double maxDistance) {
        Graph graph = new Graph(numVertices);

        List<Vertex> vertices = new ArrayList<>();

        for (int i = 0; i < numVertices; i++) {
            double x = random.nextDouble();
            double y = random.nextDouble();
            vertices.add(new Vertex(i, x, y));
        }

        for (int i = 0; i < vertices.size(); i++) {
            for (int j = i + 1; j < vertices.size(); j++) {
                Vertex v1 = vertices.get(i);
                Vertex v2 = vertices.get(j);

                if (v1.distanceTo(v2) <= maxDistance) {
                    graph.addEdge(v1.i, v2.i);
                }
            }
        }

        return graph;
    }
    
    public static Graph randomByProbability(int numVertices, double edgeProbability) {
        Graph graph = new Graph(numVertices);
        for (int i = 0; i < numVertices; i++) {
            for (int j = i + 1; j < numVertices; j++) {
                boolean hasEdge = random.nextDouble() < edgeProbability;
                if (hasEdge) {
                	graph.addEdge(i, j);
                }
            }
        }
        return graph;
    }
	
    public static Graph randomByEdgeNumber(int numVertices, int numEdges) {
    	Graph graph = new Graph(numVertices);
    	
    	while (graph.getNumEdges() < numEdges) {
            int u = random.nextInt(numVertices);
            int v = random.nextInt(numVertices);
            
            graph.addEdge(u, v);
        }
    	
        return graph;
    }
    
}
