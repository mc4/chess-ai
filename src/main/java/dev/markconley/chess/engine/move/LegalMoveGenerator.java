package dev.markconley.chess.engine.move;

import java.util.List;

import dev.markconley.chess.engine.board.Board;
import dev.markconley.chess.engine.core.Color;
import dev.markconley.chess.engine.pieces.Piece;
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

    public static boolean isLegalMove(BoardState state, Move move, Color color) {
        Board board = state.getBoard();
        Piece piece = board.getPieceAt(move.from());

        // Basic piece presence and ownership check
        if (piece == null || piece.getColor() != color) {
            return false;
        }

        // Step 1: Generate pseudo-legal moves ONLY for this piece
        List<Move> pseudoLegalMoves = MoveGenerator.generateMoves(state, piece);

        // Step 2: Check if desired move is among pseudo-legal ones
        boolean isPseudoLegal = pseudoLegalMoves.stream().anyMatch(m -> m.equals(move));
        if (!isPseudoLegal) {
            return false;
        }

        // Step 3: Copy the board state to simulate the move
        BoardState simulated = state.copy();

        // Step 4: Apply the move safely on the simulated copy
        new MoveExecutor().applyMove(simulated, move);

        // Step 5: Confirm the player's king is not in check after the move
        return !GameStateEvaluator.isInCheck(simulated, color);
    }

}
