package com.optimism.chess.engine.pieces;

import java.util.List;

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

	// Getters and setters
	public Color getColor() {
		return color;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

}
