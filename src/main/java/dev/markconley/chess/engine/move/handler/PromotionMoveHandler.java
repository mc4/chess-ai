package dev.markconley.chess.engine.move.handler;

import dev.markconley.chess.engine.board.Board;
import dev.markconley.chess.engine.core.Color;
import dev.markconley.chess.engine.core.Position;
import dev.markconley.chess.engine.move.Move;
import dev.markconley.chess.engine.move.MoveFactory;
import dev.markconley.chess.engine.move.promotion.PromotionStrategy;
import dev.markconley.chess.engine.pieces.Pawn;
import dev.markconley.chess.engine.pieces.Piece;

public class PromotionMoveHandler implements PromotionHandler {
	
	private static final PromotionMoveHandler INSTANCE = new PromotionMoveHandler();

	private PromotionMoveHandler() { }

	public static PromotionMoveHandler getInstance() {
		return INSTANCE;
	}

	@Override
	public boolean canHandle(Piece piece, Position from, Position to) {
		if (!(piece instanceof Pawn)) {
			return false;
		}

		int targetRow = to.getRow();
		Color color = piece.getColor();

		return (color == Color.WHITE && targetRow == 7) || (color == Color.BLACK && targetRow == 0);
	}

    @Override
    public Move handle(Board board, Piece piece, Position from, Position to, PromotionStrategy strategy) {
        Piece promoted = strategy.choosePromotion(piece.getColor());
        promoted.setPosition(to);
        board.setPieceAt(to, promoted);
        board.setPieceAt(from, null);
        return MoveFactory.promotion(from, to, piece, promoted);
    }

}
