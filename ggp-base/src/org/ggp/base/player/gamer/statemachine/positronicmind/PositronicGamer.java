package org.ggp.base.player.gamer.statemachine.positronicmind;

import java.util.List;

import org.ggp.base.apps.player.detail.DetailPanel;
import org.ggp.base.apps.player.detail.SimpleDetailPanel;
import org.ggp.base.player.gamer.event.GamerSelectedMoveEvent;
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

	// Default implementations follow, can be overridden
	public Move bestMove(Role role, MachineState state) throws MoveDefinitionException, GoalDefinitionException, TransitionDefinitionException {
		return null;
	}
	
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
}
