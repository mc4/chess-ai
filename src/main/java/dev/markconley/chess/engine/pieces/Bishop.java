package dev.markconley.chess.engine.pieces;

import dev.markconley.chess.engine.core.Color;

public class Bishop extends Piece {

	public Bishop(Color color) {
		super(color);
	}

	@Override
	public PieceType getPieceType() {
		return PieceType.BISHOP;
	}

	@Override
	public Bishop copy() {
		Bishop copy = new Bishop(this.getColor());
        copy.setPosition(this.getPosition()); 
        return copy;
	}

}
