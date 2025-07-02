package dev.markconley.chess.engine.rules.draw;

import java.util.List;
import java.util.Optional;

import dev.markconley.chess.engine.board.Board;
import dev.markconley.chess.engine.move.Move;
import dev.markconley.chess.engine.state.BoardState;
import dev.markconley.chess.game.GameStatus;

public interface DrawRule {
    Optional<GameStatus> check(Board board, BoardState state, List<Move> moveHistory, List<BoardState> history);
}