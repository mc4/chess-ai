package com.optimism.chess.engine.board;

import java.util.ArrayList;
import java.util.List;

import com.optimism.chess.engine.core.Color;
import com.optimism.chess.engine.core.Copyable;
import com.optimism.chess.engine.core.Position;
import com.optimism.chess.engine.move.Move;
import com.optimism.chess.engine.move.MoveFactory;
import com.optimism.chess.engine.pieces.Bishop;
import com.optimism.chess.engine.pieces.King;
import com.optimism.chess.engine.pieces.Knight;
import com.optimism.chess.engine.pieces.Pawn;
import com.optimism.chess.engine.pieces.Piece;
import com.optimism.chess.engine.pieces.Queen;
import com.optimism.chess.engine.pieces.Rook;

public class Board implements Copyable<Board> {

	private static final int BOARD_SIZE = 8;

	private Piece[][] board;
	private Color currentTurn;
	private List<Move> moveHistory;

	public Board() {
		board = new Piece[BOARD_SIZE][BOARD_SIZE];
		initializeBoard();
		currentTurn = Color.WHITE;
	}

	private void initializeBoard() {
		board = new Piece[8][8];
		board[1][0] = new Pawn(Color.WHITE);
		board[1][1] = new Pawn(Color.WHITE);
		board[1][2] = new Pawn(Color.WHITE);
		board[1][3] = new Pawn(Color.WHITE);
		board[1][4] = new Pawn(Color.WHITE);
		board[1][5] = new Pawn(Color.WHITE);
		board[1][6] = new Pawn(Color.WHITE);
		board[1][7] = new Pawn(Color.WHITE);

		board[0][0] = new Rook(Color.WHITE);
		board[0][1] = new Knight(Color.WHITE);
		board[0][2] = new Bishop(Color.WHITE);
		board[0][3] = new Queen(Color.WHITE);
		board[0][4] = new King(Color.WHITE);
		board[0][5] = new Bishop(Color.WHITE);
		board[0][6] = new Knight(Color.WHITE);
		board[0][7] = new Rook(Color.WHITE);

		board[6][0] = new Pawn(Color.BLACK);
		board[6][1] = new Pawn(Color.BLACK);
		board[6][2] = new Pawn(Color.BLACK);
		board[6][3] = new Pawn(Color.BLACK);
		board[6][4] = new Pawn(Color.BLACK);
		board[6][5] = new Pawn(Color.BLACK);
		board[6][6] = new Pawn(Color.BLACK);
		board[6][7] = new Pawn(Color.BLACK);

		board[7][0] = new Rook(Color.BLACK);
		board[7][1] = new Knight(Color.BLACK);
		board[7][2] = new Bishop(Color.BLACK);
		board[7][3] = new Queen(Color.BLACK);
		board[7][4] = new King(Color.BLACK);
		board[7][5] = new Bishop(Color.BLACK);
		board[7][6] = new Knight(Color.BLACK);
		board[7][7] = new Rook(Color.BLACK);
	}

	// === Accessors ===
	public Piece getPieceAt(Position position) {
		return board[position.getRow()][position.getCol()];
	}

	public void setPieceAt(Position position, Piece piece) {
		board[position.getRow()][position.getCol()] = piece;
	}

	public boolean isSquareEmpty(Position position) {
		return getPieceAt(position) == null;
	}

	public boolean isWithinBounds(Position position) {
		int row = position.getRow();
		int col = position.getCol();
		return row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE;
	}

	public Color getCurrentTurn() {
		return currentTurn;
	}

	public void switchTurn() {
		currentTurn = currentTurn.opposite();
	}

	public List<Piece> getPiecesOfColor(Color color) {
		List<Piece> pieces = new ArrayList<>();
		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int col = 0; col < BOARD_SIZE; col++) {
				Piece piece = getPieceAt(new Position(row, col));
				if (piece != null && piece.getColor() == color) {
					pieces.add(piece);
				}
			}
		}
		return pieces;
	}
	
	// === Move Logic ===
	public boolean makeMove(Position from, Position to) {
	    Piece movedPiece = getPieceAt(from);
	    if (movedPiece == null || movedPiece.getColor() != currentTurn) {
	        return false;
	    }

	    Piece targetPiece = getPieceAt(to);
	    Move move;

	    // Detect castling
	    if (movedPiece instanceof King && Math.abs(from.getCol() - to.getCol()) == 2) {
	        move = MoveFactory.castle(from, to, movedPiece);
	        // TODO: Move the rook as part of castling
	    } else if (movedPiece instanceof Pawn && targetPiece == null && from.getCol() != to.getCol()) { // Detect en passant
	        Position capturedPawnPos = new Position(from.getRow(), to.getCol());
	        Piece capturedPawn = getPieceAt(capturedPawnPos);
	        move = MoveFactory.enPassant(from, to, movedPiece, capturedPawn);
	        setPieceAt(capturedPawnPos, null); // Remove captured pawn
	    } else if (movedPiece instanceof Pawn) { // Detect promotion
	        int lastRank = (movedPiece.getColor() == Color.WHITE) ? 7 : 0;
	        if (to.getRow() == lastRank) {
	            Piece promotedPiece = new Queen(movedPiece.getColor());
	            promotedPiece.setPosition(to);
	            setPieceAt(to, promotedPiece);
	            move = MoveFactory.promotion(from, to, movedPiece, promotedPiece);
	        } else if (targetPiece == null) { // Regular or capturing pawn move
	            move = MoveFactory.normal(from, to, movedPiece);
	        } else {
	            move = MoveFactory.capture(from, to, movedPiece, targetPiece);
	        }
	    }
	    // All other pieces
	    else {
	        if (targetPiece == null) {
	            move = MoveFactory.normal(from, to, movedPiece);
	        } else {
	            move = MoveFactory.capture(from, to, movedPiece, targetPiece);
	        }
	    }

	    // Apply move
	    if (!move.isPromotion()) {
	        setPieceAt(to, movedPiece);
	        movedPiece.setPosition(to);
	    }

	    setPieceAt(from, null);
	    moveHistory.add(move);
	    switchTurn();
	    return true;
	}



	public List<Piece> getActivePieces(Color color) {
		List<Piece> activePieces = new ArrayList<>();
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				Piece piece = board[row][col];
				if (piece != null && piece.getColor() == color) {
					activePieces.add(piece);
				}
			}
		}
		return activePieces;
	}

	public List<Piece> getAllActivePieces() {
		List<Piece> activePieces = new ArrayList<>();
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				Piece piece = board[row][col];
				if (piece != null) {
					activePieces.add(piece);
				}
			}
		}
		return activePieces;
	}
	
	@Override
	public Board copy() {
		Board newBoard = new Board();
		newBoard.currentTurn = this.currentTurn;
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				Piece piece = this.board[row][col];
				if (piece != null) {
					newBoard.board[row][col] = piece.copy();
				}
			}
		}
		return newBoard;
	}

	public void printBoard() {
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				Piece piece = board[row][col];
				System.out.print((piece == null ? "." : piece.toString()) + " ");
			}
			System.out.println();
		}
	}
}
