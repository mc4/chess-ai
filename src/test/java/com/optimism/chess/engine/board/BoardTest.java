package com.optimism.chess.engine.board;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.optimism.chess.engine.core.Color;
import com.optimism.chess.engine.core.Position;
import com.optimism.chess.engine.pieces.King;
import com.optimism.chess.engine.pieces.Pawn;
import com.optimism.chess.engine.pieces.Piece;
import com.optimism.chess.engine.pieces.Queen;

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
	// Move Execution

	@Test
	void testMakeNormalMove() {
		
	    // Place a white queen at d4 (3, 3) 
	    Position from = new Position(3, 3); // d4
	    Position to = new Position(5, 3);   // d6
	    
	    Piece whiteQueen = new Queen(Color.WHITE);
	    whiteQueen.setPosition(from);
	    board.setPieceAt(from, whiteQueen);
	    
	    // Make the move
	    boolean result = board.makeMove(from, to);
	    
	    assertTrue(result, "Move should be successful");
	    assertNull(board.getPieceAt(from), "Original square should be empty after move");
	    Piece movedPiece = board.getPieceAt(to);
	    assertNotNull(movedPiece, "The destination square should now have the queen");
	    assertEquals(whiteQueen, movedPiece, "Queen should occupy the captured square");
	    assertTrue(movedPiece instanceof Queen, "Moved piece should still be a queen");
	    assertEquals(Color.WHITE, movedPiece.getColor(), "Moved piece should be white");
		
	}

	@Test
	void testMakeCaptureMove() {
	    // Place a white queen at d4 (3, 3) and a black pawn at d6 (5, 3)
	    Position from = new Position(3, 3); // d4
	    Position to = new Position(5, 3);   // d6

	    Piece whiteQueen = new Queen(Color.WHITE);
	    whiteQueen.setPosition(from);
	    board.setPieceAt(from, whiteQueen);

	    Piece blackPawn = new Pawn(Color.BLACK);
	    blackPawn.setPosition(to);
	    board.setPieceAt(to, blackPawn);

	    // Make the move
	    boolean result = board.makeMove(from, to);

	    assertTrue(result, "Capture move should be successful");
	    assertNull(board.getPieceAt(from), "Original square should be empty after move");
	    Piece movedPiece = board.getPieceAt(to);
	    assertNotNull(movedPiece, "Captured square should now have the queen");
	    assertEquals(whiteQueen, movedPiece, "Queen should occupy the captured square");
	    assertTrue(movedPiece instanceof Queen, "Moved piece should still be a queen");
	    assertEquals(Color.WHITE, movedPiece.getColor(), "Moved piece should be white");
	}

	@Test
	void testMakeIllegalMoveWrongTurn() {
	}

	@Test
	void testMoveUpdatesTurn() {
		
	}

	// Special Moves

	@Disabled("Not implemented yet")
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
	
	@Test
	void testPawnPromotionToQueen() {
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
