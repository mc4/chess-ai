package dev.markconley.chess.engine.board;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dev.markconley.chess.engine.core.Color;
import dev.markconley.chess.engine.core.Copyable;
import dev.markconley.chess.engine.core.Position;
import dev.markconley.chess.engine.pieces.Piece;
import dev.markconley.chess.engine.pieces.PieceType;

public class Board implements Copyable<Board> {

	private static final int BOARD_SIZE = 8;
	private final Piece[][] board;

	public Board() {
		this.board = new Piece[BOARD_SIZE][BOARD_SIZE];
	}

	public static Board emptyBoard() {
		return new Board();
	}

	public void clearBoard() {
		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int col = 0; col < BOARD_SIZE; col++) {
				board[row][col] = null;
			}
		}
	}
	
	public Board place(String square, Piece piece) {
	    Position pos = Position.of(square);
	    piece.setPosition(pos);
	    setPieceAt(pos, piece);
	    return this;
	}

	public Piece getPieceAt(Position pos) {
		return board[pos.getRow()][pos.getCol()];
	}

	public void setPieceAt(Position pos, Piece piece) {
		board[pos.getRow()][pos.getCol()] = piece;
		if (piece != null) {
			piece.setPosition(pos);
		}
	}

	public void removePieceAt(Position pos) {
		board[pos.getRow()][pos.getCol()] = null;
	}
	
	public boolean isSquareEmpty(Position pos) {
		return getPieceAt(pos) == null;
	}

	public List<Piece> getActivePieces(Predicate<Piece> filter) {
		return streamPieces().filter(filter).collect(Collectors.toList());
	}

	public List<Piece> getActivePieces(Color color) {
		return getActivePieces(p -> p.getColor() == color);
	}

	public List<Piece> getAllActivePieces() {
		return getActivePieces(p -> true);
	}

	public Stream<Piece> streamPieces() {
		List<Piece> pieces = new ArrayList<>();
		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int col = 0; col < BOARD_SIZE; col++) {
				Piece p = board[row][col];
				if (p != null) {
					pieces.add(p);
				}
			}
		}
		return pieces.stream();
	}

	public Position findKingPosition(Color color) {
		return streamPieces()
				.filter(p -> p.getColor() == color && p.getPieceType() == PieceType.KING)
				.map(Piece::getPosition)
				.findFirst()
				.orElseThrow(() -> new IllegalStateException("No king found for color: " + color));
	}

	@Override
	public Board copy() {
		Board newBoard = new Board();
		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int col = 0; col < BOARD_SIZE; col++) {
				Piece piece = this.board[row][col];
				if (piece != null) {
					newBoard.board[row][col] = piece.copy();
				}
			}
		}
		return newBoard;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("  a b c d e f g h\n");
		for (int row = 7; row >= 0; row--) {
			sb.append(row + 1).append(" ");
			for (int col = 0; col < 8; col++) {
				Piece piece = board[row][col];
				String symbol = piece == null ? "." : getSymbol(piece);
				sb.append(symbol).append(" ");
			}
			sb.append(row + 1).append("\n");
		}
		sb.append("  a b c d e f g h\n");
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

	public void printBoard() {
		System.out.println(this);
	}
	
}
