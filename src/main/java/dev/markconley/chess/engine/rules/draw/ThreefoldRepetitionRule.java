package dev.markconley.chess.engine.rules.draw;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import dev.markconley.chess.engine.board.Board;
import dev.markconley.chess.engine.move.Move;
import dev.markconley.chess.engine.state.BoardState;
import dev.markconley.chess.game.GameStatus;

public class ThreefoldRepetitionRule implements DrawRule {
	
    @Override
    public Optional<GameStatus> check(Board board, BoardState state, List<Move> moveHistory, List<BoardState> history) {
        Map<BoardState, Integer> freqMap = new HashMap<>();

        for (BoardState past : history) {
            freqMap.merge(past, 1, Integer::sum);
        }

        if (freqMap.getOrDefault(state, 0) >= 3) {
            return Optional.of(GameStatus.THREEFOLD_REPETITION);
        }
        return Optional.empty();
    }
    
}

