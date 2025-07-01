package dev.markconley.chess.engine.pieces;

import java.util.List;

import dev.markconley.chess.engine.core.Color;
import dev.markconley.chess.engine.move.Move;
import dev.markconley.chess.engine.move.MoveGenerator;
import dev.markconley.chess.engine.state.BoardState;

public class King extends Piece {

	public King(Color color) {
		super(color);
	}

	@Override
	public List<Move> getPossibleMoves(BoardState state) {
	    return MoveGenerator.generateKingMoves(state, this, state.getSpecialMoveService());
	}

	@Override
	public PieceType getPieceType() {
		return PieceType.KING;
	}

	@Override
	public King copy() {
		King copy = new King(this.getColor());
        copy.setPosition(this.getPosition()); 
        return copy;
	}

}
