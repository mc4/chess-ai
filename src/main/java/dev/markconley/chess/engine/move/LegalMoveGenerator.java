package dev.markconley.chess.engine.move;

import java.util.List;

import dev.markconley.chess.engine.core.Color;
import dev.markconley.chess.engine.state.BoardState;
import dev.markconley.chess.engine.state.GameStateEvaluator;

public class LegalMoveGenerator {

	private LegalMoveGenerator() { }

    public static List<Move> generateLegalMoves(BoardState state, Color color) {
        return state.getBoard()
                .getActivePieces(color)
                .stream()
                .flatMap(p -> p.getPossibleMoves(state).stream())
                .filter(move -> isLegalMove(state, move, color))
                .toList();
    }

    public static boolean isLegalMove(BoardState originalState, Move move, Color color) {
        BoardState simulatedState = originalState.copy();
        simulatedState.setCurrentTurn(color); 

        MoveExecutor executor = new MoveExecutor();
        executor.applyMove(simulatedState, move);

        return !GameStateEvaluator.isInCheck(simulatedState, color);
    }

}
