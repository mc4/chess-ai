package dev.markconley.chess.engine.pieces;

import java.util.List;

import dev.markconley.chess.engine.board.Board;
import dev.markconley.chess.engine.core.Color;
import dev.markconley.chess.engine.move.Move;
import dev.markconley.chess.engine.move.MoveGenerator;

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
