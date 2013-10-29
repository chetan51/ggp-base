package org.ggp.base.player.gamer.statemachine.positronicmind;

import java.util.List;

import org.ggp.base.player.gamer.event.GamerSelectedMoveEvent;
import org.ggp.base.util.statemachine.MachineState;
import org.ggp.base.util.statemachine.Move;
import org.ggp.base.util.statemachine.Role;
import org.ggp.base.util.statemachine.StateMachine;
import org.ggp.base.util.statemachine.exceptions.GoalDefinitionException;
import org.ggp.base.util.statemachine.exceptions.MoveDefinitionException;
import org.ggp.base.util.statemachine.exceptions.TransitionDefinitionException;

public class PositronicDeliberationGamer extends PositronicGamer {

	@Override
	public Move stateMachineSelectMove(long timeout)
			throws TransitionDefinitionException, MoveDefinitionException,
			GoalDefinitionException {
		long start = System.currentTimeMillis();
		List<Move> moves = getStateMachine().getLegalMoves(getCurrentState(), getRole());
		Move selection = bestMove(getRole(), getCurrentState());
		
		long stop = System.currentTimeMillis();
		notifyObservers(new GamerSelectedMoveEvent(moves, selection, stop - start));
		return selection;
	}
	
	public int value(Role role, MachineState state) throws GoalDefinitionException, MoveDefinitionException, TransitionDefinitionException {
		StateMachine sm = getStateMachine();
		if (sm.isTerminal(state)) return sm.getGoal(state, role);
		int score = 0;
		List<Move> moves = sm.getLegalMoves(state, role);
		
		for (int i = 0; i < moves.size(); i++) {
			int result = value(role, sm.getNextState(state, moves.subList(i, i+1)));
			if (result > score) score = result;
		}
		
		return score;
	}
	
	public Move bestMove(Role role, MachineState state) throws MoveDefinitionException, GoalDefinitionException, TransitionDefinitionException {
		StateMachine sm = getStateMachine();
		List<Move> moves = sm.getLegalMoves(state, role);
		Move action = moves.get(0);
		int score = 0;
		
		for (int i = 0; i < moves.size(); i++) {
			int result = value(role, sm.getNextState(state, moves.subList(i, i+1)));
			if (result > score) {
				action = moves.get(i);
				score = result;
			}
		}
		
		return action;
	}

}
