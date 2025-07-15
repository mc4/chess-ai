package dev.markconley.chess.engine.pieces;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.markconley.chess.engine.board.Board;
import dev.markconley.chess.engine.core.Color;
import dev.markconley.chess.engine.core.Position;
import dev.markconley.chess.engine.move.Move;
import dev.markconley.chess.engine.state.BoardState;

class KnightTest {

    private Board board;
    private BoardState state;

	@BeforeEach
	void setup() {
		board = Board.emptyBoard();
		state = new BoardState(board);
	}

	private List<Move> generateMovesForKnightAt(String square, Color color) {
		Knight knight = new Knight(color);
		Position pos = Position.of(square);
		board.place(square, knight);
		knight.setPosition(pos);
		return knight.getPossibleMoves(state);
	}

    @Test
    void testAllKnightMovesFromCenter() {
        List<Move> moves = generateMovesForKnightAt("e4", Color.WHITE);
        Set<Position> destinations = moves.stream().map(Move::to).collect(Collectors.toSet());

        Set<Position> expected = Set.of(
            Position.of("d6"), Position.of("f6"),
            Position.of("c5"), Position.of("g5"),
            Position.of("c3"), Position.of("g3"),
            Position.of("d2"), Position.of("f2")
        );

        assertEquals(expected, destinations);
    }

	@Test
	void testKnightMovesFromCorner() {
		List<Move> moves = generateMovesForKnightAt("a1", Color.WHITE);
		Set<Position> destinations = moves.stream().map(Move::to).collect(Collectors.toSet());

		Set<Position> expected = Set.of(Position.of("b3"), Position.of("c2"));
		assertEquals(expected, destinations);
	}

	@Test
	void testKnightCapturesEnemy() {
		Knight knight = new Knight(Color.WHITE);
		board.place("e4", knight);

		Pawn enemy = new Pawn(Color.BLACK);
		board.place("g5", enemy);

		List<Move> moves = knight.getPossibleMoves(state);
		boolean foundCapture = moves.stream()
				.anyMatch(m -> m.to().equals(Position.of("g5")) && m.capturedPiece() != null);

		assertTrue(foundCapture, "Expected knight to be able to capture enemy on g5");
	}

	@Test
	void testKnightCannotCaptureFriendly() {
		Knight knight = new Knight(Color.WHITE);
		board.place("e4", knight);

		Pawn friendly = new Pawn(Color.WHITE);
		board.place("f6", friendly);

		List<Move> moves = knight.getPossibleMoves(state);
		boolean blocked = moves.stream().anyMatch(m -> m.to().equals(Position.of("f6")));

		assertFalse(blocked, "Knight should not move to square occupied by friendly piece");
	}

    @Test
    void testKnightJumpsOverPieces() {
        Knight knight = new Knight(Color.WHITE);
        board.place("e4", knight);

        // Surround e4 without occupying the knight's destination squares
        List<String> blockers = List.of("d4", "d5", "e5", "f5", "f4", "f3", "e3", "d3");
        for (String square : blockers) {
            board.place(square, new Pawn(Color.WHITE));
        }

        List<Move> moves = knight.getPossibleMoves(state);
        Set<Position> destinations = moves.stream().map(Move::to).collect(Collectors.toSet());

        Set<Position> expected = Set.of(
            Position.of("d6"), Position.of("f6"),
            Position.of("c5"), Position.of("g5"),
            Position.of("c3"), Position.of("g3"),
            Position.of("d2"), Position.of("f2")
        );

        assertEquals(expected, destinations, "Knight should jump over surrounding pieces to all 8 destinations");
    }

    @Test
    void testKnightBlockedByAllFriendlyPieces() {
        Knight knight = new Knight(Color.WHITE);
        board.place("e4", knight);

        // Block all destinations
        for (Position pos : Set.of(
                Position.of("d6"), Position.of("f6"),
                Position.of("c5"), Position.of("g5"),
                Position.of("c3"), Position.of("g3"),
                Position.of("d2"), Position.of("f2"))) {
            board.place(pos.toString(), new Pawn(Color.WHITE));
        }

        List<Move> moves = knight.getPossibleMoves(state);
        assertTrue(moves.isEmpty(), "Knight should have no legal moves when all are blocked by friendly pieces");
    }

	@Test
	void testKnightMixedTargets() {
		Knight knight = new Knight(Color.WHITE);
		board.place("e4", knight);

		board.place("d6", new Pawn(Color.BLACK));
		board.place("f6", new Pawn(Color.WHITE));
		board.place("c5", new Pawn(Color.BLACK));
		board.place("g5", new Pawn(Color.WHITE));

		List<Move> moves = knight.getPossibleMoves(state);
		Set<Position> destinations = moves.stream().map(Move::to).collect(Collectors.toSet());

		assertTrue(destinations.contains(Position.of("d6")));
		assertFalse(destinations.contains(Position.of("f6")));
		assertTrue(destinations.contains(Position.of("c5")));
		assertFalse(destinations.contains(Position.of("g5")));

        assertTrue(destinations.containsAll(Set.of(
                Position.of("c3"), Position.of("g3"),
                Position.of("d2"), Position.of("f2")
        )));
	}
}
