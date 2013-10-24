package org.ggp.base.player.gamer.statemachine.positronicmind;

import java.util.List;

import org.ggp.base.apps.player.detail.DetailPanel;
import org.ggp.base.apps.player.detail.SimpleDetailPanel;
import org.ggp.base.player.gamer.exception.GamePreviewException;
import org.ggp.base.player.gamer.statemachine.StateMachineGamer;
import org.ggp.base.util.game.Game;
import org.ggp.base.util.statemachine.MachineState;
import org.ggp.base.util.statemachine.Move;
import org.ggp.base.util.statemachine.Role;
import org.ggp.base.util.statemachine.StateMachine;
import org.ggp.base.util.statemachine.cache.CachedStateMachine;
import org.ggp.base.util.statemachine.exceptions.GoalDefinitionException;
import org.ggp.base.util.statemachine.exceptions.MoveDefinitionException;
import org.ggp.base.util.statemachine.exceptions.TransitionDefinitionException;
import org.ggp.base.util.statemachine.implementation.prover.ProverStateMachine;

public abstract class PositronicGamer extends StateMachineGamer {
	@Override
	public void stateMachineMetaGame(long timeout) throws TransitionDefinitionException, MoveDefinitionException, GoalDefinitionException
	{
		// Positronic gamers do no metagaming at the beginning of the match.
	}
	
	/** This will currently return "PositronicGamer"
	 * If you are working on : public abstract class MyGamer extends PositronicGamer
	 * Then this function would return "MyPositronicGamer"
	 */
	public String getName() {
		return getClass().getSimpleName();
	}
	
	// This is the default State Machine
	public StateMachine getInitialStateMachine() {
		return new CachedStateMachine(new ProverStateMachine());
	}	

	// This is the default Sample Panel
	public DetailPanel getDetailPanel() {
		return new SimpleDetailPanel();
	}	
	
	@Override
	public void stateMachineStop() {
		// Positronic gamers do no special cleanup when the match ends normally.
	}
	
	@Override
	public void stateMachineAbort() {
		// Positronic gamers do no special cleanup when the match ends abruptly.
	}
	
	@Override
	public void preview(Game g, long timeout) throws GamePreviewException {
		// Positronic gamers do no game previewing.
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
