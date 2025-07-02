package dev.markconley.chess.game;

import java.util.List;
import java.util.Optional;

import dev.markconley.chess.engine.core.Color;
import dev.markconley.chess.engine.move.Move;
import dev.markconley.chess.engine.move.MoveExecutor;
import dev.markconley.chess.engine.state.BoardState;
import dev.markconley.chess.io.InputProvider;
import dev.markconley.chess.io.OutputHandler;

public class TurnManager {
	private final MoveExecutor moveExecutor;

	public TurnManager(MoveExecutor moveExecutor) {
		this.moveExecutor = moveExecutor;
	}

	public Move getValidMove(Color turn, BoardState boardState, List<Move> legalMoves, InputProvider input,
			OutputHandler output) {
		
		if (legalMoves.isEmpty()) {
			return null;
		}

		while (true) {
			output.promptMove(turn, legalMoves);
			Optional<Move> maybeMove = input.getMove(legalMoves);

			if (maybeMove.isEmpty()) {
				output.displayMessage("No input received.");
				continue;
			}

			Move move = maybeMove.get();
			if (legalMoves.contains(move)) {
				return move;
			}

			output.displayMessage("Illegal move. Try again.");
		}
	}

	public void executeMove(BoardState boardState, Move move) {
		moveExecutor.applyMove(boardState, move);
	}

}