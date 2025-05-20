package com.optimism.chess.engine.core;

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
		return "(" + row + ", " + col + ")";
	}
}
