package dev.markconley.chess.engine.state;

import dev.markconley.chess.engine.board.Board;
import dev.markconley.chess.engine.core.Color;
import dev.markconley.chess.engine.core.Position;
import dev.markconley.chess.engine.move.AttackMapGenerator;
import dev.markconley.chess.engine.move.MoveGenerator;
import dev.markconley.chess.engine.pieces.Piece;
import dev.markconley.chess.engine.pieces.PieceType;

public class GameStateEvaluator {
	
    private GameStateEvaluator() { } // Utility class

	public static boolean isInCheck(Board board, Color color) {
		Position kingPos = findKingPosition(board, color);
		Color opponentColor = color.opposite();
		return AttackMapGenerator.generateAttackSquares(board, opponentColor).contains(kingPos);
	}

    public static boolean hasLegalMoves(Board board, Color color) {
        return board.getActivePieces(color)
        		.stream()
                .flatMap(piece -> MoveGenerator.generateMoves(board, piece).stream())
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

    private static Position findKingPosition(Board board, Color color) {
        return board.getActivePieces(color)
        		.stream()
                .filter(p -> p.getPieceType() == PieceType.KING)
                .map(Piece::getPosition)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("King not found on board"));
    }

}
