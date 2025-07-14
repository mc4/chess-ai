package dev.markconley.chess.engine.move;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.markconley.chess.engine.board.Board;
import dev.markconley.chess.engine.core.Color;
import dev.markconley.chess.engine.core.Position;
import dev.markconley.chess.engine.pieces.King;
import dev.markconley.chess.engine.pieces.Pawn;
import dev.markconley.chess.engine.pieces.Piece;
import dev.markconley.chess.engine.state.BoardState;

class MoveCaptureTest {

    private Board board;
    private BoardState state;
    private MoveExecutor executor;

    @BeforeEach
    void setUp() {
        board = Board.emptyBoard()
                .place("e1", new King(Color.WHITE))
                .place("e8", new King(Color.BLACK));
        state = new BoardState(board);
        executor = new MoveExecutor();
    }

    @Test
    void testShouldReplaceCapturedPiece() {
        // Arrange
        Position from = Position.of("d4");
        Position to = Position.of("d6");
        Piece whitePawn = new Pawn(Color.WHITE);
        Piece blackPawn = new Pawn(Color.BLACK);

        board.setPieceAt(from, whitePawn);
        board.setPieceAt(to, blackPawn);

        Move move = MoveFactory.capture(from, to, whitePawn, blackPawn);

        // Act
        executor.applyMove(state, move);

        // Assert
        Piece result = board.getPieceAt(to);
        assertEquals(whitePawn, result, "Destination square should now contain the attacking white pawn");
        assertEquals(Position.of("d6"), whitePawn.getPosition(), "Attacker's internal position should be updated");
    }

    @Test
    void testShouldReturnTrueOnSuccessfulCapture() {
        // Arrange
        Position from = Position.of("d4");
        Position to = Position.of("d6");
        Piece whitePawn = new Pawn(Color.WHITE);
        Piece blackPawn = new Pawn(Color.BLACK);

        board.setPieceAt(from, whitePawn);
        board.setPieceAt(to, blackPawn);

        Move move = MoveFactory.capture(from, to, whitePawn, blackPawn);

        // Act
        executor.applyMove(state, move);

        // Assert
        assertEquals(whitePawn, board.getPieceAt(to), "Capture should result in attacker occupying target square");
        assertNull(board.getPieceAt(from), "Original square should be cleared after capture");
    }

}
