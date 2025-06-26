package dev.markconley.chess.engine.move.handler;

import dev.markconley.chess.engine.board.Board;
import dev.markconley.chess.engine.core.Color;
import dev.markconley.chess.engine.core.Position;
import dev.markconley.chess.engine.move.LastMove;
import dev.markconley.chess.engine.move.Move;
import dev.markconley.chess.engine.move.MoveFactory;
import dev.markconley.chess.engine.pieces.Pawn;
import dev.markconley.chess.engine.pieces.Piece;

public class EnPassantMoveHandler implements SimpleMoveHandler {
	
	private static final EnPassantMoveHandler INSTANCE = new EnPassantMoveHandler();

	private EnPassantMoveHandler() { }

	public static EnPassantMoveHandler getInstance() {
		return INSTANCE;
	}

	@Override
	public boolean canHandle(Piece piece, Position from, Position to) {
		if (!(piece instanceof Pawn)) {
			return false;
		}

		int forward = (piece.getColor() == Color.WHITE) ? 1 : -1;
		int colDiff = Math.abs(from.getCol() - to.getCol());
		int rowDiff = to.getRow() - from.getRow();

		return colDiff == 1 && rowDiff == forward;
	}

	@Override
	public Move handle(Board board, Piece piece, Position from, Position to) {
		if (!(piece instanceof Pawn)) {
			return null;
		}

		LastMove lastMove = board.getLastMove();
		if (lastMove == null) {
			return null;
		}

		Piece lastMoved = board.getPieceAt(lastMove.to());
		if (!(lastMoved instanceof Pawn)) {
			return null;
		}

		if (!isValidEnPassant((Pawn) piece, from, to, lastMove)) {
			return null;
		}

		performEnPassant(board, (Pawn) piece, from, to, lastMove.to());
		return MoveFactory.enPassant(from, to, piece, lastMoved);
	}

	private boolean isValidEnPassant(Pawn currentPawn, Position from, Position to, LastMove lastMove) {
		Color color = currentPawn.getColor();

		int expectedStartRow = (color == Color.WHITE) ? 6 : 1;
		int expectedPassRow = (color == Color.WHITE) ? 4 : 3;

		Position lastFrom = lastMove.from();
		Position lastTo = lastMove.to();

		if (lastFrom.getRow() != expectedStartRow || lastTo.getRow() != expectedPassRow) {
			return false;
		}

		if (lastTo.getRow() != from.getRow()) {
			return false;
		}

		return Math.abs(lastTo.getCol() - from.getCol()) == 1;
	}

	private void performEnPassant(Board board, Pawn capturingPawn, Position from, Position to,
			Position capturedPawnPos) {
		board.setPieceAt(capturedPawnPos, null);
		board.setPieceAt(to, capturingPawn);
		capturingPawn.setPosition(to);
		board.setPieceAt(from, null);
	}

}
