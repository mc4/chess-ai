package dev.markconley.chess.engine.move.promotion;

import dev.markconley.chess.engine.core.Color;
import dev.markconley.chess.engine.pieces.Piece;

public interface PromotionStrategy {
	Piece choosePromotion(Color color);
}
