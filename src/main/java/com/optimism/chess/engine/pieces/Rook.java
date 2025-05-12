package com.optimism.chess.engine.pieces;

import java.util.List;

import com.optimism.chess.engine.board.Board;
import com.optimism.chess.engine.core.Color;
import com.optimism.chess.engine.move.Move;
import com.optimism.chess.engine.move.MoveGenerator;

public class Rook extends Piece {

	public Rook(Color color) {
		super(color);
	}

	@Override
	public List<Move> getPossibleMoves(Board board) {
		return MoveGenerator.generateSlidingMoves(board, this, Direction.ROOK_DIRECTIONS);
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
