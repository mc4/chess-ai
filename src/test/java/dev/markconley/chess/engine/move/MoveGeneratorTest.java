package dev.markconley.chess.engine.move;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.markconley.chess.engine.board.Board;
import dev.markconley.chess.engine.core.Color;
import dev.markconley.chess.engine.core.Position;
import dev.markconley.chess.engine.pieces.Bishop;
import dev.markconley.chess.engine.pieces.Direction;
import dev.markconley.chess.engine.pieces.King;
import dev.markconley.chess.engine.pieces.Knight;
import dev.markconley.chess.engine.pieces.Pawn;
import dev.markconley.chess.engine.pieces.Queen;
import dev.markconley.chess.engine.pieces.Rook;

public class MoveGeneratorTest {
	
	  private Board board;

	    @BeforeEach
	    void setup() {
	        board = Board.emptyBoard();  
	    }

	    @Test
	    void testGenerateSlidingMoves_rookMovesInOpenBoard() {
	        Rook rook = new Rook(Color.WHITE);
	        Position pos = Position.of("d4");
	        rook.setPosition(pos);
	        board.setPieceAt(pos, rook);

	        List<Move> moves = MoveGenerator.generateSlidingMoves(board, rook, Direction.ROOK_DIRECTIONS);

	        // Rook on empty d4 can move in 4 directions until edges (max 14 moves)
	        assertEquals(14, moves.size());

	        // All moves must start at d4
	        assertTrue(moves.stream().allMatch(m -> m.from().equals(pos)));
	    }

	    @Test
	    void testGenerateSlidingMoves_rookBlockedBySameColor() {
	        Rook rook = new Rook(Color.WHITE);
	        Position rookPos = Position.of("d4");
	        rook.setPosition(rookPos);
	        board.setPieceAt(rookPos, rook);

	        // Place a friendly piece blocking rook on d6
	        Pawn friendlyPawn = new Pawn(Color.WHITE);
	        Position blocker = Position.of("d6");
	        friendlyPawn.setPosition(blocker);
	        board.setPieceAt(blocker, friendlyPawn);

	        List<Move> moves = MoveGenerator.generateSlidingMoves(board, rook, Direction.ROOK_DIRECTIONS);

	        // Rook cannot move beyond d5 (because d6 blocked by friendly pawn)
	        assertTrue(moves.stream().noneMatch(m -> m.to().equals(Position.of("d6"))));
	        assertTrue(moves.stream().anyMatch(m -> m.to().equals(Position.of("d5"))));
	    }

	    @Test
	    void testGenerateSlidingMoves_rookCanCaptureEnemy() {
	        Rook rook = new Rook(Color.WHITE);
	        Position rookPos = Position.of("d4");
	        rook.setPosition(rookPos);
	        board.setPieceAt(rookPos, rook);

	        // Enemy piece on d6
	        Pawn enemyPawn = new Pawn(Color.BLACK);
	        Position enemyPos = Position.of("d6");
	        enemyPawn.setPosition(enemyPos);
	        board.setPieceAt(enemyPos, enemyPawn);

	        List<Move> moves = MoveGenerator.generateSlidingMoves(board, rook, Direction.ROOK_DIRECTIONS);

	        // Rook should have a capture move to d6
	        boolean hasCapture = moves.stream().anyMatch(m -> m.to().equals(enemyPos) && m.capturedPiece() == enemyPawn);
	        assertTrue(hasCapture);

	        // No moves beyond d6
	        assertTrue(moves.stream().noneMatch(m -> m.to().equals(Position.of("d7"))));
	    }

	    @Test
	    void testGenerateJumpingMoves_knightBasic() {
	        Knight knight = new Knight(Color.WHITE);
	        Position pos = Position.of("d4");
	        knight.setPosition(pos);
	        board.setPieceAt(pos, knight);

	        List<Move> moves = MoveGenerator.generateJumpingMoves(board, knight);

	        // Knight has up to 8 moves, some may be off board from d4 but mostly 8 valid
	        assertTrue(moves.size() > 0);
	        assertTrue(moves.size() <= 8);

	        // All moves start from d4
	        assertTrue(moves.stream().allMatch(m -> m.from().equals(pos)));
	    }

	    @Test
	    void testGeneratePawnMoves_basicMoveAndDoubleStep() {
	        Pawn whitePawn = new Pawn(Color.WHITE);
	        Position startPos = Position.of("e2");
	        whitePawn.setPosition(startPos);
	        board.setPieceAt(startPos, whitePawn);

	        List<Move> moves = MoveGenerator.generatePawnMoves(board, whitePawn);

	        // Should contain move to e3 and e4 (double step)
	        List<Position> destinations = moves.stream().map(Move::to).collect(Collectors.toList());
	        assertTrue(destinations.contains(Position.of("e3")));
	        assertTrue(destinations.contains(Position.of("e4")));
	    }

