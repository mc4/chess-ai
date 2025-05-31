package dev.markconley.chess.engine.move.handler;

import dev.markconley.chess.engine.board.Board;
import dev.markconley.chess.engine.core.Position;
import dev.markconley.chess.engine.move.Move;
import dev.markconley.chess.engine.pieces.Piece;

public class PromotionMoveHandler implements SpecialMoveHandler {
	

	@Override
	public boolean canHandle(Piece piece, Position from, Position to) {
		return false;
	}

	@Override
	public Move handle(Board board, Piece piece, Position from, Position to) {
		return null;
	}

}
