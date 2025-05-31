package dev.markconley.chess.engine.move.handler;

import dev.markconley.chess.engine.board.Board;
import dev.markconley.chess.engine.core.Position;
import dev.markconley.chess.engine.move.Move;
import dev.markconley.chess.engine.pieces.Piece;

public interface SpecialMoveHandler {
	boolean canHandle(Piece piece, Position from, Position to);
	Move handle(Board board, Piece piece, Position from, Position to);
}
