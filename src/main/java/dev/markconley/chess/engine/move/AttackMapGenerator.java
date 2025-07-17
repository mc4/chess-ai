package dev.markconley.chess.engine.move;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import dev.markconley.chess.engine.board.Board;
import dev.markconley.chess.engine.core.Color;
import dev.markconley.chess.engine.core.Position;
import dev.markconley.chess.engine.pieces.Bishop;
import dev.markconley.chess.engine.pieces.Direction;
import dev.markconley.chess.engine.pieces.King;
import dev.markconley.chess.engine.pieces.Knight;
import dev.markconley.chess.engine.pieces.Pawn;
import dev.markconley.chess.engine.pieces.Piece;
import dev.markconley.chess.engine.pieces.Queen;
import dev.markconley.chess.engine.pieces.Rook;

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

		int forwardRow = row + direction;

		if (Position.isValid(forwardRow, col - 1)) {
			attacked.add(Position.of(forwardRow, col - 1));
		}
		if (Position.isValid(forwardRow, col + 1)) {
			attacked.add(Position.of(forwardRow, col + 1));
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
