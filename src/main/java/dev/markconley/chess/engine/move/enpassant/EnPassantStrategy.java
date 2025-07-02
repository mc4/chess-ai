package dev.markconley.chess.engine.move.enpassant;

import dev.markconley.chess.engine.core.Position;
import dev.markconley.chess.engine.move.Move;
import dev.markconley.chess.engine.pieces.Piece;
import dev.markconley.chess.engine.state.BoardState;

public interface EnPassantStrategy {
	
	boolean isEnPassant(Position from, Position to, BoardState state);
	Move createEnPassantMove(Position from, Position to, Piece pawn, BoardState state);
	
}
