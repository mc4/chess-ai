package dev.markconley.chess.engine.move.service;

import dev.markconley.chess.engine.core.Position;
import dev.markconley.chess.engine.move.Move;
import dev.markconley.chess.engine.move.handler.CastlingMoveHandler;
import dev.markconley.chess.engine.move.handler.EnPassantMoveHandler;
import dev.markconley.chess.engine.move.handler.PromotionMoveHandler;
import dev.markconley.chess.engine.move.promotion.PromotionStrategy;
import dev.markconley.chess.engine.pieces.Piece;
import dev.markconley.chess.engine.state.BoardState;

public class SpecialMoveService {

    private final CastlingMoveHandler castlingMoveHandler;
    private final EnPassantMoveHandler enPassantMoveHandler;
    private final PromotionMoveHandler promotionMoveHandler;

    public SpecialMoveService() {
    	this.castlingMoveHandler = CastlingMoveHandler.getInstance();
    	this.enPassantMoveHandler = EnPassantMoveHandler.getInstance();
        this.promotionMoveHandler = PromotionMoveHandler.getInstance();
    }

    public Move trySpecialMove(BoardState state, Piece piece, Position from, Position to, PromotionStrategy promotionStrategy) {
    	
        if (castlingMoveHandler.canHandle(piece, from, to)) {
            return castlingMoveHandler.handle(state, piece, from, to);
        }

        if (enPassantMoveHandler.canHandle(piece, from, to)) {
            return enPassantMoveHandler.handle(state, piece, from, to);
        }

        if (promotionMoveHandler.canHandle(piece, from, to)) {
            return promotionMoveHandler.handle(state, piece, from, to, promotionStrategy);
        }

        return null; // No special move applicable
    }

    public CastlingMoveHandler getCastlingMoveHandler() {
        return castlingMoveHandler;
    }
    
    public void updateCastlingRightsOnStandardMove(BoardState state, Piece movedPiece, Piece capturedPiece, Position from, Position to) {
        castlingMoveHandler.updateCastlingRightsOnMove(state, movedPiece, from);
        if (capturedPiece != null) {
            castlingMoveHandler.updateCastlingRightsOnRookCapture(state, capturedPiece, to);
        }
    }
    
}
