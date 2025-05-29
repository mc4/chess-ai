package com.optimism.chess.engine.board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

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
	
	private boolean whiteCanCastleKingside = true;
	private boolean whiteCanCastleQueenside = true;
	private boolean blackCanCastleKingside = true;
	private boolean blackCanCastleQueenside = true;
	
	public Board() {
		this.board = new Piece[BOARD_SIZE][BOARD_SIZE];
		setupInitialPosition();
		this.currentTurn = Color.WHITE;
		this.moveHistory = new ArrayList<>();
	}
	
	private Board(boolean skipSetup) {
		this.board = new Piece[BOARD_SIZE][BOARD_SIZE];
		this.currentTurn = Color.WHITE;
		this.moveHistory = new ArrayList<>();

		if (!skipSetup) {
			setupInitialPosition();
		}
	}
	
	public static Board emptyBoard() {
	    return new Board(true); 
	}
	
	public Board place(String square, Piece piece) {
	    Position pos = new Position(square);
	    piece.setPosition(pos);
	    setPieceAt(pos, piece);
	    return this;
	}

	public Board place(Position pos, Piece piece) {
	    piece.setPosition(pos);
	    setPieceAt(pos, piece);
	    return this;
	}

	private void setupInitialPosition() {
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

	public Color getCurrentTurn() {
		return currentTurn;
	}

	public void switchTurn() {
		currentTurn = currentTurn.opposite();
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
	        if (!canCastle(from, to, movedPiece.getColor())) {
	            return false;
	        }

	        move = MoveFactory.castle(from, to, movedPiece);
	        moveRookForCastling(from, to); // TODO: implement

	        if (movedPiece.getColor() == Color.WHITE) {
	            whiteCanCastleKingside = false;
	            whiteCanCastleQueenside = false;
	        } else {
	            blackCanCastleKingside = false;
	            blackCanCastleQueenside = false;
	        }
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
	    } else { // All other pieces
	        if (targetPiece == null) {
	            move = MoveFactory.normal(from, to, movedPiece);
	        } else {
	            move = MoveFactory.capture(from, to, movedPiece, targetPiece);
	        }
	    }
	    
		// Update castling rights
	    if (movedPiece instanceof King) {
	        if (movedPiece.getColor() == Color.WHITE) {
	            whiteCanCastleKingside = false;
	            whiteCanCastleQueenside = false;
	        } else {
	            blackCanCastleKingside = false;
	            blackCanCastleQueenside = false;
	        }
		} else if (movedPiece instanceof Rook) {
			int row = movedPiece.getColor() == Color.WHITE ? 0 : 7;
			if (from.equals(new Position(row, 0))) {
				if (movedPiece.getColor() == Color.WHITE) {
					whiteCanCastleQueenside = false;
				} else {
					blackCanCastleQueenside = false;
				}
			} else if (from.equals(new Position(row, 7))) {
				if (movedPiece.getColor() == Color.WHITE) {
					whiteCanCastleKingside = false;
				} else {
					blackCanCastleKingside = false;
				}
			}
		}
		
	    // Also, if a rook is captured, update castling rights
		if (targetPiece instanceof Rook) {
			int row = targetPiece.getColor() == Color.WHITE ? 0 : 7;
			if (to.equals(new Position(row, 0))) {
				if (targetPiece.getColor() == Color.WHITE) {
					whiteCanCastleQueenside = false;
				} else {
					blackCanCastleQueenside = false;
				}
			} else if (to.equals(new Position(row, 7))) {
				if (targetPiece.getColor() == Color.WHITE) {
					whiteCanCastleKingside = false;
				} else {
					blackCanCastleKingside = false;
				}
			}
		}

		if (targetPiece instanceof Rook) {
			int row = targetPiece.getColor() == Color.WHITE ? 0 : 7;
			if (to.equals(new Position(row, 0))) {
				if (targetPiece.getColor() == Color.WHITE) {
					whiteCanCastleQueenside = false;
				} else {
					blackCanCastleQueenside = false;
				}
			} else if (to.equals(new Position(row, 7))) {
				if (targetPiece.getColor() == Color.WHITE) {
					whiteCanCastleKingside = false;
				} else {
					blackCanCastleKingside = false;
				}
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
	
	private boolean canCastle(Position kingFrom, Position kingTo, Color color) {
	    int row = color == Color.WHITE ? 0 : 7;
	    boolean kingside = kingTo.getCol() == 6;
	    boolean queenside = kingTo.getCol() == 2;

	    // Check castling rights
	    if (kingside) {
	        if ((color == Color.WHITE && !whiteCanCastleKingside) || 
	            (color == Color.BLACK && !blackCanCastleKingside)) {
	        	return false;
	        }
	    } else if (queenside) {
	        if ((color == Color.WHITE && !whiteCanCastleQueenside) || 
	            (color == Color.BLACK && !blackCanCastleQueenside)) {
	        	return false;
	        }
	    } else {
	        return false;
	    }

	    // Check if squares between king and rook are empty
	    if (kingside) {
	        if (getPieceAt(new Position(row, 5)) != null ||
	            getPieceAt(new Position(row, 6)) != null) {
	        	return false;
	        }
	    } else {
	        if (getPieceAt(new Position(row, 1)) != null ||
	            getPieceAt(new Position(row, 2)) != null ||
	            getPieceAt(new Position(row, 3)) != null) {
	        	return false;
	        }
	    }

	    // Check that king is not in check or passing through check
	    if (isSquareAttacked(kingFrom, color) || 
	        isSquareAttacked(new Position(row, kingside ? 5 : 3), color) ||
	        isSquareAttacked(kingTo, color)) {
	    	return false;
	    }

	    // Check if rook is present and correct color
	    Position rookPos = new Position(row, kingside ? 7 : 0);
	    Piece rook = getPieceAt(rookPos);
	    if (!(rook instanceof Rook) || rook.getColor() != color) {
	    	return false;
	    }

	    return true;
	}

	
	private boolean isSquareAttacked(Position position, Color defenderColor) {
		// iterate over the opposing pieces and generate their legal attacks
		return false;
	}
	
	private void moveRookForCastling(Position kingFrom, Position kingTo) {
	    int row = kingFrom.getRow();
	    if (kingTo.getCol() == 6) { // Kingside
	        Position rookFrom = new Position(row, 7);
	        Position rookTo = new Position(row, 5);
	        Piece rook = getPieceAt(rookFrom);
	        setPieceAt(rookFrom, null);
	        setPieceAt(rookTo, rook);
	        if (rook != null) {
	        	rook.setPosition(rookTo);
	        }
	    } else if (kingTo.getCol() == 2) { // Queenside
	        Position rookFrom = new Position(row, 0);
	        Position rookTo = new Position(row, 3);
	        Piece rook = getPieceAt(rookFrom);
	        setPieceAt(rookFrom, null);
	        setPieceAt(rookTo, rook);
	        if (rook != null) {
	        	rook.setPosition(rookTo);
	        }
	    }
	}

	private void movePiece(Position from, Position to) {
		Piece piece = getPieceAt(from);
		setPieceAt(from, null);
		setPieceAt(to, piece);
		piece.setPosition(to);
	}
	
	public List<Piece> getActivePieces(Predicate<Piece> filter) {
		return Arrays.stream(board)
			.flatMap(Arrays::stream)
			.filter(p -> p != null && filter.test(p))
			.toList();
	}

	public List<Piece> getActivePieces(Color color) {
		return getActivePieces(p -> p.getColor() == color);
	}

	public List<Piece> getAllActivePieces() {
		return getActivePieces(p -> true);
	}
	
	public List<Move> getMoveHistory() {
	    return List.copyOf(moveHistory); 
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
