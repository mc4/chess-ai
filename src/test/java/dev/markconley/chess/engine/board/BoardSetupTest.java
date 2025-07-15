package dev.markconley.chess.engine.board;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.markconley.chess.engine.core.Color;
import dev.markconley.chess.engine.core.Position;
import dev.markconley.chess.engine.pieces.Piece;
import dev.markconley.chess.engine.pieces.Queen;

class BoardSetupTest {
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
	
	@Test
	void testGetAndSetPieceAt() {
	    Position position = new Position(3, 3);
	    Piece queen = new Queen(Color.WHITE);
	    queen.setPosition(position);

	    board.setPieceAt(position, queen);
	    Piece retrieved = board.getPieceAt(position);

	    assertNotNull(retrieved, "Piece should not be null after setting");
	    assertEquals(queen, retrieved, "Retrieved piece should match the one that was set");
	    assertEquals(Color.WHITE, retrieved.getColor(), "Piece color should be white");
	    assertTrue(retrieved instanceof Queen, "Piece should be instance of Queen");
	}
	
}
