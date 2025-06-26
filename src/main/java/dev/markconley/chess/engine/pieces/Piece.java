package dev.markconley.chess.engine.pieces;

import java.util.List;
import java.util.Objects;

import dev.markconley.chess.engine.board.Board;
import dev.markconley.chess.engine.core.Color;
import dev.markconley.chess.engine.core.Copyable;
import dev.markconley.chess.engine.core.Position;
import dev.markconley.chess.engine.move.Move;
import dev.markconley.chess.engine.move.MoveGenerator;
import dev.markconley.chess.engine.move.service.SpecialMoveService;

public abstract class Piece implements Copyable<Piece> {
	private Color color;
	private Position position;

	public Piece(Color color) {
		this.color = color;
	}

//	public abstract List<Move> getPossibleMoves(Board board);
//	
//	public List<Move> getPossibleMoves(Board board, SpecialMoveService specialMoveService) {
//	    return MoveGenerator.generateMoves(board, this, specialMoveService);
//	}
	

	public List<Move> getPossibleMoves(Board board) {
	    return getPossibleMoves(board, board.getSpecialMoveService());
	}

	public List<Move> getPossibleMoves(Board board, SpecialMoveService specialMoveService) {
	    return MoveGenerator.generateMoves(board, this, specialMoveService);
	}

	public abstract PieceType getPieceType();

	@Override
	public abstract Piece copy();

	public Color getColor() {
		return color;
	}

	public Position getPosition() {
		if (position == null) {
			throw new IllegalStateException("Position not set for piece: " + this);
		}
		return position;
	}

	public void setPosition(Position position) {
		if (position == null) {
			throw new IllegalArgumentException("Cannot set null position on piece: " + this);
		}
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
