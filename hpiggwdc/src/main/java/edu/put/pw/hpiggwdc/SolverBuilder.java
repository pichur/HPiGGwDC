package edu.put.pw.hpiggwdc;

import java.util.Collections;
import java.util.List;

import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.exhaustivesearch.ExhaustiveSearchPhaseConfig;
import org.optaplanner.core.config.exhaustivesearch.ExhaustiveSearchType;
import org.optaplanner.core.config.exhaustivesearch.NodeExplorationType;
import org.optaplanner.core.config.localsearch.LocalSearchPhaseConfig;
import org.optaplanner.core.config.localsearch.decider.acceptor.LocalSearchAcceptorConfig;
import org.optaplanner.core.config.localsearch.decider.forager.LocalSearchForagerConfig;
import org.optaplanner.core.config.localsearch.decider.forager.LocalSearchPickEarlyType;
import org.optaplanner.core.config.phase.PhaseConfig;
import org.optaplanner.core.config.score.director.ScoreDirectorFactoryConfig;
import org.optaplanner.core.config.solver.SolverConfig;
import org.optaplanner.core.config.solver.termination.TerminationConfig;

public class SolverBuilder {
	
	public enum Mode {TABU_SEARCH, BRUTE_FORCE};
	
	private long timeLimit = 1;
	
	private Mode mode = Mode.TABU_SEARCH;
	
	public SolverBuilder timeLimit(int timeLimit) {
		this.timeLimit = (long) timeLimit;
		return this;
	}
	
	public SolverBuilder mode(Mode mode) {
		this.mode = mode;
		return this;
	}
	
    public Solver<Solution> build() {
        SolverConfig solverConfig = new SolverConfig();
        solverConfig.setMoveThreadCount("NONE");
        
        solverConfig.setSolutionClass(Solution.class);
        solverConfig.setEntityClassList(List.of(Position.class));
        
        ScoreDirectorFactoryConfig scoreDirectorFactoryConfig = new ScoreDirectorFactoryConfig();
        scoreDirectorFactoryConfig.setEasyScoreCalculatorClass(ScoreCalculator.class);
        solverConfig.setScoreDirectorFactoryConfig(scoreDirectorFactoryConfig);
        
        TerminationConfig terminationConfig = new TerminationConfig();
        terminationConfig.setBestScoreFeasible(true);
        terminationConfig.setSecondsSpentLimit(timeLimit);
		solverConfig.setTerminationConfig(terminationConfig);
        
		PhaseConfig<?> phaseConfig;
		if (mode == Mode.TABU_SEARCH) {
	        // Add tabu search phase
	        LocalSearchPhaseConfig localSearchPhaseConfig = new LocalSearchPhaseConfig();
	        
	        //localSearchPhaseConfig.setLocalSearchType(LocalSearchType.TABU_SEARCH);
	        
	        LocalSearchForagerConfig localSearchForagerConfig = new LocalSearchForagerConfig();
	        
	        localSearchForagerConfig.setPickEarlyType(LocalSearchPickEarlyType.FIRST_BEST_SCORE_IMPROVING);
	        localSearchForagerConfig.setAcceptedCountLimit(1000);
			localSearchPhaseConfig.setForagerConfig(localSearchForagerConfig);
			
			LocalSearchAcceptorConfig acceptorConfig = new LocalSearchAcceptorConfig();
			acceptorConfig.setEntityTabuSize(5); // Przykład konfiguracji Tabu Search
			
			//localSearchPhaseConfig.setMoveSelectorConfig(new ListSwapMoveSelectorConfig());
			
			phaseConfig = localSearchPhaseConfig;
		} else if (mode == Mode.BRUTE_FORCE) {
			// Konfiguracja Exhaustive Search
			ExhaustiveSearchPhaseConfig exhaustiveSearchPhaseConfig = new ExhaustiveSearchPhaseConfig();
			exhaustiveSearchPhaseConfig.setExhaustiveSearchType(ExhaustiveSearchType.BRANCH_AND_BOUND);
			exhaustiveSearchPhaseConfig.setNodeExplorationType(NodeExplorationType.DEPTH_FIRST);
			
			phaseConfig = exhaustiveSearchPhaseConfig;
		} else {
			throw new RuntimeException("Missing mode");
		}
        
		solverConfig.setPhaseConfigList(Collections.singletonList(phaseConfig));
		
        // Build the solver
        SolverFactory<Solution> solverFactory = SolverFactory.create(solverConfig);
        Solver<Solution> solver = solverFactory.buildSolver();
        
        solver.addEventListener(event -> {
            if (event.getNewBestSolution().getScore().isFeasible()) {
            	event.getNewBestSolution().setTimeMillisSpent(event.getTimeMillisSpent());
                solver.terminateEarly();
            }
        });  
        
        return solver;
    }
}
