package com.optimism.chess.engine.move;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.optimism.chess.engine.board.Board;
import com.optimism.chess.engine.core.Color;
import com.optimism.chess.engine.core.Position;
import com.optimism.chess.engine.pieces.Bishop;
import com.optimism.chess.engine.pieces.Direction;
import com.optimism.chess.engine.pieces.King;
import com.optimism.chess.engine.pieces.Knight;
import com.optimism.chess.engine.pieces.Pawn;
import com.optimism.chess.engine.pieces.Piece;
import com.optimism.chess.engine.pieces.Queen;
import com.optimism.chess.engine.pieces.Rook;

public class AttackMapGenerator {

	public static List<Position> generateAttackSquares(Board board, Piece piece) {
		List<Position> attacked = new ArrayList<>();

		if (piece instanceof Pawn) {
			attacked.addAll(generatePawnAttacks(piece));
		} else if (piece instanceof King) {
			attacked.addAll(generateKingAttacks(piece));
		} else if (piece instanceof Knight) {
			for (Direction direction : Direction.KNIGHT_DIRECTIONS) {
				int row = piece.getPosition().getRow() + direction.rowOffset();
				int col = piece.getPosition().getCol() + direction.colOffset();
				if (Position.isValid(row, col)) {
					attacked.add(Position.of(row, col));
				}
			}
		} else if (piece instanceof Rook) {
			attacked.addAll(traceDirections(board, piece, Direction.ROOK_DIRECTIONS));
		} else if (piece instanceof Bishop) {
			attacked.addAll(traceDirections(board, piece, Direction.BISHOP_DIRECTIONS));
		} else if (piece instanceof Queen) {
			attacked.addAll(traceDirections(board, piece, Direction.ROOK_DIRECTIONS));
			attacked.addAll(traceDirections(board, piece, Direction.BISHOP_DIRECTIONS));
		}

		return attacked;
	}

    public static Set<Position> generateAttackSquares(Board board, Color color) {
        return board.getActivePieces(color)
                    .stream()
                    .flatMap(p -> generateAttackSquares(board, p).stream())
                    .collect(Collectors.toSet());
    }

	private static List<Position> traceDirections(Board board, Piece piece, Direction[] directions) {
		List<Position> attacked = new ArrayList<>();
		for (Direction direction : directions) {
			int row = piece.getPosition().getRow();
			int col = piece.getPosition().getCol();
			while (true) {
				row += direction.rowOffset();
				col += direction.colOffset();
				if (!Position.isValid(row, col)) {
					break;
				}

				Position pos = new Position(row, col);
				attacked.add(pos);

				if (board.getPieceAt(pos) != null) {
					break;
				}
			}
		}
		return attacked;
	}

	private static List<Position> generatePawnAttacks(Piece pawn) {
		List<Position> attacked = new ArrayList<>();
		int row = pawn.getPosition().getRow();
		int col = pawn.getPosition().getCol();
		int direction = (pawn.getColor() == Color.WHITE) ? 1 : -1;

		Position left = new Position(row + direction, col - 1);
		Position right = new Position(row + direction, col + 1);

		if (Position.isValid(left)) {
			attacked.add(left);
		}
		if (Position.isValid(right)) {
			attacked.add(right);
		}

		return attacked;
	}

	private static List<Position> generateKingAttacks(Piece king) {
		List<Position> attacked = new ArrayList<>();
		for (Direction direction : Direction.KING_DIRECTIONS) {
			int row = king.getPosition().getRow() + direction.rowOffset();
			int col = king.getPosition().getCol() + direction.colOffset();
			if (Position.isValid(row, col)) {
				attacked.add(Position.of(row, col));
			}
		}
		
		return attacked;
	}

}
