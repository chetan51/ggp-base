package org.ggp.base.player.gamer.statemachine.positronicmind;

import java.util.Arrays;
import java.util.List;

import org.ggp.base.util.statemachine.MachineState;
import org.ggp.base.util.statemachine.Move;
import org.ggp.base.util.statemachine.Role;
import org.ggp.base.util.statemachine.StateMachine;
import org.ggp.base.util.statemachine.exceptions.GoalDefinitionException;
import org.ggp.base.util.statemachine.exceptions.MoveDefinitionException;
import org.ggp.base.util.statemachine.exceptions.TransitionDefinitionException;

public class PositronicMinimaxGamer extends PositronicGamer {

	@Override
	public Move bestMove(Role role, MachineState state) throws MoveDefinitionException, GoalDefinitionException, TransitionDefinitionException {
		StateMachine sm = getStateMachine();
		List<Move> moves = sm.getLegalMoves(state, role);
		int score = 0;
		Move move = moves.get(0);
		
		for (int i = 0; i < moves.size(); i++) {
			int result = minScore(role, moves.get(i), state);
			if (result > score) {
				score = result;
				move = moves.get(i);
			}
		}
		
		return move;
	}
	
	public int minScore(Role role, Move move, MachineState state) throws MoveDefinitionException, TransitionDefinitionException, GoalDefinitionException {
		StateMachine sm = getStateMachine();
		Role opponent = getOpponentRole(role, sm);
		List<Move> moves = sm.getLegalMoves(state, opponent);
		int score = 100;
		
		for (int i = 0; i < moves.size(); i++) {
			MachineState nextState = sm.getNextState(state, Arrays.asList(move, moves.get(i)));
			int result = maxScore(role, nextState);
			if (result < score) {
				score = result;
			}
			if (score == 0) break;
		}
		
		return score;
	}

	public int maxScore(Role role, MachineState state) throws MoveDefinitionException, TransitionDefinitionException, GoalDefinitionException {
		StateMachine sm = getStateMachine();
		if (sm.isTerminal(state)) return sm.getGoal(state, role);
		
		List<Move> moves = sm.getLegalMoves(state, role);
		int score = 0;
		
		for (int i = 0; i < moves.size(); i++) {
			int result = minScore(role, moves.get(i), state);
			if (result > score) {
				score = result;
			}
			if (score == 100) break;
		}
		
		return score;
	}
	
}
