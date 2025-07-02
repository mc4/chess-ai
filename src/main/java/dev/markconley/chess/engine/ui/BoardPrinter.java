package dev.markconley.chess.engine.ui;

import dev.markconley.chess.engine.board.Board;
import dev.markconley.chess.engine.core.Color;
import dev.markconley.chess.engine.core.Position;
import dev.markconley.chess.engine.pieces.Piece;

public class BoardPrinter {
	
	public String getBoardString(Board board) {
		StringBuilder sb = new StringBuilder();
		final String separator = "===============================";
		sb.append(separator).append("\n");
		sb.append("  a b c d e f g h\n");
		for (int row = 7; row >= 0; row--) {
			sb.append(row + 1).append(" ");
			for (int col = 0; col < 8; col++) {
				Piece piece = board.getPieceAt(Position.of(row, col));
				String symbol = piece == null ? "." : getSymbol(piece);
				sb.append(symbol).append(" ");
			}
			sb.append(row + 1).append("\n");
		}
		sb.append("  a b c d e f g h\n");
		sb.append(separator);
		return sb.toString();
	}
	
	private String getSymbol(Piece piece) {
		String base = switch (piece.getPieceType()) {
			case KING -> "K";
			case QUEEN -> "Q";
			case ROOK -> "R";
			case BISHOP -> "B";
			case KNIGHT -> "N";
			case PAWN -> "P";
		};
		return piece.getColor() == Color.WHITE ? base : base.toLowerCase();
	}

}
