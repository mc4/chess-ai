package com.optimism.chess.engine.state;

import com.optimism.chess.engine.board.Board;
import com.optimism.chess.engine.core.Color;
import com.optimism.chess.engine.core.Position;
import com.optimism.chess.engine.pieces.Piece;
import com.optimism.chess.engine.pieces.PieceType;

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
			if (from.equals(new Position("e1")) && to.equals(new Position("g1"))) {
				return rights.whiteCanCastleKingside();
			}
			if (from.equals(new Position("e1")) && to.equals(new Position("c1"))) {
				return rights.whiteCanCastleQueenside();
			}
		} else {
			if (from.equals(new Position("e8")) && to.equals(new Position("g8"))) {
				return rights.blackCanCastleKingside();
			}
			if (from.equals(new Position("e8")) && to.equals(new Position("c8"))) {
				return rights.blackCanCastleQueenside();
			}
		}
		return false;
	}

	private static boolean isRookInPlace(Board board, Position from, Position to, Color color) {
		if (color == Color.WHITE) {
			if (to.equals(new Position("g1"))) {
				return isRookAt(board, new Position("h1"), Color.WHITE);
			}
			if (to.equals(new Position("c1"))) {
				return isRookAt(board, new Position("a1"), Color.WHITE);
			}
		} else {
			if (to.equals(new Position("g8"))) {
				return isRookAt(board, new Position("h8"), Color.BLACK);
			}
			if (to.equals(new Position("c8"))) {
				return isRookAt(board, new Position("a8"), Color.BLACK);
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
			if (board.getPieceAt(new Position(row, col)) != null) {
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
