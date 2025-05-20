package com.optimism.chess.engine.pieces;

import java.util.List;
import java.util.Objects;

import com.optimism.chess.engine.board.Board;
import com.optimism.chess.engine.core.Color;
import com.optimism.chess.engine.core.Copyable;
import com.optimism.chess.engine.core.Position;
import com.optimism.chess.engine.move.Move;

public abstract class Piece implements Copyable<Piece> {
	private Color color;
	private Position position;

	public Piece(Color color) {
		this.color = color;
	}

	public abstract List<Move> getPossibleMoves(Board board);

	public abstract PieceType getPieceType();

	@Override
	public abstract Piece copy();

	public Color getColor() {
		return color;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Piece piece = (Piece) o;
		return color == piece.color && Objects.equals(position, piece.position);
	}

	@Override
	public int hashCode() {
		return Objects.hash(getClass(), color, position);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" + color + " at " + position + ")";
	}

}
