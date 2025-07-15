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

class MoveExecutionTest {
	
    private Board board;
    private BoardState state;
    private MoveExecutor executor;

    @BeforeEach
    void init() {
        board = Board.emptyBoard()
            .place("e1", new King(Color.WHITE))
            .place("e8", new King(Color.BLACK))
            .place("e2", new Pawn(Color.WHITE));
        state = new BoardState(board);
        executor = new MoveExecutor();
    }
    
    @Test
    void testShouldMovePieceToNewSquare() {
        Position from = Position.of("e2");
        Position to = Position.of("e4");
        Pawn pawn = new Pawn(Color.WHITE);

        board.setPieceAt(from, pawn);
        state = new BoardState(board);
        executor = new MoveExecutor();

        Move move = MoveFactory.normal(from, to, pawn);
        executor.applyMove(state, move);

        assertEquals(pawn, board.getPieceAt(to), "Pawn should be at the destination square");
    }


    @Test
    void testShouldMovePawnForward() {
        Position from = Position.of("e2");
        Position to = Position.of("e4");
        Piece pawn = board.getPieceAt(from);

        Move move = MoveFactory.normal(from, to, pawn);
        executor.applyMove(state, move);

        assertNull(board.getPieceAt(from), "Original square should be empty after move");
        assertEquals(pawn, board.getPieceAt(to), "Pawn should be on the destination square");
        assertEquals(to, pawn.getPosition(), "Pawn’s internal position should be updated");
    }
    
    @Test
    void testShouldClearOriginalSquare() {
        Position from = Position.of("e2");
        Position to = Position.of("e4");
        Pawn pawn = new Pawn(Color.WHITE);

        board.setPieceAt(from, pawn);
        state = new BoardState(board);
        executor = new MoveExecutor();

        Move move = MoveFactory.normal(from, to, pawn);
        executor.applyMove(state, move);

        assertNull(board.getPieceAt(from), "Original square should be empty after move");
    }

    
    @Test
    void testShouldUpdatePiecePosition() {
        Position from = Position.of("e2");
        Position to = Position.of("e4");
        Pawn pawn = new Pawn(Color.WHITE);

        board.setPieceAt(from, pawn);
        state = new BoardState(board);
        executor = new MoveExecutor();

        Move move = MoveFactory.normal(from, to, pawn);
        executor.applyMove(state, move);

        assertEquals(to, pawn.getPosition(), "Pawn’s internal position should reflect the destination");
    }


}
