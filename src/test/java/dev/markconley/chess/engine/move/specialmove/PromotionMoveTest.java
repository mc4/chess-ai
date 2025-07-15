package dev.markconley.chess.engine.move.specialmove;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
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
import dev.markconley.chess.engine.pieces.Queen;
import dev.markconley.chess.engine.state.BoardState;

class PromotionMoveTest {

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
    void testPromotionToQueen() {
        Pawn pawn = new Pawn(Color.WHITE);
        Queen promoted = new Queen(Color.WHITE);
        board.place("e7", pawn);

        Move move = MoveFactory.promotion(Position.of("e7"), Position.of("e8"), pawn, promoted);
        moveExecutor.applyMove(state, move);

        assertInstanceOf(Queen.class, board.getPieceAt(Position.of("e8")));
        assertEquals(Color.WHITE, board.getPieceAt(Position.of("e8")).getColor());
        assertEquals(Position.of("e8"), board.getPieceAt(Position.of("e8")).getPosition());
        assertNull(board.getPieceAt(Position.of("e7")));
    }

    @Test
    void testHalfMoveClockResetAfterPromotion() {
        Pawn pawn = new Pawn(Color.WHITE);
        Queen promoted = new Queen(Color.WHITE);
        board.place("e7", pawn);
        state.setHalfMoveClock(50);

        Move move = MoveFactory.promotion(Position.of("e7"), Position.of("e8"), pawn, promoted);
        moveExecutor.applyMove(state, move);

        assertEquals(0, state.getHalfMoveClock());
    }

    @Test
    void testPromotionWithCapture() {
        Pawn pawn = new Pawn(Color.WHITE);
        Queen enemyQueen = new Queen(Color.BLACK);
        Queen promoted = new Queen(Color.WHITE);
        board.place("e7", pawn);
        board.place("f8", enemyQueen);

        Move move = new Move(Position.of("e7"), Position.of("f8"), pawn, enemyQueen, false, false, promoted);
        moveExecutor.applyMove(state, move);

        assertInstanceOf(Queen.class, board.getPieceAt(Position.of("f8")));
        assertEquals(Color.WHITE, board.getPieceAt(Position.of("f8")).getColor());
        assertNull(board.getPieceAt(Position.of("e7")));
    }
    
}
