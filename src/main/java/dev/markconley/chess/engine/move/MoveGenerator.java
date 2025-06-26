package dev.markconley.chess.engine.move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import dev.markconley.chess.engine.board.Board;
import dev.markconley.chess.engine.core.Color;
import dev.markconley.chess.engine.core.Position;
import dev.markconley.chess.engine.move.service.SpecialMoveService;
import dev.markconley.chess.engine.pieces.Bishop;
import dev.markconley.chess.engine.pieces.Direction;
import dev.markconley.chess.engine.pieces.King;
import dev.markconley.chess.engine.pieces.Knight;
import dev.markconley.chess.engine.pieces.Pawn;
import dev.markconley.chess.engine.pieces.Piece;
import dev.markconley.chess.engine.pieces.Queen;
import dev.markconley.chess.engine.pieces.Rook;

public class MoveGenerator {
	
//	private final EnPassantStrategy enPassantStrategy = new DefaultEnPassantStrategy();

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


	public static List<Move> generateJumpingMoves(Board board, Piece piece) {
		List<Move> moves = new ArrayList<>();

		for (Direction direction : Direction.KNIGHT_DIRECTIONS) {
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

	    Position from = piece.getPosition();
	    Color color = piece.getColor();
	    int row = from.getRow();
	    int col = from.getCol();

	    int direction = color == Color.WHITE ? 1 : -1;
	    int startRow = color == Color.WHITE ? 1 : 6;
	    Color enemyColor = color.opposite();

		// Forward one step
		Position oneStep = Position.of(row + direction, col);
		if (Position.isValid(oneStep) && board.getPieceAt(oneStep) == null) {
			addMoveOrPromotion(moves, from, oneStep, piece, null);

			// Forward two steps from starting row
			if (row == startRow) {
				Position twoStep = Position.of(row + 2 * direction, col);
				if (board.getPieceAt(twoStep) == null) {
					moves.add(MoveFactory.normal(from, twoStep, piece));
				}
			}
		}

	    // Diagonal captures (including promotion)
	    Position[] captures = {
	        Position.of(row + direction, col - 1),
	        Position.of(row + direction, col + 1)
	    };

	    for (Position target : captures) {
	        if (Position.isValid(target)) {
	            Piece targetPiece = board.getPieceAt(target);
	            if (targetPiece != null && targetPiece.getColor() == enemyColor) {
	                addMoveOrPromotion(moves, from, target, piece, targetPiece);
	            }
	        }
	    }

	    // En passant
	    Position enPassantTarget = board.getEnPassantTarget();
	    if (enPassantTarget != null && Math.abs(enPassantTarget.getCol() - col) == 1 && enPassantTarget.getRow() == row + direction) {
	        Piece capturedPawn = board.getPieceAt(Position.of(row, enPassantTarget.getCol()));
	        if (capturedPawn != null && capturedPawn.getColor() == enemyColor && capturedPawn instanceof Pawn) {
	            moves.add(MoveFactory.enPassant(from, enPassantTarget, piece, capturedPawn));
	        }
	    }

	    return moves;
	}

	private static void addMoveOrPromotion(List<Move> moves, Position from, Position to, Piece pawn, Piece captured) {
	    int promotionRow = (pawn.getColor() == Color.WHITE) ? 7 : 0;

	    if (to.getRow() == promotionRow) {
	    	List<Piece> promotionOptions = List.of(
		            new Queen(pawn.getColor()),
		            new Rook(pawn.getColor()),
		            new Bishop(pawn.getColor()),
		            new Knight(pawn.getColor()));

			for (Piece promoted : promotionOptions) {
				moves.add(MoveFactory.promotion(from, to, pawn, promoted));
			}
	    } else if (captured == null) {
	        moves.add(MoveFactory.normal(from, to, pawn));
	    } else {
	        moves.add(MoveFactory.capture(from, to, pawn, captured));
	    }
	}

	public static List<Move> generateKingMoves(Board board, Piece king, SpecialMoveService specialMoveService) {
		List<Move> moves = new ArrayList<>();
		
		// Normal moves
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
		
		// Castling moves
	    Position from = king.getPosition();

	    // Kingside and queenside castling destinations
	    int row = from.getRow();
	    List<Position> castleDestinations = List.of(
	        Position.of(row, 6),
	        Position.of(row, 2) 
	    );

	    for (Position to : castleDestinations) {
	        if (specialMoveService.getCastlingMoveHandler().canHandle(king, from, to)) {
	            Move castleMove = specialMoveService.getCastlingMoveHandler().handle(board, king, from, to);
	            if (castleMove != null) {
	                moves.add(castleMove);
	            }
	        }
	    }
	    
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
		    Knight.class,  (board, piece) -> generateJumpingMoves(board, piece),
		    Pawn.class,    MoveGenerator::generatePawnMoves
		);

	public static List<Move> generateMoves(Board board, Piece piece, SpecialMoveService specialMoveService) {
	    if (piece instanceof King) {
	        return generateKingMoves(board, piece, specialMoveService);
	    }

	    return MOVE_GENERATORS
	        .getOrDefault(piece.getClass(), (b, p) -> List.of())
	        .apply(board, piece);
	}
	// @formatter:on

}
