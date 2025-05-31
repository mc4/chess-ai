package dev.markconley.chess.engine.pieces;

import java.util.List;

import dev.markconley.chess.engine.board.Board;
import dev.markconley.chess.engine.core.Color;
import dev.markconley.chess.engine.move.Move;
import dev.markconley.chess.engine.move.MoveGenerator;

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
