package dev.markconley.chess.engine.board;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import dev.markconley.chess.engine.core.Color;
import dev.markconley.chess.engine.core.Position;
import dev.markconley.chess.engine.move.Move;
import dev.markconley.chess.engine.pieces.King;
import dev.markconley.chess.engine.pieces.Pawn;
import dev.markconley.chess.engine.pieces.Piece;
import dev.markconley.chess.engine.pieces.Queen;

class BoardTest {

	private Board board;

	@BeforeEach
	void setUp() {
		board = Board.emptyBoard();
	}

	// Basic Setup & State

	@Test
	void testInitialBoardSetup() {
		board = new Board();
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
	void testSimplePawnMove() {
	    Board board = Board.emptyBoard()
	    		.place("e2", new Pawn(Color.WHITE));

	    boolean result = board.makeMove(new Position("e2"), new Position("e4"));

	    assertTrue(result);
	    assertNull(board.getPieceAt(new Position("e2")));
	    assertNotNull(board.getPieceAt(new Position("e4")));
	}

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
	    board.setPieceAt(Position.of("g1"), new King(Color.WHITE));

	    Piece blackPawn = new Pawn(Color.BLACK);
	    blackPawn.setPosition(to);
	    board.setPieceAt(to, blackPawn);
	    board.setPieceAt(Position.of("g8"), new King(Color.BLACK));

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
	    Position from = new Position("e7"); 
	    Position to = new Position("e5"); 
	    
	    Piece blackPawn = new Pawn(Color.BLACK);
	    blackPawn.setPosition(from);
	    board.setPieceAt(from, blackPawn);
	    
	    boolean result = board.makeMove(from, to);
	    
	    assertFalse(result, "Black moving on white's turn should fail");
	    assertEquals(board.getPieceAt(from), blackPawn, "Black pawn should be on e7");
	    assertNull(board.getPieceAt(to), "Destination square should be empty after move failed");
	    assertFalse(board.getCurrentTurn() == Color.BLACK, "Black should not move first");
	    assertTrue(board.getMoveHistory().isEmpty(), "No move should be recorded for an illegal move");
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

	@Test
	void testMoveIsRecordedInHistory() {
	    Board board = Board.emptyBoard()
	                       .place("e2", new Pawn(Color.WHITE))
	                       .place("e1", new King(Color.WHITE))
	                       .place("e8", new King(Color.BLACK));

	    Position from = new Position("e2");
	    Position to = new Position("e4");

	    boolean moveResult = board.makeMove(from, to);

	    assertTrue(moveResult, "Move should succeed");
	    
	    List<Move> history = board.getMoveHistory();
	    assertEquals(1, history.size(), "Move history should contain exactly one move");
	    
	    Move move = history.get(0);
	    assertEquals(from, move.from(), "Move 'from' position should be correct");
	    assertEquals(to, move.to(), "Move 'to' position should be correct");
	    assertEquals(Color.WHITE, move.movedPiece().getColor(), "Moved piece should be white");
	    assertNull(move.capturedPiece(), "No piece should be captured in this move");
	}


	@Disabled("Not implemented yet")
	@Test
	void testUndoMove() {
	}
}
