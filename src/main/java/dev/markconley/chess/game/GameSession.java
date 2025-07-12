
package dev.markconley.chess.game;

import java.util.List;

import dev.markconley.chess.engine.board.Board;
import dev.markconley.chess.engine.core.Color;
import dev.markconley.chess.engine.move.LegalMoveGenerator;
import dev.markconley.chess.engine.move.Move;
import dev.markconley.chess.engine.move.MoveExecutor;
import dev.markconley.chess.engine.state.BoardState;
import dev.markconley.chess.io.InputProvider;
import dev.markconley.chess.io.OutputHandler;

public class GameSession {
    private final MoveInputHandler moveInputHandler;
    private final GameResultEvaluator resultEvaluator;
    private final MoveExecutor moveExecutor;
    private final HistoryTracker historyTracker;
    private final Board board;
    private BoardState boardState;
    private Color currentTurn = Color.WHITE;

    public GameSession(Board board,
                       BoardState initialState,
                       MoveInputHandler moveInputHandler,
                       GameResultEvaluator resultEvaluator,
                       HistoryTracker historyTracker, MoveExecutor moveExecutor) {
		this.board = board;
        this.boardState = initialState;
        this.moveInputHandler = moveInputHandler;
        this.moveExecutor = moveExecutor;
        this.resultEvaluator = resultEvaluator;
        this.historyTracker = historyTracker;
    }

	public void play(InputProvider input, OutputHandler output) {
		GameStatus status = GameStatus.IN_PROGRESS;

		while (status == GameStatus.IN_PROGRESS) {
			output.displayBoard(board);

		    List<Move> legalMoves = LegalMoveGenerator.generateLegalMoves(boardState, currentTurn);
		    Move move = moveInputHandler.getValidMove(currentTurn, boardState, legalMoves, input, output);
		    
			if (move == null) {
				output.displayMessage("No move received - resigning.");
				status = GameStatus.RESIGNATION;
				break;
			}

			moveExecutor.applyMove(boardState, move);
			historyTracker.record(boardState, move);

			status = resultEvaluator.evaluate(board, boardState, currentTurn, legalMoves, historyTracker);
			currentTurn = currentTurn.opposite();
		}

		output.displayResult(status);
	}
	
}

