package dev.markconley.chess.engine.state;

import dev.markconley.chess.engine.board.Board;
import dev.markconley.chess.engine.core.Color;
import dev.markconley.chess.engine.core.Position;
import dev.markconley.chess.engine.move.AttackMapGenerator;

public class GameStateEvaluator {
	
    private GameStateEvaluator() { } // Utility class

	public static boolean isInCheck(Board board, Color color) {
		Position kingPos = Board.findKingPosition(board, color);
		Color opponentColor = color.opposite();
		return AttackMapGenerator.generateAttackSquares(board, opponentColor).contains(kingPos);
	}

    public static boolean hasLegalMoves(Board board, Color color) {
        return board.getActivePieces(color)
        		.stream()
                .flatMap(piece -> piece.getPossibleMoves(board, board.getSpecialMoveService()).stream())
                .anyMatch(move -> {
                    Board simulated = board.copy();
                    simulated.makeMove(move.from(), move.to());
                    return !isInCheck(simulated, color);
                });
    }

	public static boolean isCheckmate(Board board, Color color) {
		return isInCheck(board, color) && !hasLegalMoves(board, color);
	}

	public static boolean isStalemate(Board board, Color color) {
		return !isInCheck(board, color) && !hasLegalMoves(board, color);
	}

}
