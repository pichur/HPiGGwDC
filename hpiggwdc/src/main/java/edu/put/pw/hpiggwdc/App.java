package edu.put.pw.hpiggwdc;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.optaplanner.core.api.solver.Solver;

import edu.put.pw.hpiggwdc.SolverBuilder.Mode;

public class App {
	
	public static void main(String[] args) {
        int timeLimit = 1;
        boolean bf = false;
        
        int max_iter   = 100;
        int nv_start   =  20;
        int nv_end     =  30;
        double d_start = 0.30;
        double d_end   = 0.40;
        double d_step  = 0.01;
        
    	Solver<Solution> tabuSearchSolver = new SolverBuilder()
    			.timeLimit(timeLimit)
    			.mode(Mode.TABU_SEARCH)
    			.build();
    	
    	Solver<Solution> bruteForceSolver = bf ? new SolverBuilder()
    			.timeLimit(timeLimit)
    			.mode(Mode.BRUTE_FORCE)
    			.build() : null;
    	
    	String file = "out/info_" + getCurrentDateTime() + ".csv";
        String info = "";
        info += "iter; ";
        info += "numVertices; ";
        info += "distance; ";
        info += "numEdges; ";
        info += "edgeProbability; ";
        info += "TS_D_ok; ";
        info += "TS_D_time; ";
        if (bf) {
            info += "BF_D_ok; ";
            info += "BF_D_time; ";
        }
        info += "TS_R_ok; ";
        info += "TS_R_time; ";
        if (bf) {
            info += "BF_R_ok; ";
            info += "BF_R_time; ";
        }
        
        System.out.println(info);
        writeLineToFile(file, info);
    	//run(file, 10, 1, tabuSearchSolver, bruteForceSolver);
    	for (int iter = 0; iter < max_iter; iter++) {
	    	for (int numVertices = nv_start; numVertices < nv_end; numVertices++) {
	    		for (double distance = d_start; distance < d_end; distance += d_step) {
	    			run(file, iter, numVertices, distance, tabuSearchSolver, bruteForceSolver);
	    		}
			}
    	}
	}
	
    public static void run(String file, int iter, int numVertices, double distance, Solver<Solution> tabuSearchSolver, Solver<Solution> bruteForceSolver) {
        // Create a random graph with the given probability
        Graph graphD = GraphBuilder.randomByDistance  (numVertices, distance);
        Graph graphR = GraphBuilder.randomByEdgeNumber(numVertices, graphD.getNumEdges());
        
        // Create the unsolved solution
        Solution tabuSearchSolutionD = new Solution(graphD, true );
        Solution tabuSearchSolutionR = new Solution(graphR, true );
        
        tabuSearchSolutionD = tabuSearchSolver.solve(tabuSearchSolutionD);
        tabuSearchSolutionR = tabuSearchSolver.solve(tabuSearchSolutionR);
        
        Solution bruteForceSolutionD = null;
        Solution bruteForceSolutionR = null;
        if (bruteForceSolver != null) {
            bruteForceSolutionD = new Solution(graphD, false);
            bruteForceSolutionR = new Solution(graphR, false);
            
            bruteForceSolutionD = bruteForceSolver.solve(bruteForceSolutionD);
            bruteForceSolutionR = bruteForceSolver.solve(bruteForceSolutionR);
        }
        // Print the result
        
        //graphD.show();
        //tabuSearchSolutionD.printSolution("TABU_SEARCH D");
        //bruteForceSolutionD.printSolution("BRUTE_FORCE D");

        //graphR.show();
        //tabuSearchSolutionR.printSolution("TABU_SEARCH R");
        //bruteForceSolutionR.printSolution("BRUTE_FORCE R");
        
        String info = "";
        info += iter + "; ";
        info += numVertices + "; ";
        info += (int)Math.round(100 * distance) + "; ";
        info += graphD.getNumEdges() + "; ";
        info += (int)Math.round(100 * graphD.getEdgeProbability()) + "; ";
        
        info += (tabuSearchSolutionD.ok() ? 1 : 0) + "; ";
        info += tabuSearchSolutionD.getTimeMillisSpent() + "; ";
        
        if (bruteForceSolver != null) {
            info += (bruteForceSolutionD.ok() ? 1 : 0) + "; ";
            info += bruteForceSolutionD.getTimeMillisSpent() + "; ";
        }
        
        info += (tabuSearchSolutionR.ok() ? 1 : 0) + "; ";
        info += tabuSearchSolutionR.getTimeMillisSpent() + "; ";
        
        if (bruteForceSolver != null) {
            info += (bruteForceSolutionR.ok() ? 1 : 0) + "; ";
            info += bruteForceSolutionR.getTimeMillisSpent() + "; ";
        }
        
        System.out.println(info);
        writeLineToFile(file, info);
    }
    
    public static void writeLineToFile(String filePath, String line) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(line);
            writer.newLine();  // Adds a new line after writing
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }
    
    public static String getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        return now.format(formatter);
    }
    
}
