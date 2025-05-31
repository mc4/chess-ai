package dev.markconley.chess.engine.core;

import java.util.Objects;

public final class Position {

	private final int row;
	private final int col;

	public Position(int row, int col) {
		if (!isValid(row, col)) {
			throw new IllegalArgumentException("Invalid board position: " + row + ", " + col);
		}
		this.row = row;
		this.col = col;
	}
	
	public Position(String algebraic) {
		if (algebraic == null || algebraic.length() != 2) {
			throw new IllegalArgumentException("Invalid position: " + algebraic);
		}

		char file = algebraic.charAt(0);
		char rank = algebraic.charAt(1);

		if (file < 'a' || file > 'h' || rank < '1' || rank > '8') {
			throw new IllegalArgumentException("Invalid position: " + algebraic);
		}

		this.col = file - 'a'; // a-h maps to 0-7
		this.row = rank - '1'; // 1-8 maps to 0-7
	}

	public String toAlgebraic() {
		char file = (char) ('a' + col);
		char rank = (char) ('1' + row);
		return "" + file + rank;
	}
	
    public static Position of(String algebraic) {
        return new Position(algebraic);
    }
	
    public static Position of(int row, int col) {
        return new Position(row, col);
    }

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public static boolean isValid(int row, int col) {
		return row >= 0 && row < 8 && col >= 0 && col < 8;
	}

	public static boolean isValid(Position pos) {
		return pos != null && isValid(pos.getRow(), pos.getCol());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Position)) {
			return false;
		}
		Position position = (Position) o;
		return row == position.row && col == position.col;
	}

	@Override
	public int hashCode() {
		return Objects.hash(row, col);
	}

	@Override
	public String toString() {
		return toAlgebraic();
	}
}
