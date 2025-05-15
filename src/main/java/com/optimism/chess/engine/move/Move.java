package com.optimism.chess.engine.move;

import com.optimism.chess.engine.core.Position;
import com.optimism.chess.engine.pieces.Piece;

import java.util.Objects;

public record Move(
    Position from,
    Position to,
    Piece movedPiece,
    Piece capturedPiece,
    boolean isCastling,
    boolean isEnPassant,
    Piece promotionPiece
) {

    public Move {
        Objects.requireNonNull(from, "From must not be null");
        Objects.requireNonNull(to, "To must not be null");
        Objects.requireNonNull(movedPiece, "Moved piece must not be null");
    }

    public boolean isPromotion() {
        return promotionPiece != null;
    }

    @Override
    public String toString() {
        return from + " -> " + to + (isPromotion() ? " (promotes to " + promotionPiece + ")" : "");
    }
}
