package dev.markconley.chess.engine.move;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import dev.markconley.chess.engine.core.Color;
import dev.markconley.chess.engine.core.Position;
import dev.markconley.chess.engine.pieces.King;
import dev.markconley.chess.engine.pieces.Pawn;
import dev.markconley.chess.engine.pieces.Piece;
import dev.markconley.chess.engine.pieces.Queen;

public class MoveFactoryTest {

	private final Piece whitePawn = new Pawn(Color.WHITE);
	private final Piece blackPawn = new Pawn(Color.BLACK);
	private final Piece whiteQueen = new Queen(Color.WHITE);

	@Test
	void testNormalMove() {
		Position from = Position.of("e2");
		Position to = Position.of("e4");

		Move move = MoveFactory.normal(from, to, whitePawn);

		assertEquals(from, move.from());
		assertEquals(to, move.to());
		assertEquals(whitePawn, move.movedPiece());
		assertNull(move.capturedPiece());
		assertFalse(move.isCastling());
		assertFalse(move.isEnPassant());
		assertFalse(move.isPromotion());
		assertNull(move.promotionPiece());
	}

	@Test
	void testCaptureMove() {
		Position from = Position.of("d4");
		Position to = Position.of("e5");

		Move move = MoveFactory.capture(from, to, whitePawn, blackPawn);

		assertEquals(from, move.from());
		assertEquals(to, move.to());
		assertEquals(whitePawn, move.movedPiece());
		assertEquals(blackPawn, move.capturedPiece());
		assertFalse(move.isCastling());
		assertFalse(move.isEnPassant());
		assertFalse(move.isPromotion());
		assertNull(move.promotionPiece());
	}

	@Test
	void testPromotionMove() {
		Position from = Position.of("e7");
		Position to = Position.of("e8");

		Move move = MoveFactory.promotion(from, to, whitePawn, whiteQueen);

		assertEquals(from, move.from());
		assertEquals(to, move.to());
		assertEquals(whitePawn, move.movedPiece());
		assertNull(move.capturedPiece());
		assertFalse(move.isCastling());
		assertFalse(move.isEnPassant());
		assertTrue(move.isPromotion());
		assertEquals(whiteQueen, move.promotionPiece());
	}

	@Test
	void testCastleMove() {
		Position from = Position.of("e1");
		Position to = Position.of("g1");

		Piece whiteKing = new King(Color.WHITE);

		Move move = MoveFactory.castle(from, to, whiteKing);

		assertEquals(from, move.from());
		assertEquals(to, move.to());
		assertTrue(move.isCastling());
		assertFalse(move.isEnPassant());
		assertFalse(move.isPromotion());
	}

	@Test
	void testEnPassantMove() {
		Position from = Position.of("e5");
		Position to = Position.of("d6");

		Move move = MoveFactory.enPassant(from, to, whitePawn, blackPawn);

		assertEquals(from, move.from());
		assertEquals(to, move.to());
		assertEquals(whitePawn, move.movedPiece());
		assertEquals(blackPawn, move.capturedPiece());
		assertFalse(move.isCastling());
		assertTrue(move.isEnPassant());
		assertFalse(move.isPromotion());
		assertNull(move.promotionPiece());
	}

}
