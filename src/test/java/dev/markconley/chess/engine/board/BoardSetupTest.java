package dev.markconley.chess.engine.board;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.markconley.chess.engine.core.Color;
import dev.markconley.chess.engine.core.Position;
import dev.markconley.chess.engine.pieces.Piece;
import dev.markconley.chess.engine.pieces.Queen;

public class BoardSetupTest {
	private Board board;

	@BeforeEach
	void init() {
		board = new Board();
	}

	@Test
	void testShouldInitializeWith32Pieces() {
		int whiteCount = 0, blackCount = 0;
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				Piece p = board.getPieceAt(new Position(row, col));
				if (p != null) {
					if (p.getColor() == Color.WHITE) {
						whiteCount++;
					} else {
						blackCount++;
					}
				}
			}
		}
		assertEquals(16, whiteCount);
		assertEquals(16, blackCount);
	}

	@Test
	void testShouldPlacePieceCorrectly() {
		Piece queen = new Queen(Color.WHITE);
		Position pos = Position.of("d4");
		board.setPieceAt(pos, queen);
		assertEquals(queen, board.getPieceAt(pos));
	}

	@Test
	void testShouldReturnNullForEmptySquare() {
		assertNull(board.getPieceAt(Position.of("c3")));
	}
}
