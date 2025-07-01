package dev.markconley.chess.engine.move.handler;

import dev.markconley.chess.engine.core.Color;
import dev.markconley.chess.engine.core.Position;
import dev.markconley.chess.engine.move.Move;
import dev.markconley.chess.engine.move.MoveFactory;
import dev.markconley.chess.engine.pieces.King;
import dev.markconley.chess.engine.pieces.Piece;
import dev.markconley.chess.engine.pieces.Rook;
import dev.markconley.chess.engine.state.BoardState;
import dev.markconley.chess.engine.state.CastlingRights;

public class CastlingMoveHandler implements SimpleMoveHandler {
	
	private static final CastlingMoveHandler INSTANCE = new CastlingMoveHandler();

	private CastlingMoveHandler() { }

	public static CastlingMoveHandler getInstance() {
		return INSTANCE;
	}

	@Override
	public boolean canHandle(Piece piece, Position from, Position to) {
		return piece instanceof King && Math.abs(from.getCol() - to.getCol()) == 2;
	}
	
    @Override
	public Move handle(BoardState state, Piece piece, Position from, Position to) {
		if (!isValidCastle(state, piece.getColor(), from, to)) {
			return null;
		}
		return MoveFactory.castle(from, to, piece);
	}

    private boolean isValidCastle(BoardState state, Color color, Position from, Position to) {
        
	    boolean hasCastlingRights =  hasCastlingRights(state, color, to);
	    boolean isPathClear = isPathClear(state, color, from, to);
	    boolean isKingNotInCheck = isKingNotInCheck(state, color, from);
	    boolean areCastlingSquaresSafe = areCastlingSquaresSafe(state, color, from, to);
	    
	    return hasCastlingRights && isPathClear && isKingNotInCheck && areCastlingSquaresSafe; 
    }

	private boolean hasCastlingRights(BoardState state, Color color, Position to) {
		CastlingRights rights = state.getCastlingRights();

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

	private boolean isPathClear(BoardState state, Color color, Position from, Position to) {
		int row = color == Color.WHITE ? 0 : 7;
		int startCol = 4;
		int endCol = to.getCol();
		int step = (endCol - startCol) / Math.abs(endCol - startCol);

		for (int col = startCol + step; col != endCol; col += step) {
			if (state.getBoard().getPieceAt(Position.of(row, col)) != null) {
				return false;
			}
		}
		return true;
	}
    
    private boolean isKingNotInCheck(BoardState state, Color color, Position from) {
        return !state.isSquareAttacked(from, color);
    }

	private boolean areCastlingSquaresSafe(BoardState state, Color color, Position from, Position to) {
		int row = from.getRow();
		int step = (to.getCol() - from.getCol()) / 2;

		Position intermediate = Position.of(row, from.getCol() + step);
		Position destination = to;

		return !state.isSquareAttacked(intermediate, color)
				&& !state.isSquareAttacked(destination, color);
	}
    
	public void updateCastlingRightsOnMove(BoardState state, Piece piece, Position from) {
		CastlingRights rights = state.getCastlingRights();

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

	public void updateCastlingRightsOnRookCapture(BoardState state, Piece capturedPiece, Position to) {
		if (!(capturedPiece instanceof Rook)) {
			return;
		}

		CastlingRights rights = state.getCastlingRights();
		int row = capturedPiece.getColor() == Color.WHITE ? 0 : 7;

		if (to.equals(Position.of(row, 0))) {
			rights.disableQueenside(capturedPiece.getColor());
		} else if (to.equals(Position.of(row, 7))) {
			rights.disableKingside(capturedPiece.getColor());
		}
	}
}
