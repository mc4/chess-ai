package dev.markconley.chess.engine.state;

import dev.markconley.chess.engine.board.Board;
import dev.markconley.chess.engine.core.Color;
import dev.markconley.chess.engine.core.Position;
import dev.markconley.chess.engine.pieces.Piece;
import dev.markconley.chess.engine.pieces.PieceType;

public class CastlingValidator {

	public static boolean canCastle(Board board, Position from, Position to, Color color) {
		return hasCastlingRights(board, from, to, color) 
				&& isRookInPlace(board, from, to, color)
				&& isPathClear(board, from, to) 
				&& !GameStateEvaluator.isInCheck(board, color)
				&& isSafePath(board, color, from, to);
	}

	private static boolean hasCastlingRights(Board board, Position from, Position to, Color color) {
		CastlingRights rights = board.getCastlingRights();
		if (color == Color.WHITE) {
			if (from.equals(Position.of("e1")) && to.equals(Position.of("g1"))) {
				return rights.whiteCanCastleKingside();
			}
			if (from.equals(Position.of("e1")) && to.equals(Position.of("c1"))) {
				return rights.whiteCanCastleQueenside();
			}
		} else {
			if (from.equals(Position.of("e8")) && to.equals(Position.of("g8"))) {
				return rights.blackCanCastleKingside();
			}
			if (from.equals(Position.of("e8")) && to.equals(Position.of("c8"))) {
				return rights.blackCanCastleQueenside();
			}
		}
		return false;
	}

	private static boolean isRookInPlace(Board board, Position from, Position to, Color color) {
		if (color == Color.WHITE) {
			if (to.equals(Position.of("g1"))) {
				return isRookAt(board, Position.of("h1"), Color.WHITE);
			}
			if (to.equals(Position.of("c1"))) {
				return isRookAt(board, Position.of("a1"), Color.WHITE);
			}
		} else {
			if (to.equals(Position.of("g8"))) {
				return isRookAt(board, Position.of("h8"), Color.BLACK);
			}
			if (to.equals(Position.of("c8"))) {
				return isRookAt(board, Position.of("a8"), Color.BLACK);
			}
		}
		return false;
	}

	private static boolean isRookAt(Board board, Position pos, Color color) {
		Piece piece = board.getPieceAt(pos);
		return piece != null && piece.getColor() == color && piece.getPieceType() == PieceType.ROOK;
	}

	private static boolean isPathClear(Board board, Position from, Position to) {
		int row = from.getRow();
		int startCol = Math.min(from.getCol(), to.getCol());
		int endCol = Math.max(from.getCol(), to.getCol());

		for (int col = startCol + 1; col < endCol; col++) {
			if (board.getPieceAt(Position.of(row, col)) != null) {
				return false;
			}
		}
		return true;
	}

	private static boolean isSafePath(Board board, Color color, Position from, Position to) {
		int row = from.getRow();
		int step = (to.getCol() > from.getCol()) ? 1 : -1;

		for (int i = 0; i <= 2; i++) {
			Position pos = new Position(row, from.getCol() + i * step);
			if (board.isSquareAttacked(pos, color.opposite())) {
				return false;
			}
		}
		
		return true;
	}

}
