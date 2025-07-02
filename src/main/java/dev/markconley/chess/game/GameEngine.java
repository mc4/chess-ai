package dev.markconley.chess.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dev.markconley.chess.engine.board.Board;
import dev.markconley.chess.engine.core.Color;
import dev.markconley.chess.engine.move.LegalMoveGenerator;
import dev.markconley.chess.engine.move.Move;
import dev.markconley.chess.engine.move.MoveExecutor;
import dev.markconley.chess.engine.rules.draw.DrawRuleEngine;
import dev.markconley.chess.engine.state.BoardState;
import dev.markconley.chess.engine.state.GameStateEvaluator;
import dev.markconley.chess.io.InputProvider;
import dev.markconley.chess.io.OutputHandler;

public class GameEngine {
	private Board board;
	private BoardState boardState;
	private Color currentTurn = Color.WHITE;

	private final MoveExecutor moveExecutor;
	private final DrawRuleEngine drawRuleEngine;

	private final List<Move> moveHistory = new ArrayList<>();
	private final List<BoardState> stateHistory = new ArrayList<>();

	public GameEngine(Board board, 
			BoardState initialState, 
			MoveExecutor moveExecutor,
			LegalMoveGenerator legalMoveGenerator, 
			GameStateEvaluator gameStateEvaluator,
			DrawRuleEngine drawRuleEngine) {
		this.board = board;
		this.boardState = initialState;
		this.moveExecutor = moveExecutor;
		this.drawRuleEngine = drawRuleEngine;
	}

	public void play(InputProvider input, OutputHandler output) {
		GameStatus result = GameStatus.IN_PROGRESS;

		while (result == GameStatus.IN_PROGRESS) {
			output.displayBoard(board);
			List<Move> legalMoves = LegalMoveGenerator.generateLegalMoves(boardState, currentTurn);

			if (isGameOverByNoLegalMoves(legalMoves)) {
				result = GameStateEvaluator.evaluate(boardState, currentTurn);
				break;
			}

			output.promptMove(currentTurn, legalMoves);
			Optional<Move> maybeMove = input.getMove(legalMoves);

			if (maybeMove.isEmpty()) {
				output.displayMessage("No move received - resigning.");
				result = GameStatus.RESIGNATION;
				break;
			}

			Move move = maybeMove.get();
			if (!legalMoves.contains(move)) {
				output.displayMessage("Illegal move. Try again.");
				continue;
			}

			moveExecutor.applyMove(boardState, move);
			updateHistories(move);

			result = drawRuleEngine.checkDraw(board, boardState, moveHistory, stateHistory);
			currentTurn = currentTurn.opposite();
		}

		output.displayResult(result);
	}

	private boolean isGameOverByNoLegalMoves(List<Move> legalMoves) {
		return legalMoves.isEmpty();
	}

	private void updateHistories(Move move) {
		moveHistory.add(move);
		stateHistory.add(boardState.copy());
	}
	
}
