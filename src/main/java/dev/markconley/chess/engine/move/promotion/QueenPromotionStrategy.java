package dev.markconley.chess.engine.move.promotion;

import dev.markconley.chess.engine.core.Color;
import dev.markconley.chess.engine.pieces.Piece;
import dev.markconley.chess.engine.pieces.Queen;

public class QueenPromotionStrategy implements PromotionStrategy {
	@Override
	public Piece choosePromotion(Color color) {
		return new Queen(color);
	}
}