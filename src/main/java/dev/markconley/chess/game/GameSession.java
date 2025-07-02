
package dev.markconley.chess.game;

import java.util.List;

import dev.markconley.chess.engine.board.Board;
import dev.markconley.chess.engine.core.Color;
import dev.markconley.chess.engine.move.LegalMoveGenerator;
import dev.markconley.chess.engine.move.Move;
import dev.markconley.chess.engine.state.BoardState;
import dev.markconley.chess.io.InputProvider;
import dev.markconley.chess.io.OutputHandler;

public class GameSession {
    private final TurnManager turnManager;
    private final GameResultEvaluator resultEvaluator;
    private final HistoryTracker historyTracker;
    private final Board board;
    private BoardState boardState;
    private Color currentTurn = Color.WHITE;

    public GameSession(Board board,
                       BoardState initialState,
                       TurnManager turnManager,
                       GameResultEvaluator resultEvaluator,
                       HistoryTracker historyTracker) {
        this.board = board;
        this.boardState = initialState;
        this.turnManager = turnManager;
        this.resultEvaluator = resultEvaluator;
        this.historyTracker = historyTracker;
    }

	public void play(InputProvider input, OutputHandler output) {
		GameStatus status = GameStatus.IN_PROGRESS;

		while (status == GameStatus.IN_PROGRESS) {
			output.displayBoard(board);

		    List<Move> legalMoves = LegalMoveGenerator.generateLegalMoves(boardState, currentTurn);
		    Move move = turnManager.getValidMove(currentTurn, boardState, legalMoves, input, output);
		    
			if (move == null) {
				output.displayMessage("No move received - resigning.");
				status = GameStatus.RESIGNATION;
				break;
			}

			turnManager.executeMove(boardState, move);
			historyTracker.record(boardState, move);

			status = resultEvaluator.evaluate(board, boardState, currentTurn, legalMoves, historyTracker);
			currentTurn = currentTurn.opposite();
		}

		output.displayResult(status);
	}
	
}

