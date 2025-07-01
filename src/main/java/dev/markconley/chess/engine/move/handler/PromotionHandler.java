package dev.markconley.chess.engine.move.handler;

import dev.markconley.chess.engine.core.Position;
import dev.markconley.chess.engine.move.Move;
import dev.markconley.chess.engine.move.promotion.PromotionStrategy;
import dev.markconley.chess.engine.pieces.Piece;
import dev.markconley.chess.engine.state.BoardState;

public interface PromotionHandler extends SpecialMoveHandler {
    Move handle(BoardState state, Piece piece, Position from, Position to, PromotionStrategy strategy);
}
