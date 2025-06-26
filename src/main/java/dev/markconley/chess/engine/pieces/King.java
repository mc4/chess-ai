package dev.markconley.chess.engine.pieces;

import java.util.List;

import dev.markconley.chess.engine.board.Board;
import dev.markconley.chess.engine.core.Color;
import dev.markconley.chess.engine.move.Move;
import dev.markconley.chess.engine.move.MoveGenerator;
import dev.markconley.chess.engine.move.service.SpecialMoveService;

public class King extends Piece {

	public King(Color color) {
		super(color);
	}

	@Override
	public List<Move> getPossibleMoves(Board board, SpecialMoveService specialMoveService) {
		return MoveGenerator.generateKingMoves(board, this, specialMoveService);
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
