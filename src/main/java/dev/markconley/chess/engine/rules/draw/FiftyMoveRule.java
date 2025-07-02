package dev.markconley.chess.engine.rules.draw;

import java.util.List;
import java.util.Optional;

import dev.markconley.chess.engine.board.Board;
import dev.markconley.chess.engine.move.Move;
import dev.markconley.chess.engine.state.BoardState;
import dev.markconley.chess.game.GameStatus;

public class FiftyMoveRule implements DrawRule {
    @Override
    public Optional<GameStatus> check(Board board, BoardState state, List<Move> moveHistory, List<BoardState> history) {
        int halfMoveClock = state.getHalfMoveClock();
        if (halfMoveClock >= 100) {
            return Optional.of(GameStatus.FIFTY_MOVE_RULE);
        }
        return Optional.empty();
    }
}