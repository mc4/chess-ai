package dev.markconley.chess.engine.pieces;

import dev.markconley.chess.engine.core.Color;

public class Queen extends Piece {

	public Queen(Color color) {
		super(color);
	}

	@Override
	public PieceType getPieceType() {
		return PieceType.QUEEN;
	}

	@Override
	public Queen copy() {
		Queen copy = new Queen(this.getColor());
        copy.setPosition(this.getPosition()); 
        return copy;
	}

}
