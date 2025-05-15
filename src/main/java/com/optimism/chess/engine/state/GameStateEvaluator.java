package com.optimism.chess.engine.state;

import java.util.Objects;

import com.optimism.chess.engine.board.Board;
import com.optimism.chess.engine.core.Color;
import com.optimism.chess.engine.core.Position;
import com.optimism.chess.engine.move.MoveGenerator;
import com.optimism.chess.engine.pieces.Piece;
import com.optimism.chess.engine.pieces.PieceType;

public class GameStateEvaluator {
	
    private GameStateEvaluator() {
        // Utility class
    }

    public static boolean isInCheck(Board board, Color color) {
        Position kingPos = findKingPosition(board, color);
        Color opponentColor = (color == Color.WHITE) ? Color.BLACK : Color.WHITE;

        return board.getPiecesOfColor(opponentColor).stream()
                .flatMap(piece -> MoveGenerator.generateMoves(board, piece).stream())
                .anyMatch(move -> Objects.equals(move.to(), kingPos));
    }

    public static boolean hasLegalMoves(Board board, Color color) {
        return board.getPiecesOfColor(color).stream()
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
        return board.getPiecesOfColor(color).stream()
                .filter(p -> p.getPieceType() == PieceType.KING)
                .map(Piece::getPosition)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("King not found on board"));
    }

}
