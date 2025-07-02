package dev.markconley.chess.engine.rules.draw;

import java.util.List;
import java.util.Optional;

import dev.markconley.chess.engine.board.Board;
import dev.markconley.chess.engine.move.Move;
import dev.markconley.chess.engine.pieces.Bishop;
import dev.markconley.chess.engine.pieces.King;
import dev.markconley.chess.engine.pieces.Knight;
import dev.markconley.chess.engine.pieces.Piece;
import dev.markconley.chess.engine.state.BoardState;
import dev.markconley.chess.game.GameStatus;

public class InsufficientMaterialRule implements DrawRule {
	
	@Override
	public Optional<GameStatus> check(Board board, BoardState state, List<Move> moveHistory, List<BoardState> history) {
		List<Piece> pieces = board.getAllActivePieces();

		boolean onlyKings = pieces.stream().allMatch(p -> p instanceof King);
		if (onlyKings) {
			return Optional.of(GameStatus.INSUFFICIENT_MATERIAL);
		}

		long minorPieces = pieces.stream().filter(p -> p instanceof Bishop || p instanceof Knight).count();

		if (pieces.size() == 3 && minorPieces == 1) {
			return Optional.of(GameStatus.INSUFFICIENT_MATERIAL);
		}

		return Optional.empty();
	}
	
}
