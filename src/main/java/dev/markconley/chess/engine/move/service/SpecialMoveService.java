package dev.markconley.chess.engine.move.service;

import dev.markconley.chess.engine.board.Board;
import dev.markconley.chess.engine.core.Position;
import dev.markconley.chess.engine.move.Move;
import dev.markconley.chess.engine.move.handler.CastlingMoveHandler;
import dev.markconley.chess.engine.move.handler.EnPassantMoveHandler;
import dev.markconley.chess.engine.move.handler.PromotionMoveHandler;
import dev.markconley.chess.engine.move.promotion.PromotionStrategy;
import dev.markconley.chess.engine.pieces.Piece;

public class SpecialMoveService {

    private final CastlingMoveHandler castlingMoveHandler;
    private final EnPassantMoveHandler enPassantMoveHandler;
    private final PromotionMoveHandler promotionMoveHandler;

    public SpecialMoveService() {
    	this.castlingMoveHandler = CastlingMoveHandler.getInstance();
    	this.enPassantMoveHandler = EnPassantMoveHandler.getInstance();
        this.promotionMoveHandler = PromotionMoveHandler.getInstance();
    }

    public Move trySpecialMove(Board board, Piece piece, Position from, Position to, PromotionStrategy promotionStrategy) {
        if (castlingMoveHandler.canHandle(piece, from, to)) {
            return castlingMoveHandler.handle(board, piece, from, to);
        }

        if (enPassantMoveHandler.canHandle(piece, from, to)) {
            return enPassantMoveHandler.handle(board, piece, from, to);
        }

        if (promotionMoveHandler.canHandle(piece, from, to)) {
            return promotionMoveHandler.handle(board, piece, from, to, promotionStrategy);
        }

        return null; // No special move applicable
    }

    public CastlingMoveHandler getCastlingMoveHandler() {
        return castlingMoveHandler;
    }
    
    public void updateCastlingRightsOnStandardMove(Board board, Piece movedPiece, Piece capturedPiece, Position from, Position to) {
        castlingMoveHandler.updateCastlingRightsOnMove(board, movedPiece, from);
        if (capturedPiece != null) {
            castlingMoveHandler.updateCastlingRightsOnRookCapture(board, capturedPiece, to);
        }
    }
    
}
