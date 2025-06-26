package dev.markconley.chess.engine.move.handler;

import dev.markconley.chess.engine.core.Position;
import dev.markconley.chess.engine.pieces.Piece;

public interface SpecialMoveHandler {
	boolean canHandle(Piece piece, Position from, Position to);
}
