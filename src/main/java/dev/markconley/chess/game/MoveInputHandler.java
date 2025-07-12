package dev.markconley.chess.game;

import java.util.List;
import java.util.Optional;

import dev.markconley.chess.engine.core.Color;
import dev.markconley.chess.engine.move.Move;
import dev.markconley.chess.engine.state.BoardState;
import dev.markconley.chess.io.InputProvider;
import dev.markconley.chess.io.OutputHandler;

public class MoveInputHandler {

	public MoveInputHandler() {
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

}