package dev.markconley.chess.engine.pieces;

import java.util.List;

import dev.markconley.chess.engine.board.Board;
import dev.markconley.chess.engine.core.Color;
import dev.markconley.chess.engine.move.Move;
import dev.markconley.chess.engine.move.MoveGenerator;

public class Bishop extends Piece {

	public Bishop(Color color) {
		super(color);
	}

	@Override
	public List<Move> getPossibleMoves(Board board) {
		return MoveGenerator.generateSlidingMoves(board, this, Direction.BISHOP_DIRECTIONS);
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
