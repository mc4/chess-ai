package com.optimism.chess.engine.pieces;

import java.util.List;

import com.optimism.chess.engine.board.Board;
import com.optimism.chess.engine.core.Color;
import com.optimism.chess.engine.move.Move;
import com.optimism.chess.engine.move.MoveGenerator;

public class Knight extends Piece {

	public Knight(Color color) {
		super(color);
	}

	@Override
	public List<Move> getPossibleMoves(Board board) {
		return MoveGenerator.generateJumpingMoves(board, this);
	}

	@Override
	public PieceType getPieceType() {
		return PieceType.KNIGHT;
	}

	@Override
	public Knight copy() {
		Knight copy = new Knight(this.getColor());
        copy.setPosition(this.getPosition()); 
        return copy;
	}

}
