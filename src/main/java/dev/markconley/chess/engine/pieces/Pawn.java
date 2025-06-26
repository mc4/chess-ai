package dev.markconley.chess.engine.pieces;

import dev.markconley.chess.engine.core.Color;

public class Pawn extends Piece {

	public Pawn(Color color) {
		super(color);
	}

	@Override
	public PieceType getPieceType() {
		return PieceType.PAWN;
	}

	@Override
	public Pawn copy() {
		Pawn copy = new Pawn(this.getColor());
        copy.setPosition(this.getPosition()); 
        return copy;
	}

}
