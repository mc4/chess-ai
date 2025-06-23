package dev.markconley.chess.engine.move.enpassant;

import dev.markconley.chess.engine.board.Board;
import dev.markconley.chess.engine.core.Position;
import dev.markconley.chess.engine.move.Move;
import dev.markconley.chess.engine.move.MoveFactory;
import dev.markconley.chess.engine.pieces.Pawn;
import dev.markconley.chess.engine.pieces.Piece;

public class DefaultEnPassantStrategy implements EnPassantStrategy {

	@Override
	public boolean isEnPassant(Position from, Position to, Board board) {
		Position enPassantTarget = board.getEnPassantTarget();
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
	public Move createEnPassantMove(Position from, Position to, Piece pawn, Board board) {
		Position capturedPawnPos = Position.of(from.getRow(), to.getCol());
		Piece captured = board.getPieceAt(capturedPawnPos);
		return MoveFactory.enPassant(from, to, pawn, captured);
	}
}
