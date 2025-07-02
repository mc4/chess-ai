package dev.markconley.chess.game;

import dev.markconley.chess.engine.move.Move;
import dev.markconley.chess.engine.state.BoardState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistoryTracker {
	private final List<Move> moveHistory = new ArrayList<>();
	private final List<BoardState> stateHistory = new ArrayList<>();

	public void record(BoardState state, Move move) {
		moveHistory.add(move);
		stateHistory.add(state.copy());
	}

	public List<Move> getMoves() {
		return Collections.unmodifiableList(moveHistory);
	}

	public List<BoardState> getStates() {
		return Collections.unmodifiableList(stateHistory);
	}
	
}
