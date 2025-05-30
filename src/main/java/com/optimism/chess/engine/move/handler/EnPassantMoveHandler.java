package com.optimism.chess.engine.move.handler;

import com.optimism.chess.engine.board.Board;
import com.optimism.chess.engine.core.Position;
import com.optimism.chess.engine.move.Move;
import com.optimism.chess.engine.pieces.Piece;

public class EnPassantMoveHandler implements SpecialMoveHandler {
	

	@Override
	public boolean canHandle(Piece piece, Position from, Position to) {
		return false;
	}

	@Override
	public Move handle(Board board, Piece piece, Position from, Position to) {
		return null;
	}

}
