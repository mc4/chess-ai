package dev.markconley.chess.engine.move.enpassant;

import dev.markconley.chess.engine.board.Board;
import dev.markconley.chess.engine.core.Position;
import dev.markconley.chess.engine.pieces.Piece;
import dev.markconley.chess.engine.move.Move;

public interface EnPassantStrategy {
	
	boolean isEnPassant(Position from, Position to, Board board);
	Move createEnPassantMove(Position from, Position to, Piece pawn, Board board);
	
}
