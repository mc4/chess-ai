package dev.markconley.chess.engine.state;

import dev.markconley.chess.engine.board.Board;
import dev.markconley.chess.engine.core.Color;
import dev.markconley.chess.engine.core.Position;
import dev.markconley.chess.engine.move.AttackMapGenerator;
import dev.markconley.chess.engine.move.MoveExecutor;
import dev.markconley.chess.game.GameStatus;

public class GameStateEvaluator {
	
	private static final MoveExecutor moveExecutor = new MoveExecutor();

	private GameStateEvaluator() { } // Utility class


	public static GameStatus evaluate(BoardState state, Color color) {
		boolean inCheck = isInCheck(state, color);
		boolean hasMoves = hasLegalMoves(state, color);

		if (inCheck && !hasMoves) {
			return color == Color.WHITE ? GameStatus.CHECKMATE_BLACK_WINS : GameStatus.CHECKMATE_WHITE_WINS;
		}
		if (!inCheck && !hasMoves) {
			return GameStatus.STALEMATE;
		}

		return GameStatus.IN_PROGRESS;
	}

	public static boolean isInCheck(BoardState state, Color color) {
		Board board = state.getBoard();
		Position kingPos = board.findKingPosition(color);
		Color opponentColor = color.opposite();
		return AttackMapGenerator.generateAttackSquares(board, opponentColor).contains(kingPos);
	}

	public static boolean hasLegalMoves(BoardState state, Color color) {
	    return state.getActivePieces(color).stream()
	        .flatMap(piece -> piece.getPossibleMoves(state).stream())
	        .anyMatch(move -> {
	            BoardState simulated = state.copy();
	            moveExecutor.applyMove(simulated, move);
	            return !isInCheck(simulated, color);
	        });
	}

	public static boolean isCheckmate(BoardState state, Color color) {
		return isInCheck(state, color) && !hasLegalMoves(state, color);
	}

	public static boolean isStalemate(BoardState state, Color color) {
		return !isInCheck(state, color) && !hasLegalMoves(state, color);
	}

}
