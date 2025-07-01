package dev.markconley.chess.engine.move;

import dev.markconley.chess.engine.board.Board;
import dev.markconley.chess.engine.core.Position;
import dev.markconley.chess.engine.pieces.Piece;
import dev.markconley.chess.engine.state.BoardState;

public class MoveExecutor {

	public void applyMove(BoardState state, Move move) {
		Board board = state.getBoard();
		Piece movedPiece = move.movedPiece();
		Position from = move.from();
		Position to = move.to();

		board.setPieceAt(from, null);
		board.setPieceAt(to, movedPiece);
		movedPiece.setPosition(to);

		if (move.isEnPassant()) {
			Position capturedPawnPos = new Position(from.getRow(), to.getCol());
			board.setPieceAt(capturedPawnPos, null);
		}

		if (move.isCastling()) {
			handleCastling(board, from, to);
		}

		if (move.isPromotion()) {
			Piece promoted = move.promotionPiece();
			promoted.setPosition(to);
			board.setPieceAt(to, promoted);
		}
	}

	private void handleCastling(Board board, Position from, Position to) {
		int row = from.getRow();
		int toCol = to.getCol();
		if (toCol == 6) { // Kingside castling
			moveRookForCastling(board, row, 7, 5);
		} else if (toCol == 2) { // Queenside castling
			moveRookForCastling(board, row, 0, 3);
		}
	}

	private void moveRookForCastling(Board board, int row, int fromCol, int toCol) {
		Position rookFrom = new Position(row, fromCol);
		Position rookTo = new Position(row, toCol);
		Piece rook = board.getPieceAt(rookFrom);
		board.setPieceAt(rookTo, rook);
		board.setPieceAt(rookFrom, null);
		if (rook != null) {
			rook.setPosition(rookTo);
		}
	}
	
}
