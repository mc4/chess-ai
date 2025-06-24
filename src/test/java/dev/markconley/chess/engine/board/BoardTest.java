package dev.markconley.chess.engine.board;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import dev.markconley.chess.engine.core.Color;
import dev.markconley.chess.engine.core.Position;
import dev.markconley.chess.engine.move.Move;
import dev.markconley.chess.engine.move.MoveFactory;
import dev.markconley.chess.engine.move.MoveGenerator;
import dev.markconley.chess.engine.pieces.Bishop;
import dev.markconley.chess.engine.pieces.King;
import dev.markconley.chess.engine.pieces.Knight;
import dev.markconley.chess.engine.pieces.Pawn;
import dev.markconley.chess.engine.pieces.Piece;
import dev.markconley.chess.engine.pieces.Queen;
import dev.markconley.chess.engine.pieces.Rook;

class BoardTest {

	private Board board;

	@BeforeEach
	void setUp() {
		board = Board.emptyBoard()	                
				.place("e1", new King(Color.WHITE))
                .place("e8", new King(Color.BLACK));
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
	    board.place("e2", new Pawn(Color.WHITE));
	    
	    boolean result = board.makeMove(new Position("e2"), new Position("e4"));

	    assertTrue(result);
	    assertNull(board.getPieceAt(new Position("e2")));
	    assertNotNull(board.getPieceAt(new Position("e4")));
	}

	@Test
	void testMakeNormalMove() {
		board.place("e1", new King(Color.WHITE))
			 .place("e8", new King(Color.BLACK));
		
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
	    Position from = Position.of("d4"); 
	    Position to = Position.of("d6");  

	    Piece whiteQueen = new Queen(Color.WHITE);
	    whiteQueen.setPosition(from);
	    board.setPieceAt(from, whiteQueen);
	    King whiteKing = new King(Color.WHITE);
	    whiteKing.setPosition(Position.of("g1"));
	    board.setPieceAt(Position.of("g1"), whiteKing);

	    Piece blackPawn = new Pawn(Color.BLACK);
	    blackPawn.setPosition(to);
	    board.setPieceAt(to, blackPawn);
	    King blackKing = new King(Color.BLACK);
	    blackKing.setPosition(Position.of("g8"));
	    board.setPieceAt(Position.of("g8"), blackKing);

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
	    Position from = Position.of("e2");
	    Position to = Position.of("e4");
	    
	    Piece whitePawn = new Pawn(Color.WHITE);
	    whitePawn.setPosition(from);
	    board.setPieceAt(from, whitePawn);

	    // Initially, it should be White's turn
	    assertEquals(Color.WHITE, board.getCurrentTurn());

	    // Make the move
	    Move move = MoveFactory.normal(from, to, whitePawn);
	    board.makeMove(move.from(), move.to());

	    // After the move, it should be Black's turn
	    assertEquals(Color.BLACK, board.getCurrentTurn());
	}
	
	// Special Moves

	@Test
	void testCastlingMoveWhiteCastleKingSide() {
	    board.clearBoard();
	    Piece blackKing = new King(Color.BLACK);
	    blackKing.setPosition(Position.of("e8"));
	    board.setPieceAt(Position.of("e8"), blackKing);

	    Position kingStart = Position.of("e1");
	    Position rookStart = Position.of("h1");
	    Position kingEnd = Position.of("g1");
	    Position rookEnd = Position.of("f1");

	    King whiteKing = new King(Color.WHITE);
	    Rook whiteRook = new Rook(Color.WHITE);
	    whiteKing.setPosition(kingStart);
	    whiteRook.setPosition(rookStart);

	    board.setPieceAt(kingStart, whiteKing);
	    board.setPieceAt(rookStart, whiteRook);

	    Move castlingMove = MoveFactory.castle(kingStart, kingEnd, whiteKing);
	    boolean moveSuccessful = board.makeMove(castlingMove.from(), castlingMove.to());

	    assertTrue(moveSuccessful, "Castling was not successful");
	    assertTrue(board.getPieceAt(kingEnd) instanceof King);
	    assertTrue(board.getPieceAt(rookEnd) instanceof Rook);
	    assertNull(board.getPieceAt(kingStart));
	    assertNull(board.getPieceAt(rookStart));
	    assertEquals(Color.BLACK, board.getCurrentTurn());
	}


