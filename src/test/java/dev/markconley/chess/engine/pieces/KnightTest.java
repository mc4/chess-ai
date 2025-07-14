package dev.markconley.chess.engine.pieces;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import dev.markconley.chess.engine.board.Board;
import dev.markconley.chess.engine.core.Color;
import dev.markconley.chess.engine.core.Position;
import dev.markconley.chess.engine.move.Move;

@Disabled("This class is temporarily disabled")
class KnightTest {

	private Board board;

	@BeforeEach
	void setUp() {
		board = Board.emptyBoard(); 
	}

	@Test
	void testAllKnightMovesFromCenter() {
//		Knight knight = new Knight(Color.WHITE);
//		Position center = Position.of("e4");
//		knight.setPosition(center);
//		board.setPieceAt(center, knight);
//
//		List<Move> moves = knight.getPossibleMoves(board);
//		Set<Position> destinations = moves.stream()
//				.map(Move::to)
//				.collect(Collectors.toSet());
//
//		Set<Position> expected = Set.of(Position.of("d6"), Position.of("f6"), 
//				Position.of("c5"), Position.of("g5"),
//				Position.of("c3"), Position.of("g3"),
//				Position.of("d2"), Position.of("f2"));
//
//		assertEquals(expected, destinations);
	}

	@Test
	void testKnightMovesFromCorner() {
//		Knight knight = new Knight(Color.WHITE);
//		Position corner = Position.of("a1");
//		knight.setPosition(corner);
//		board.setPieceAt(corner, knight);
//
//		List<Move> moves = knight.getPossibleMoves(board);
//		Set<Position> destinations = moves.stream()
//				.map(Move::to)
//				.collect(Collectors.toSet());
//
//		Set<Position> expected = Set.of(Position.of("b3"), Position.of("c2"));
//
//		assertEquals(expected, destinations);
	}

	@Test
	void testKnightCanCaptureEnemy() {
//		Knight knight = new Knight(Color.WHITE);
//		Position from = Position.of("e4");
//		knight.setPosition(from);
//		board.setPieceAt(from, knight);
//
//		Position target = Position.of("g5");
//		Pawn enemy = new Pawn(Color.BLACK);
//		enemy.setPosition(target);
//		board.setPieceAt(target, enemy);
//
//		List<Move> moves = knight.getPossibleMoves(board);
//
//		boolean foundCapture = moves.stream().anyMatch(m -> m.to().equals(target) && m.capturedPiece() != null);
//
//		assertTrue(foundCapture, "Expected knight to be able to capture enemy on g5");
	}

	@Test
	void testKnightCannotCaptureFriendly() {
//		Knight knight = new Knight(Color.WHITE);
//		Position from = Position.of("e4");
//		knight.setPosition(from);
//		board.setPieceAt(from, knight);
//
//		Position target = Position.of("f6");
//		Pawn friendly = new Pawn(Color.WHITE);
//		friendly.setPosition(target);
//		board.setPieceAt(target, friendly);
//
//		List<Move> moves = knight.getPossibleMoves(board);
//		boolean containsFriendly = moves.stream()
//				.anyMatch(m -> m.to().equals(target));
//
//		assertFalse(containsFriendly);
	}

	@Test
	void testKnightJumpsOverSurroundingPieces() {
//		Knight knight = new Knight(Color.WHITE);
//		Position from = Position.of("e4");
//		knight.setPosition(from);
//		board.setPieceAt(from, knight);
//
//		// Fill surrounding squares (not knight destinations)
//		for (int row = 3; row <= 5; row++) {
//			for (int col = 3; col <= 5; col++) {
//				Position pos = new Position(row, col);
//				if (!pos.equals(from)) {
//					board.setPieceAt(pos, new Pawn(Color.WHITE));
//				}
//			}
//		}
//		
//		List<Move> moves = knight.getPossibleMoves(board);
//		assertEquals(6, moves.size()); 
	}

	@Test
	void testKnightBlockedByAllFriendlyPieces() {
//		Knight knight = new Knight(Color.WHITE);
//		Position center = Position.of("e4");
//		knight.setPosition(center);
//		board.setPieceAt(center, knight);
//
//		for (Position pos : List.of(Position.of("d6"), Position.of("f6"), Position.of("c5"), Position.of("g5"),
//				Position.of("c3"), Position.of("g3"), Position.of("d2"), Position.of("f2"))) {
//			Pawn pawn = new Pawn(Color.WHITE);
//			pawn.setPosition(pos);
//			board.setPieceAt(pos, pawn);
//		}
//
//		List<Move> moves = knight.getPossibleMoves(board);
//		assertTrue(moves.isEmpty());
	}

	@Test
	void testKnightWithMixedTargets() {
//		Knight knight = new Knight(Color.WHITE);
//		Position from = Position.of("e4");
//		knight.setPosition(from);
//		board.setPieceAt(from, knight);
//
//		board.setPieceAt(Position.of("d6"), new Pawn(Color.BLACK));
//		board.setPieceAt(Position.of("f6"), new Pawn(Color.WHITE));
//		board.setPieceAt(Position.of("c5"), new Pawn(Color.BLACK));
//		board.setPieceAt(Position.of("g5"), new Pawn(Color.WHITE));
//
//		List<Move> moves = knight.getPossibleMoves(board);
//		Set<Position> destinations = moves.stream().map(Move::to).collect(Collectors.toSet());
//
//		assertTrue(destinations.contains(Position.of("d6"))); // enemy
//		assertFalse(destinations.contains(Position.of("f6"))); // friendly
//		assertTrue(destinations.contains(Position.of("c5"))); // enemy
//		assertFalse(destinations.contains(Position.of("g5"))); // friendly
//
//		// Remaining legal targets
//		assertTrue(destinations
//				.containsAll(Set.of(Position.of("c3"), Position.of("g3"), Position.of("d2"), Position.of("f2"))));
	}
}
