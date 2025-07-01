package dev.markconley.chess.engine.validation;

import java.util.List;

import dev.markconley.chess.engine.core.Color;
import dev.markconley.chess.engine.core.Position;
import dev.markconley.chess.engine.move.Move;
import dev.markconley.chess.engine.move.MoveExecutor;
import dev.markconley.chess.engine.pieces.Piece;
import dev.markconley.chess.engine.state.BoardState;
import dev.markconley.chess.engine.state.GameStateEvaluator;

/**
 * 
 * @param move
 * @param currentTurn
 * 
 * A move is legal if:
 *
 * 1. The piece at the from square belongs to the player whose turn it is.
 * 2. The piece can move from from to to based on movement rules (including special rules: castling, en passant, promotion).
 * 3. The destination square does not contain a friendly piece.
 * 4. The move does not leave the moving player in check.
 * 
 * @return
 */
public class MoveValidator {
	
	public boolean isMoveLegal(BoardState state, Move move, Color currentTurn) {
		Position from = move.from();
		Position to = move.to();
		Piece movingPiece = state.getBoard().getPieceAt(from);

		// Rule 1: Must be a piece of current turn
		if (movingPiece == null || movingPiece.getColor() != currentTurn) {
			return false;
		}

		// Rule 2 and 3: Move must be in the piece's possible moves
		List<Move> legalMoves = movingPiece.getPossibleMoves(state)
				.stream().filter(m -> m.to().equals(to))
				.toList();

		if (legalMoves.isEmpty()) {
			return false;
		}

		// Rule 4: Simulate and check for check
		BoardState clone = state.copy();
		MoveExecutor executor = new MoveExecutor();
		executor.applyMove(state, move);
		return !GameStateEvaluator.isInCheck(clone, currentTurn);
	}

}
