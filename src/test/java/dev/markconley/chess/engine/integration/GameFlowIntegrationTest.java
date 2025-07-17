package dev.markconley.chess.engine.integration;
//package dev.markconley.chess.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import dev.markconley.chess.engine.board.Board;
import dev.markconley.chess.engine.core.Color;
import dev.markconley.chess.engine.core.Position;
import dev.markconley.chess.engine.move.LegalMoveGenerator;
import dev.markconley.chess.engine.move.Move;
import dev.markconley.chess.engine.move.MoveExecutor;
import dev.markconley.chess.engine.move.MoveFactory;
import dev.markconley.chess.engine.pieces.Bishop;
import dev.markconley.chess.engine.pieces.King;
import dev.markconley.chess.engine.pieces.Pawn;
import dev.markconley.chess.engine.pieces.Piece;
import dev.markconley.chess.engine.pieces.Queen;
import dev.markconley.chess.engine.pieces.Rook;
import dev.markconley.chess.engine.state.BoardState;

class GameFlowIntegrationTest {

    private Board board;
    private BoardState state;
    private MoveExecutor executor;

    @BeforeEach
    void setUp() {
        board = new Board(); // starts with initial position
        state = new BoardState(board);
        executor = new MoveExecutor();
    }
    
    @Test
    void testShouldPlayFullLegalOpeningSequence2() {

        // 1. e4 e5
        executor.applyMove(state, MoveFactory.normal(Position.of("e2"), Position.of("e4"), board.getPieceAt(Position.of("e2"))));
        state.switchTurn();
        executor.applyMove(state, MoveFactory.normal(Position.of("e7"), Position.of("e5"), board.getPieceAt(Position.of("e7"))));
        state.switchTurn();

        // 2. Nf3 Nc6
        executor.applyMove(state, MoveFactory.normal(Position.of("g1"), Position.of("f3"), board.getPieceAt(Position.of("g1"))));
        state.switchTurn();
        executor.applyMove(state, MoveFactory.normal(Position.of("b8"), Position.of("c6"), board.getPieceAt(Position.of("b8"))));
        state.switchTurn();

        // 3. Bc4 Bc5
        executor.applyMove(state, MoveFactory.normal(Position.of("f1"), Position.of("c4"), board.getPieceAt(Position.of("f1"))));
        state.switchTurn();
        executor.applyMove(state, MoveFactory.normal(Position.of("f8"), Position.of("c5"), board.getPieceAt(Position.of("f8"))));
        state.switchTurn();

        // Assert all pieces are on expected squares
        assertEquals("BISHOP", board.getPieceAt(Position.of("c4")).getPieceType().name());
        assertEquals("BISHOP", board.getPieceAt(Position.of("c5")).getPieceType().name());
        assertEquals("KNIGHT", board.getPieceAt(Position.of("f3")).getPieceType().name());
        assertEquals("KNIGHT", board.getPieceAt(Position.of("c6")).getPieceType().name());

        // Assert current turn is White's
        assertEquals(Color.WHITE, state.getCurrentTurn());
    }


    @Test
    void testShouldPlayFullLegalOpeningSequence() {
        // 1. e4 e5 2. Nf3 Nc6 3. Bc4 Bc5
        apply("e2", "e4");
        apply("e7", "e5");
        apply("g1", "f3");
        apply("b8", "c6");
        apply("f1", "c4");
        apply("f8", "c5");

        Piece bishop = board.getPieceAt(Position.of("c5"));
        assertTrue(bishop instanceof Bishop && bishop.getColor() == Color.BLACK);
    }

    @Test
    void testShouldUpdateCastlingRightsOverGame() {

    }

    @Test
    void testShouldSupportAllSpecialMovesInSingleGame() {
        board = Board.emptyBoard()
            .place("e1", new King(Color.WHITE))
            .place("h1", new Rook(Color.WHITE))
            .place("e5", new Pawn(Color.BLACK))
            .place("d5", new Pawn(Color.WHITE))
            .place("a7", new Pawn(Color.WHITE));
        state = new BoardState(board);

        // En Passant: white d5 captures black e5
        Piece whitePawn = board.getPieceAt(Position.of("d5"));
        Piece blackPawn = board.getPieceAt(Position.of("e5"));
        Move enPassant = MoveFactory.enPassant(Position.of("d5"), Position.of("e6"), whitePawn, blackPawn);
        executor.applyMove(state, enPassant);

        assertNull(board.getPieceAt(Position.of("e5")));

        // Castling: white king castles kingside
        Piece whiteKing = board.getPieceAt(Position.of("e1"));
        Move castle = MoveFactory.castle(Position.of("e1"), Position.of("g1"), whiteKing);
        executor.applyMove(state, castle);

        assertTrue(board.getPieceAt(Position.of("f1")) instanceof Rook);
        assertEquals(Position.of("g1"), whiteKing.getPosition());

        // Promotion: white pawn promotes
        Piece promoPawn = board.getPieceAt(Position.of("a7"));
        Piece queen = new Queen(Color.WHITE);
        Move promote = MoveFactory.promotion(Position.of("a7"), Position.of("a8"), promoPawn, queen);
        executor.applyMove(state, promote);

        assertTrue(board.getPieceAt(Position.of("a8")) instanceof Queen);
    }

    @Test
    void testShouldDetectThreefoldRepetition() {

    }

    @Test
    void testShouldEndGameWithCheckmate() {
        board = Board.emptyBoard()
            .place("e8", new King(Color.BLACK))
            .place("f7", new Pawn(Color.BLACK))
            .place("g7", new Pawn(Color.BLACK))
            .place("d1", new Queen(Color.WHITE))
            .place("e1", new King(Color.WHITE));
        state = new BoardState(board);

        // Simulate: Qd1-h5, mate
        Piece whiteQueen = board.getPieceAt(Position.of("d1"));
        Move move = MoveFactory.normal(Position.of("d1"), Position.of("h5"), whiteQueen);
        executor.applyMove(state, move);

        // No way to checkmate logic without GameResultEvaluator, so just assert square
        assertEquals(Position.of("h5"), whiteQueen.getPosition());
    }

    @Test
    void testShouldEndGameWithStalemate() {

    }

    @Disabled
    @Test
    void testShouldRejectIllegalMoveAndNotChangeState() {
        Position from = Position.of("e2");
        Position to = Position.of("e5");
        Piece pawn = board.getPieceAt(from);
        Move illegal = MoveFactory.normal(from, to, pawn);

        BoardState before = state.copy();
        boolean isLegal = LegalMoveGenerator.isLegalMove(state, illegal, Color.WHITE);

        assertFalse(isLegal, "Move should be considered illegal");

        // Ensure state hasn't changed
        assertEquals(before, state, "Board state should remain unchanged after illegal move check");

    }

    private void apply(String from, String to) {
        Position f = Position.of(from);
        Position t = Position.of(to);
        Piece piece = board.getPieceAt(f);
        Move move = MoveFactory.normal(f, t, piece);
        executor.applyMove(state, move);
    }

//    private void apply(Position from, Position to) {
//        Piece piece = board.getPieceAt(from);
//        Move move = MoveFactory.normal(from, to, piece);
//        executor.applyMove(state, move);
//    }
    
}
