package dev.markconley.chess.engine.rules.draw;

import java.util.List;
import java.util.Optional;

import dev.markconley.chess.engine.board.Board;
import dev.markconley.chess.engine.move.Move;
import dev.markconley.chess.engine.state.BoardState;
import dev.markconley.chess.game.GameStatus;

public class DrawRuleEngine {
	
    private final List<DrawRule> rules;

    public DrawRuleEngine() {
        this.rules = List.of(
            new FiftyMoveRule(),
            new ThreefoldRepetitionRule(),
            new InsufficientMaterialRule()
        );
    }

	public GameStatus checkDraw(Board board, BoardState state, List<Move> moveHistory, List<BoardState> history) {
		for (DrawRule rule : rules) {
			Optional<GameStatus> result = rule.check(board, state, moveHistory, history);
			if (result.isPresent()) {
				return result.get();
			}
		}
		return GameStatus.IN_PROGRESS;
	}
	
}
