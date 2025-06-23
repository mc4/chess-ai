package dev.markconley.chess.engine.move.handler;

import dev.markconley.chess.engine.board.Board;
import dev.markconley.chess.engine.core.Color;
import dev.markconley.chess.engine.core.Position;
import dev.markconley.chess.engine.move.Move;
import dev.markconley.chess.engine.move.MoveFactory;
import dev.markconley.chess.engine.pieces.King;
import dev.markconley.chess.engine.pieces.Piece;
import dev.markconley.chess.engine.pieces.Rook;
import dev.markconley.chess.engine.state.CastlingRights;

public class CastlingMoveHandler implements SpecialMoveHandler {

	@Override
	public boolean canHandle(Piece piece, Position from, Position to) {
		return piece instanceof King && Math.abs(from.getCol() - to.getCol()) == 2;
	}
	
    @Override
    public Move handle(Board board, Piece piece, Position from, Position to) {
        if (!isValidCastle(board, piece.getColor(), from, to)) {
        	return null;
        }

//        disableCastlingRightsAfterCastling(board, piece.getColor());
//        moveRookDuringCastling(board, from, to);
        return MoveFactory.castle(from, to, piece);
    }

    private boolean isValidCastle(Board board, Color color, Position from, Position to) {
        
    boolean hasCastlingRights =  hasCastlingRights(board, color, to);
    boolean isPathClear = isPathClear(board, color, from, to);
    boolean isKingNotInCheck = isKingNotInCheck(board, color, from);
    boolean areCastlingSquaresSafe = areCastlingSquaresSafe(board, color, from, to);
    
    return hasCastlingRights && isPathClear && isKingNotInCheck && areCastlingSquaresSafe; 
    }

	private boolean hasCastlingRights(Board board, Color color, Position to) {
		CastlingRights rights = board.getCastlingRights();

		if (color == Color.WHITE) {
			if (to.equals(Position.of(0, 6))) {
				return rights.whiteCanCastleKingside();
			}
			if (to.equals(Position.of(0, 2))) {
				return rights.whiteCanCastleQueenside();
			}
		} else {
			if (to.equals(Position.of(7, 6))) {
				return rights.blackCanCastleKingside();
			}
			if (to.equals(Position.of(7, 2))) {
				return rights.blackCanCastleQueenside();
			}
		}
		return false;
	}

	private boolean isPathClear(Board board, Color color, Position from, Position to) {
		int row = color == Color.WHITE ? 0 : 7;
		int startCol = 4;
		int endCol = to.getCol();
		int step = (endCol - startCol) / Math.abs(endCol - startCol);

		for (int col = startCol + step; col != endCol; col += step) {
			if (board.getPieceAt(Position.of(row, col)) != null) {
				return false;
			}
		}
		return true;
	}
    
    private boolean isKingNotInCheck(Board board, Color color, Position from) {
        return !board.isSquareAttacked(from, color);
    }

	private boolean areCastlingSquaresSafe(Board board, Color color, Position from, Position to) {
		int row = from.getRow();
		int step = (to.getCol() - from.getCol()) / 2;

		Position intermediate = Position.of(row, from.getCol() + step);
		Position destination = to;

		return !board.isSquareAttacked(intermediate, color)
				&& !board.isSquareAttacked(destination, color);
	}
    
	public void updateCastlingRightsOnMove(Board board, Piece piece, Position from) {
		CastlingRights rights = board.getCastlingRights();

		if (piece instanceof Rook) {
			int row = piece.getColor() == Color.WHITE ? 0 : 7;
			if (from.equals(Position.of(row, 0))) {
				rights.disableQueenside(piece.getColor());
			} else if (from.equals(Position.of(row, 7))) {
				rights.disableKingside(piece.getColor());
			}
		} else if (piece instanceof King) {
			rights.disableAll(piece.getColor());
		}
	}

	public void updateCastlingRightsOnRookCapture(Board board, Piece capturedPiece, Position to) {
		if (!(capturedPiece instanceof Rook)) {
			return;
		}

		CastlingRights rights = board.getCastlingRights();
		int row = capturedPiece.getColor() == Color.WHITE ? 0 : 7;

		if (to.equals(Position.of(row, 0))) {
			rights.disableQueenside(capturedPiece.getColor());
		} else if (to.equals(Position.of(row, 7))) {
			rights.disableKingside(capturedPiece.getColor());
		}
	}
}