	    @Test
	    void testGeneratePawnMoves_blockedForward_noMoves() {
	        Pawn whitePawn = new Pawn(Color.WHITE);
	        Position startPos = Position.of("e2");
	        whitePawn.setPosition(startPos);
	        board.setPieceAt(startPos, whitePawn);

	        // Block the pawn directly in front on e3
	        Pawn blockingPawn = new Pawn(Color.WHITE);
	        Position blockPos = Position.of("e3");
	        blockingPawn.setPosition(blockPos);
	        board.setPieceAt(blockPos, blockingPawn);

	        List<Move> moves = MoveGenerator.generatePawnMoves(board, whitePawn);

	        // Pawn cannot move forward or double step
	        assertTrue(moves.stream().noneMatch(m -> m.to().equals(Position.of("e3"))));
	        assertTrue(moves.stream().noneMatch(m -> m.to().equals(Position.of("e4"))));
	    }

	    @Test
	    void testGeneratePawnMoves_captureMoves() {
	        Pawn whitePawn = new Pawn(Color.WHITE);
	        Position startPos = Position.of("d4");
	        whitePawn.setPosition(startPos);
	        board.setPieceAt(startPos, whitePawn);

	        // Enemy pawns diagonally at c5 and e5
	        Pawn blackPawn1 = new Pawn(Color.BLACK);
	        Position enemyPos1 = Position.of("c5");
	        blackPawn1.setPosition(enemyPos1);
	        board.setPieceAt(enemyPos1, blackPawn1);

	        Pawn blackPawn2 = new Pawn(Color.BLACK);
	        Position enemyPos2 = Position.of("e5");
	        blackPawn2.setPosition(enemyPos2);
	        board.setPieceAt(enemyPos2, blackPawn2);

	        List<Move> moves = MoveGenerator.generatePawnMoves(board, whitePawn);

	        assertTrue(moves.stream().anyMatch(m -> m.to().equals(enemyPos1) && m.capturedPiece() == blackPawn1));
	        assertTrue(moves.stream().anyMatch(m -> m.to().equals(enemyPos2) && m.capturedPiece() == blackPawn2));
	    }

	    @Test
	    void testGeneratePawnMoves_promotionMoves() {
	        Pawn whitePawn = new Pawn(Color.WHITE);
	        Position startPos = Position.of("g7");
	        whitePawn.setPosition(startPos);
	        board.setPieceAt(startPos, whitePawn);

	        // The square in front is empty to promote
	        List<Move> moves = MoveGenerator.generatePawnMoves(board, whitePawn);

	        // Should have 4 promotion moves for each piece type
	        List<Move> promotionMoves = moves.stream().filter(Move::isPromotion).toList();
	        assertEquals(4, promotionMoves.size());

	        // Check promotion pieces types
	        Set<Class<?>> promotedTypes = promotionMoves.stream()
	        	    .map(Move::promotionPiece)
	        	    .map(p -> (Class<?>) p.getClass()) 
	        	    .collect(Collectors.toSet());   

	        assertTrue(promotedTypes.containsAll(Set.of(
	            Queen.class, Rook.class, Bishop.class, Knight.class
	        )));
	    }

	    @Test
	    void testGenerateKingMoves_basic() {
	        King king = new King(Color.WHITE);
	        Position pos = Position.of("e1");
	        king.setPosition(pos);
	        board.setPieceAt(pos, king);

	        List<Move> moves = MoveGenerator.generateKingMoves(board, king, board.getSpecialMoveService());

	        // King can move one square in any direction (max 8 moves)
	        assertTrue(moves.size() > 0);
	        assertTrue(moves.size() <= 8);

	        // All moves start from e1
	        assertTrue(moves.stream().allMatch(m -> m.from().equals(pos)));
	    }

	    @Test
	    void testGenerateMoves_dispatchesCorrectly() {
	        // Put a white queen on d4 and check generateMoves calls combined sliding moves
	        Queen queen = new Queen(Color.WHITE);
	        Position pos = Position.of("d4");
	        queen.setPosition(pos);
	        board.setPieceAt(pos, queen);

	        List<Move> moves = MoveGenerator.generateMoves(board, queen, board.getSpecialMoveService());

	        // Queen moves = rook moves + bishop moves => should be more than zero
	        assertTrue(moves.size() > 0);

	        // All moves start from d4
	        assertTrue(moves.stream().allMatch(m -> m.from().equals(pos)));
	    }

}
