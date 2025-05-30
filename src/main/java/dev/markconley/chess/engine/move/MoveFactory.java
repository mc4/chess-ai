package dev.markconley.chess.engine.move;

import dev.markconley.chess.engine.core.Position;
import dev.markconley.chess.engine.pieces.Piece;

public class MoveFactory {
	
	private MoveFactory() {} // utitily class

    public static Move normal(Position from, Position to, Piece moved) {
        return new Move(from, to, moved, null, false, false, null);
    }

    public static Move capture(Position from, Position to, Piece moved, Piece captured) {
        return new Move(from, to, moved, captured, false, false, null);
    }

    public static Move promotion(Position from, Position to, Piece moved, Piece promotionPiece) {
        return new Move(from, to, moved, null, false, false, promotionPiece);
    }

    public static Move castle(Position from, Position to, Piece king) {
        return new Move(from, to, king, null, true, false, null);
    }

    public static Move enPassant(Position from, Position to, Piece moved, Piece captured) {
        return new Move(from, to, moved, captured, false, true, null);
    }
    
}

