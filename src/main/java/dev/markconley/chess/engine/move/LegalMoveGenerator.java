package dev.markconley.chess.engine.move;

import java.util.List;

import dev.markconley.chess.engine.board.Board;
import dev.markconley.chess.engine.core.Color;
import dev.markconley.chess.engine.state.GameStateEvaluator;

public class LegalMoveGenerator {

	private LegalMoveGenerator() { 	
	}

	public static List<Move> generateLegalMoves(Board board, Color color) {
	    return board.getActivePieces(color)
	                .stream()
	                .flatMap(p -> p.getPossibleMoves(board, board.getSpecialMoveService()).stream())
	                .filter(move -> isLegalMove(board, move, color))
	                .toList();
	}

	public static boolean isLegalMove(Board board, Move move, Color color) {
		Board simulated = board.copy();
		simulated.makeMove(move.from(), move.to());
		return !GameStateEvaluator.isInCheck(simulated, color);
	}
	
}
