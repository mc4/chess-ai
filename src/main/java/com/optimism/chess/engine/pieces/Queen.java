package com.optimism.chess.engine.pieces;

import java.util.List;

import com.optimism.chess.engine.board.Board;
import com.optimism.chess.engine.core.Color;
import com.optimism.chess.engine.move.Move;
import com.optimism.chess.engine.move.MoveGenerator;

public class Queen extends Piece {

	public Queen(Color color) {
		super(color);
	}

	@Override
	public List<Move> getPossibleMoves(Board board) {
		return MoveGenerator.generateSlidingMoves(board, this, Direction.QUEEN_DIRECTIONS);
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
