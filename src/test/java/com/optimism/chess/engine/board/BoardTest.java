package com.optimism.chess.engine.board;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.optimism.chess.engine.core.Color;
import com.optimism.chess.engine.core.Position;
import com.optimism.chess.engine.pieces.King;
import com.optimism.chess.engine.pieces.Piece;

class BoardTest {

	private Board board;

	@BeforeEach
	void setUp() {
		board = new Board();
	}

	// Basic Setup & State

	@Test
	void testInitialBoardSetup() {
		int whitePieces = 0;
		int blackPieces = 0;
		int whiteKings = 0;
		int blackKings = 0;

		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				Position pos = new Position(row, col);
				Piece piece = board.getPieceAt(pos);

				if (piece != null) {
					if (piece.getColor() == Color.WHITE) {
						whitePieces++;
						if (piece instanceof King) {
							whiteKings++;
						}
					} else if (piece.getColor() == Color.BLACK) {
						blackPieces++;
						if (piece instanceof King) {
							blackKings++;
						}
					}
				}
			}
		}

		assertEquals(16, whitePieces, "White should have 16 pieces at start");
		assertEquals(16, blackPieces, "Black should have 16 pieces at start");
		assertEquals(1, whiteKings, "White should have 1 king");
		assertEquals(1, blackKings, "Black should have 1 king");
	}

	@Test
	void testGetAndSetPieceAt() {
	}

	// Move Execution

	@Test
	void testMakeNormalMove() {
	}

	@Test
	void testMakeCaptureMove() {
	}

	@Test
	void testMakeIllegalMoveWrongTurn() {
	}

	@Test
	void testMoveUpdatesTurn() {
	}

	// Special Moves

	@Test
	void testCastlingMove() {
	}

	@Disabled("Not implemented yet")
	@Test
	void testEnPassantMove() {
	}

	@Disabled("Not implemented yet")
	@Test
	void testPawnPromotion() {
	}

	// Move History

	@Disabled("Not implemented yet")
	@Test
	void testMoveIsRecordedInHistory() {
	}

	@Disabled("Not implemented yet")
	@Test
	void testUndoMove() {
	}
}
