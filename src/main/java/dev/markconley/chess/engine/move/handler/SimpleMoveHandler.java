package dev.markconley.chess.engine.move.handler;

import dev.markconley.chess.engine.core.Position;
import dev.markconley.chess.engine.move.Move;
import dev.markconley.chess.engine.pieces.Piece;
import dev.markconley.chess.engine.state.BoardState;

public interface SimpleMoveHandler extends SpecialMoveHandler {
    Move handle(BoardState state, Piece piece, Position from, Position to);
}
