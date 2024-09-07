package edu.put.pw.hpiggwdc;

import java.util.ArrayList;
import java.util.List;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.impl.domain.valuerange.buildin.primint.IntValueRange;

@PlanningSolution
public class Solution {

	private Graph graph;
	
    @PlanningEntityCollectionProperty
    private List<Position> path;  // Planning entity list (optimized by OptaPlanner)

    @PlanningScore
    private HardSoftScore score;  // The score calculated by the score calculator
    
    private long timeMillisSpent = 0;
    
    public Solution() {
    }
    
    public Solution(Graph graph, boolean initialized) {
    	setGraph(graph);
        List<Position> positions = new ArrayList<>();
        for (int i = 0; i < graph.getNumVertices(); i++) {
        	positions.add(new Position(i, initialized));
        }
        setPath(positions);
	}

	// Value range provider for the vertex positions using createIntValueRange
    @ValueRangeProvider(id = "range")
    public IntValueRange getPositionRange() {
    	return new IntValueRange(0, graph.getNumVertices());
    }
    
    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public List<Position> getPath() {
        return path;
    }

    public void setPath(List<Position> path) {
        this.path = path;
    }

    public HardSoftScore getScore() {
        return score;
    }

    public void setScore(HardSoftScore score) {
        this.score = score;
    }

	public void setTimeMillisSpent(long timeMillisSpent) {
		this.timeMillisSpent = timeMillisSpent;
	}
	
	public long getTimeMillisSpent() {
		return timeMillisSpent;
	}
	
	public boolean ok() {
		return score != null && score.isFeasible();
	}
	
	public void printSolution(String prefix) {
		System.out.println(
				prefix + "; "
				+ score.hardScore() + "; "
				+ (score.isFeasible() ? path.stream().map(Position::getId).toList().toString() : "FAIL") + "; "
				+ timeMillisSpent);
	}

}
