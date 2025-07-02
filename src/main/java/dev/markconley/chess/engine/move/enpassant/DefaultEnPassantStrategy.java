package dev.markconley.chess.engine.move.enpassant;

import dev.markconley.chess.engine.board.Board;
import dev.markconley.chess.engine.core.Position;
import dev.markconley.chess.engine.move.LastMove;
import dev.markconley.chess.engine.move.Move;
import dev.markconley.chess.engine.move.MoveFactory;
import dev.markconley.chess.engine.pieces.Pawn;
import dev.markconley.chess.engine.pieces.Piece;
import dev.markconley.chess.engine.state.BoardState;

public class DefaultEnPassantStrategy implements EnPassantStrategy {

	@Override
	public boolean isEnPassant(Position from, Position to, BoardState state) {
		Board board = state.getBoard();
		Position enPassantTarget = getEnPassantTarget(state);
		if (enPassantTarget == null || !enPassantTarget.equals(to)) {
			return false;
		}

		Piece piece = board.getPieceAt(from);
		if (!(piece instanceof Pawn)) {
			return false;
		}

		Position capturedPawnPos = Position.of(from.getRow(), to.getCol());
		Piece captured = board.getPieceAt(capturedPawnPos);

		return captured instanceof Pawn && captured.getColor() != piece.getColor();
	}

	@Override
	public Move createEnPassantMove(Position from, Position to, Piece pawn, BoardState state) {
		Position capturedPawnPos = Position.of(from.getRow(), to.getCol());
		Piece captured = state.getBoard().getPieceAt(capturedPawnPos);
		return MoveFactory.enPassant(from, to, pawn, captured);
	}
	
	private Position getEnPassantTarget(BoardState state) {
		LastMove lastMove = state.getLastMove();
		if (lastMove == null) {
			return null;
		}

		Piece movedPiece = lastMove.piece();
		Position from = lastMove.from();
		Position to = lastMove.to();

		if (!(movedPiece instanceof Pawn)) {
			return null;
		}

		int rowDiff = Math.abs(from.getRow() - to.getRow());
		if (rowDiff != 2) {
			return null;
		}

		// Return the square "behind" the pawn that moved two steps forward
		int enPassantRow = (from.getRow() + to.getRow()) / 2;
		int col = to.getCol();

		return Position.of(enPassantRow, col);
	}
	
}
