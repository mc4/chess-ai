package com.optimism.chess.engine.move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

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

public class MoveGenerator {

	public static List<Move> generateSlidingMoves(Board board, Piece piece, Direction[] directions) {
	    return Arrays.stream(directions)
	            .flatMap(direction -> slideInDirection(board, piece, direction).stream())
	            .toList();
	}

	private static List<Move> slideInDirection(Board board, Piece piece, Direction direction) {
	    List<Move> moves = new ArrayList<>();
	    int dRow = direction.rowOffset();
	    int dCol = direction.colOffset();

	    int row = piece.getPosition().getRow();
	    int col = piece.getPosition().getCol();

	    while (Position.isValid(row + dRow, col + dCol)) {
	        row += dRow;
	        col += dCol;

	        Position target = new Position(row, col);
	        Piece targetPiece = board.getPieceAt(target);

	        if (targetPiece == null) {
	        	moves.add(MoveFactory.normal(piece.getPosition(), target, piece));
	        } else {
	            if (targetPiece.getColor() != piece.getColor()) {
	            	moves.add(MoveFactory.capture(piece.getPosition(), target, piece, targetPiece));
	            }
	            break;
	        }
	    }

	    return moves;
	}


	public static List<Move> generateJumpingMoves(Board board, Piece piece, Direction[] directions) {
		List<Move> moves = new ArrayList<>();

		for (Direction direction : directions) {
			int row = piece.getPosition().getRow() + direction.rowOffset();
			int col = piece.getPosition().getCol() + direction.colOffset();

			if (Position.isValid(row, col)) {
				Position target = new Position(row, col);
				Piece targetPiece = board.getPieceAt(target);
				
				if (targetPiece == null) {
					moves.add(MoveFactory.normal(piece.getPosition(), target, piece));
				} else if (targetPiece.getColor() != piece.getColor()) {
					moves.add(MoveFactory.capture(piece.getPosition(), target, piece, targetPiece));
				}
			}
		}

		return moves;
	}

	public static List<Move> generatePawnMoves(Board board, Piece piece) {
		List<Move> moves = new ArrayList<>();
		Color currentTurn = board.getCurrentTurn();

		if (currentTurn == Color.WHITE) {
			generatePawnMovesForWhite(board, piece, moves);
		} else {
			generatePawnMovesForBlack(board, piece, moves);
		}

		return moves;
	}

	private static void generatePawnMovesForWhite(Board board, Piece piece, List<Move> moves) {
		Position from = piece.getPosition();
		int row = from.getRow();
		int col = from.getCol();

		Position oneStep = new Position(row + 1, col);
		if (Position.isValid(oneStep) && board.getPieceAt(oneStep) == null) {
			moves.add(MoveFactory.normal(from, oneStep, piece));

			Position twoStep = new Position(row + 2, col);
			if (row == 1 && board.getPieceAt(twoStep) == null) {
				moves.add(MoveFactory.normal(from, twoStep, piece));
			}
		}

		Position[] captures = { 
				new Position(row + 1, col - 1), 
				new Position(row + 1, col + 1) 
		};

		for (Position target : captures) {
			if (Position.isValid(target)) {
				Piece targetPiece = board.getPieceAt(target);
				if (target != null && targetPiece.getColor() == Color.BLACK) {
					moves.add(MoveFactory.capture(from, target, piece, targetPiece));
				}
			}
		}

		// TODO: Handle promotion and en passant
	}

	private static void generatePawnMovesForBlack(Board board, Piece piece, List<Move> moves) {
	    Position from = piece.getPosition();
	    int row = from.getRow();
	    int col = from.getCol();

	    Position oneStep = new Position(row - 1, col);
		if (Position.isValid(oneStep) && board.getPieceAt(oneStep) == null) {
			moves.add(MoveFactory.normal(from, oneStep, piece));

			Position twoStep = new Position(row - 2, col);
			if (row == 6 && board.getPieceAt(twoStep) == null) {
				moves.add(MoveFactory.normal(from, twoStep, piece));
			}
		}

	    Position[] captures = {
	        new Position(row - 1, col - 1),
	        new Position(row - 1, col + 1)
	    };

	    for (Position target : captures) {
			if (Position.isValid(target)) {
				Piece targetPiece = board.getPieceAt(target);
				if (target != null && targetPiece.getColor() == Color.WHITE) {
					moves.add(MoveFactory.capture(from, target, piece, targetPiece));
				}
			}
	    }

	    // TODO: Handle promotion and en passant
	}

	public static List<Move> generateKingMoves(Board board, Piece king) {
		List<Move> moves = new ArrayList<>();
		
		for (Direction direction : Direction.KING_DIRECTIONS) {
			int row = king.getPosition().getRow() + direction.rowOffset();
			int col = king.getPosition().getCol() + direction.colOffset();
			
			if (Position.isValid(row, col)) {
				Position target = new Position(row, col);
				Piece targetPiece = board.getPieceAt(target);
				
				if (targetPiece == null) {
					moves.add(MoveFactory.normal(king.getPosition(), target, king));
				} else if (targetPiece.getColor() != king.getColor()) {
					moves.add(MoveFactory.capture(king.getPosition(), target, king, targetPiece));
				}
			}
		}
		
		 // TODO: Handle castling
		
		return moves;
	}
	
	// @formatter:off
	private static final Map<Class<?>, BiFunction<Board, Piece, List<Move>>> MOVE_GENERATORS = Map.of(
		    Rook.class,    (board, piece) -> generateSlidingMoves(board, piece, Direction.ROOK_DIRECTIONS),
		    Bishop.class,  (board, piece) -> generateSlidingMoves(board, piece, Direction.BISHOP_DIRECTIONS),
		    Queen.class,   (board, piece) -> {
		        List<Move> moves = new ArrayList<>(generateSlidingMoves(board, piece, Direction.ROOK_DIRECTIONS));
		        moves.addAll(generateSlidingMoves(board, piece, Direction.BISHOP_DIRECTIONS));
		        return moves;
		    },
		    Knight.class,  (board, piece) -> generateJumpingMoves(board, piece, Direction.KNIGHT_DIRECTIONS),
		    Pawn.class,    MoveGenerator::generatePawnMoves,
		    King.class,    MoveGenerator::generateKingMoves
		);

	public static List<Move> generateMoves(Board board, Piece piece) {
	    return MOVE_GENERATORS
	        .getOrDefault(piece.getClass(), (b, p) -> List.of())
	        .apply(board, piece);
	}
	// @formatter:on

}
