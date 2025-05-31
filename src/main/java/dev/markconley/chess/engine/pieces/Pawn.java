package dev.markconley.chess.engine.pieces;

import java.util.List;

import dev.markconley.chess.engine.board.Board;
import dev.markconley.chess.engine.core.Color;
import dev.markconley.chess.engine.move.Move;
import dev.markconley.chess.engine.move.MoveGenerator;

public class Pawn extends Piece {

	public Pawn(Color color) {
		super(color);
	}

	@Override
	public List<Move> getPossibleMoves(Board board) {
		return MoveGenerator.generatePawnMoves(board, this);
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
