package com.optimism.chess.engine.move.handler;

import com.optimism.chess.engine.board.Board;
import com.optimism.chess.engine.core.Color;
import com.optimism.chess.engine.core.Position;
import com.optimism.chess.engine.move.Move;
import com.optimism.chess.engine.move.MoveFactory;
import com.optimism.chess.engine.pieces.King;
import com.optimism.chess.engine.pieces.Piece;
import com.optimism.chess.engine.pieces.Rook;
import com.optimism.chess.engine.state.CastlingRights;

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

        disableCastlingRightsAfterCastling(board, piece.getColor());
        moveRookDuringCastling(board, from, to);
        return MoveFactory.castle(from, to, piece);
    }

    private boolean isValidCastle(Board board, Color color, Position from, Position to) {
        CastlingRights rights = board.getCastlingRights();

        // Check castling rights	
        if (color == Color.WHITE) {
            if (to.equals(Position.of(0, 6)) && !rights.whiteCanCastleKingside()) {
            	return false;
            }
            if (to.equals(Position.of(0, 2)) && !rights.whiteCanCastleQueenside()) {
            	return false;
            }
        } else {
            if (to.equals(Position.of(7, 6)) && !rights.blackCanCastleKingside()) {
            	return false;
            }
            if (to.equals(Position.of(7, 2)) && !rights.blackCanCastleQueenside()) {
            	return false;
            }
        }

        int row = color == Color.WHITE ? 0 : 7;

        // Define squares king moves through (and ends on)
        int startCol = 4;
        int endCol = to.getCol();
        int step = (endCol - startCol) / Math.abs(endCol - startCol); // +1 or -1

        // Check path clearance between king and rook
        for (int col = startCol + step; col != (endCol + step); col += step) {
            Position pos = Position.of(row, col);
            if (board.getPieceAt(pos) != null) {
            	return false;  // Path not clear
            }
        }

        // Check king not currently in check
        if (board.isSquareAttacked(Position.of(row, startCol), color.opposite())) {
        	return false;
        }

        // Check king does not pass through or land on attacked squares
        // King moves two steps; check both intermediate and target squares
        for (int col = startCol + step; col != endCol + step; col += step) {
            Position pos = Position.of(row, col);
            if (board.isSquareAttacked(pos, color.opposite())) {
            	return false;
            }
        }

        return true;
    }

    
    private void disableCastlingRightsAfterCastling(Board board, Color color) {
    	board.getCastlingRights().disableAll(color);;
    }

	private void moveRookDuringCastling(Board board, Position from, Position to) {
		Piece rook = board.getPieceAt(from);
		board.setPieceAt(to, rook);
		if (rook != null) {
			rook.setPosition(to);
		}
		board.setPieceAt(from, null);
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
