package dev.markconley.chess.engine.move.specialmove;

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
import dev.markconley.chess.engine.pieces.King;
import dev.markconley.chess.engine.pieces.Rook;
import dev.markconley.chess.engine.state.BoardState;


class CastlingMoveTest {

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
	void testWhiteKingsideCastling() {
		board.place("e1", new King(Color.WHITE));
		board.place("h1", new Rook(Color.WHITE));

		Move move = MoveFactory.castle(Position.of("e1"), Position.of("g1"), board.getPieceAt(Position.of("e1")));
		moveExecutor.applyMove(state, move);

		assertNotNull(board.getPieceAt(Position.of("g1")));
		assertNotNull(board.getPieceAt(Position.of("f1")));
		assertNull(board.getPieceAt(Position.of("e1")));
		assertNull(board.getPieceAt(Position.of("h1")));
	}

	@Test
	void testWhiteQueensideCastling() {
		board.place("e1", new King(Color.WHITE));
		board.place("a1", new Rook(Color.WHITE));

		Move move = MoveFactory.castle(Position.of("e1"), Position.of("c1"), board.getPieceAt(Position.of("e1")));
		moveExecutor.applyMove(state, move);

		assertNotNull(board.getPieceAt(Position.of("c1")));
		assertNotNull(board.getPieceAt(Position.of("d1")));
		assertNull(board.getPieceAt(Position.of("e1")));
		assertNull(board.getPieceAt(Position.of("a1")));
	}

	@Test
	void testBlackKingsideCastling() {
		board.place("e8", new King(Color.BLACK));
		board.place("h8", new Rook(Color.BLACK));

		Move move = MoveFactory.castle(Position.of("e8"), Position.of("g8"), board.getPieceAt(Position.of("e8")));
		moveExecutor.applyMove(state, move);

		assertNotNull(board.getPieceAt(Position.of("g8")));
		assertNotNull(board.getPieceAt(Position.of("f8")));
		assertNull(board.getPieceAt(Position.of("e8")));
		assertNull(board.getPieceAt(Position.of("h8")));
	}

}
