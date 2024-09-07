package edu.put.pw.hpiggwdc;

import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.calculator.EasyScoreCalculator;

import java.util.List;

public class ScoreCalculator implements EasyScoreCalculator<Solution, HardSoftScore> {
	
	private int VERTEX_PENALTY = 2;
	private int EDGE_PENALTY   = 1;
	
	@Override
    public HardSoftScore calculateScore(Solution solution) {

        Graph graph = solution.getGraph(); // Access the graph from the solution
        List<Position> path = solution.getPath();
        
        
        int missing = solution.getGraph().getNumVertices() - (int) path.stream().map(Position::getId).distinct().count();
        
        int hardScore = - VERTEX_PENALTY * missing;
        
        for (int i = 0; i < path.size() - 1; i++) {
            Position from = path.get(i);
            Position to = path.get(i + 1);

            // Penalize if consecutive vertices are not connected
            if (!graph.areConnected(from.getId(), to.getId())) {
                hardScore -= EDGE_PENALTY;
            }
        }
        
        // System.out.println(" S " + hardScore + " " + path.stream().map(Position::getId).toList().toString());

        return HardSoftScore.of(hardScore, 0); // Return the hard score only
    }
	
}
