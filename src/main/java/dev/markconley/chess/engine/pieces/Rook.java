package dev.markconley.chess.engine.pieces;

import dev.markconley.chess.engine.core.Color;

public class Rook extends Piece {

	public Rook(Color color) {
		super(color);
	}

	@Override
	public PieceType getPieceType() {
		return PieceType.ROOK;
	}

	@Override
	public Rook copy() {
		Rook copy = new Rook(this.getColor());
        copy.setPosition(this.getPosition()); 
        return copy;
	}

}
