package dev.markconley.chess.game;

import java.util.List;

import dev.markconley.chess.engine.board.Board;
import dev.markconley.chess.engine.core.Color;
import dev.markconley.chess.engine.move.Move;
import dev.markconley.chess.engine.rules.draw.DrawRuleEngine;
import dev.markconley.chess.engine.state.BoardState;
import dev.markconley.chess.engine.state.GameStateEvaluator;

public class GameResultEvaluator {
	private final DrawRuleEngine drawRuleEngine;

	public GameResultEvaluator(DrawRuleEngine drawRuleEngine) {
		this.drawRuleEngine = drawRuleEngine;
	}

	public GameStatus evaluate(Board board, BoardState boardState, Color turn, List<Move> legalMoves,
			HistoryTracker history) {
		if (legalMoves.isEmpty()) {
			return GameStateEvaluator.evaluate(boardState, turn);
		}
		return drawRuleEngine.checkDraw(board, boardState, history.getMoves(), history.getStates());
	}

}
