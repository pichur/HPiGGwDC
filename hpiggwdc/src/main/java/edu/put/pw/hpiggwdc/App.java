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
        
    	Solver<Solution> tabuSearchSolver = new SolverBuilder()
    			.timeLimit(timeLimit)
    			.mode(Mode.TABU_SEARCH)
    			.build();
    	
    	Solver<Solution> bruteForceSolver = new SolverBuilder()
    			.timeLimit(timeLimit)
    			.mode(Mode.BRUTE_FORCE)
    			.build();
    	
    	String file = "C:/tmp/hpiggwdc/info_" + getCurrentDateTime() + ".csv";
        String info = "";
        info += "iter; ";
        info += "numVertices; ";
        info += "distance; ";
        info += "numEdges; ";
        info += "edgeProbability; ";
        info += "TS_D_ok; ";
        info += "TS_D_time; ";
        info += "BF_D_ok; ";
        info += "BF_D_time; ";
        info += "TS_R_ok; ";
        info += "TS_R_time; ";
        info += "BF_R_ok; ";
        info += "BF_R_time; ";
        
        System.out.println(info);
        writeLineToFile(file, info);
    	//run(file, 10, 1, tabuSearchSolver, bruteForceSolver);
    	for (int iter = 0; iter < 10; iter++) {
	    	for (int numVertices = 5; numVertices <= 25; numVertices++) {
	    		for (double distance = 0.3; distance < 1.55; distance += 0.1) {
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
        Solution bruteForceSolutionD = new Solution(graphD, false);

        Solution tabuSearchSolutionR = new Solution(graphR, true );
        Solution bruteForceSolutionR = new Solution(graphR, false);
        
        // Solve the problem
        tabuSearchSolutionD = tabuSearchSolver.solve(tabuSearchSolutionD);
        bruteForceSolutionD = bruteForceSolver.solve(bruteForceSolutionD);
        tabuSearchSolutionR = tabuSearchSolver.solve(tabuSearchSolutionR);
        bruteForceSolutionR = bruteForceSolver.solve(bruteForceSolutionR);
        
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
        info += (bruteForceSolutionD.ok() ? 1 : 0) + "; ";
        info += bruteForceSolutionD.getTimeMillisSpent() + "; ";
        info += (tabuSearchSolutionR.ok() ? 1 : 0) + "; ";
        info += tabuSearchSolutionR.getTimeMillisSpent() + "; ";
        info += (bruteForceSolutionR.ok() ? 1 : 0) + "; ";
        info += bruteForceSolutionR.getTimeMillisSpent() + "; ";
        
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
