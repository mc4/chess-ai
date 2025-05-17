package com.optimism.chess.engine.pieces;

import java.util.List;

import com.optimism.chess.engine.board.Board;
import com.optimism.chess.engine.core.Color;
import com.optimism.chess.engine.move.Move;
import com.optimism.chess.engine.move.MoveGenerator;

public class King extends Piece {

	public King(Color color) {
		super(color);
	}

	@Override
	public List<Move> getPossibleMoves(Board board) {
		return MoveGenerator.generateKingMoves(board, this);
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