	@Disabled("Not implemented yet")
	@Test
	void testEnPassantMove() {
	}
	
	@Test
	void testEnPassantNotAllowedAfterDelay() {
		Piece whitePawn = new Pawn(Color.WHITE);
		Piece blackPawn = new Pawn(Color.BLACK);
		
		Position e5 = Position.of("e5");
		Position d7 = Position.of("d7");

		board.setPieceAt(e5, whitePawn);
		board.setPieceAt(d7, blackPawn);
		
		whitePawn.setPosition(e5);
		blackPawn.setPosition(d7);
		board.setCurrentTurn(Color.BLACK);

		// Black plays d7 - d5
		assertTrue(board.makeMove(Position.of("d7"), Position.of("d5")));

		// White plays a "waiting" move (we'll fake a turn swap)
		board.setCurrentTurn(Color.WHITE);
		board.switchTurn(); // simulate any move

		// Now try en passant — should fail
		board.setCurrentTurn(Color.WHITE);
		assertFalse(board.makeMove(Position.of("e5"), Position.of("d6")), "En passant must be done immediately");
	}

	@Test
	void testEnPassantTargetSquareSetCorrectly() {
		Piece blackPawn = new Pawn(Color.BLACK);
		board.setPieceAt(Position.of("e7"), blackPawn);
		blackPawn.setPosition(Position.of("e7"));
		board.setCurrentTurn(Color.BLACK);

		assertTrue(board.makeMove(Position.of("e7"), Position.of("e5")));

		Position expectedTarget = Position.of("e6");
		assertEquals(expectedTarget, board.getEnPassantTarget(), "En passant square should be set to e6");
	}

	@Test
	void testEnPassantFailsWhenNoTarget() {
		Piece whitePawn = new Pawn(Color.WHITE);
		Piece blackPawn = new Pawn(Color.BLACK);

		// White pawn on e5
		Position e5 = Position.of("e5");
		whitePawn.setPosition(e5);
		board.setPieceAt(e5, whitePawn);

		// Black pawn pushed to d5 one or more moves ago (simulate no en passant target)
		Position d5 = Position.of("d5");
		blackPawn.setPosition(d5);
		board.setPieceAt(d5, blackPawn);

		board.setCurrentTurn(Color.WHITE);

		// Attempt en passant: e5 - d6 (diagonal empty square with no target)
		Position d6 = Position.of("d6");
		assertFalse(board.makeMove(e5, d6), "Should not allow en passant when not set up");
	}
	
	@Test
	void testPawnPromotion() {
	    Board board = Board.emptyBoard(); 

	    Piece whitePawn = new Pawn(Color.WHITE);
	    Position from = Position.of("e7");
	    whitePawn.setPosition(from);
	    board.setPieceAt(from, whitePawn);

	    List<Move> moves = MoveGenerator.generatePawnMoves(board, whitePawn);

	    List<Move> promotionMoves = moves.stream()
	            .filter(Move::isPromotion)
	            .toList();

	    assertEquals(4, promotionMoves.size());

	    Set<Class<?>> promotionClasses = promotionMoves.stream()
	            .map(Move::promotionPiece)
	            .filter(Objects::nonNull)
	            .map(p -> (Class<?>) p.getClass())
	            .collect(Collectors.toSet());

	    assertTrue(promotionClasses.containsAll(List.of(
	        Queen.class, Rook.class, Bishop.class, Knight.class
	    )));
	}
	
	@Test
	void testPawnPromotionToQueen() {
	    Board board = new Board();
	    Piece whitePawn = new Pawn(Color.WHITE);
	    Position from = Position.of("e7");
	    whitePawn.setPosition(from);
	    board.setPieceAt(from, whitePawn);

	    List<Move> moves = MoveGenerator.generatePawnMoves(board, whitePawn);

	    boolean hasQueenPromotion = moves.stream()
	            .filter(Move::isPromotion)
	            .anyMatch(move -> move.promotionPiece() instanceof Queen);

	    assertTrue(hasQueenPromotion);
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
