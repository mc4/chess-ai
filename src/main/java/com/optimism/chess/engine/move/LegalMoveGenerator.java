package com.optimism.chess.engine.move;

import java.util.List;

import com.optimism.chess.engine.board.Board;
import com.optimism.chess.engine.core.Color;
import com.optimism.chess.engine.state.GameStateEvaluator;

public class LegalMoveGenerator {

	private LegalMoveGenerator() { 	
	}

	public static List<Move> generateLegalMoves(Board board, Color color) {
	    return board.getActivePieces(color)
	                .stream()
	                .flatMap(p -> MoveGenerator.generateMoves(board, p).stream())
	                .filter(move -> isLegalMove(board, move, color))
	                .toList();
	}

	public static boolean isLegalMove(Board board, Move move, Color color) {
		Board simulated = board.copy();
		simulated.makeMove(move.from(), move.to());
		return !GameStateEvaluator.isInCheck(simulated, color);
	}
	
}
