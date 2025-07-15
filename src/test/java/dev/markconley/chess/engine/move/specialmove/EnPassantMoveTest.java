package dev.markconley.chess.engine.move.specialmove;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.markconley.chess.engine.board.Board;
import dev.markconley.chess.engine.core.Color;
import dev.markconley.chess.engine.core.Position;
import dev.markconley.chess.engine.move.Move;
import dev.markconley.chess.engine.move.MoveExecutor;
import dev.markconley.chess.engine.move.MoveFactory;
import dev.markconley.chess.engine.pieces.Pawn;
import dev.markconley.chess.engine.state.BoardState;

public class EnPassantMoveTest {

    private Board board;
    private BoardState state;
    private MoveExecutor moveExecutor;

    @BeforeEach
    void setUp() {
        board = Board.emptyBoard();
        state = new BoardState(board);
        moveExecutor = new MoveExecutor();
    }

    @Test
    void testEnPassantCapture() {
        Pawn whitePawn = new Pawn(Color.WHITE);
        Pawn blackPawn = new Pawn(Color.BLACK);

        board.place("e5", whitePawn);
        board.place("d5", blackPawn);

        state.recordMove(MoveFactory.normal(Position.of("d7"), Position.of("d5"), blackPawn));

        Move enPassant = MoveFactory.enPassant(
                Position.of("e5"),
                Position.of("d6"),
                whitePawn,
                blackPawn
        );

        moveExecutor.applyMove(state, enPassant);

        assertNotNull(board.getPieceAt(Position.of("d6"))); // White pawn moved
        assertNull(board.getPieceAt(Position.of("e5")));    // Original square empty
        assertNull(board.getPieceAt(Position.of("d5")));    // Captured pawn removed
    }

    @Test
    void testNoEnPassantTargetIfPawnMovesOneSquare() {
        Pawn whitePawn = new Pawn(Color.WHITE);
        board.place("e2", whitePawn);

        Move move = MoveFactory.normal(Position.of("e2"), Position.of("e3"), whitePawn);
        state.recordMove(move);

        assertNull(state.getEnPassantTarget(), "En passant target should not be set after one-square pawn move");
    }

    @Test
    void testEnPassantTargetSetCorrectly() {
        Pawn whitePawn = new Pawn(Color.WHITE);
        board.place("e2", whitePawn);

        Move move = MoveFactory.normal(Position.of("e2"), Position.of("e4"), whitePawn);
        state.recordMove(move);

        assertEquals(Position.of("e3"), state.getEnPassantTarget(), "En passant target should be square behind destination");
    }
    
}
