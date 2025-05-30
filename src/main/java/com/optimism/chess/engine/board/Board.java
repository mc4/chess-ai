package com.optimism.chess.engine.board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import com.optimism.chess.engine.core.Color;
import com.optimism.chess.engine.core.Copyable;
import com.optimism.chess.engine.core.Position;
import com.optimism.chess.engine.move.AttackMapGenerator;
import com.optimism.chess.engine.move.Move;
import com.optimism.chess.engine.move.MoveFactory;
import com.optimism.chess.engine.move.handler.CastlingMoveHandler;
import com.optimism.chess.engine.move.handler.EnPassantMoveHandler;
import com.optimism.chess.engine.move.handler.PromotionMoveHandler;
import com.optimism.chess.engine.move.handler.SpecialMoveHandler;
import com.optimism.chess.engine.pieces.Bishop;
import com.optimism.chess.engine.pieces.King;
import com.optimism.chess.engine.pieces.Knight;
import com.optimism.chess.engine.pieces.Pawn;
import com.optimism.chess.engine.pieces.Piece;
import com.optimism.chess.engine.pieces.PieceType;
import com.optimism.chess.engine.pieces.Queen;
import com.optimism.chess.engine.pieces.Rook;
import com.optimism.chess.engine.state.CastlingRights;

public class Board implements Copyable<Board> {

	private static final int BOARD_SIZE = 8;
	private Piece[][] board;
	private Color currentTurn;
	private List<Move> moveHistory;
	private CastlingRights castlingRights;
    private final CastlingMoveHandler castlingMoveHandler = new CastlingMoveHandler();
    
	public Board() {
		this.board = new Piece[BOARD_SIZE][BOARD_SIZE];
		setupInitialPosition();
		this.currentTurn = Color.WHITE;
		this.moveHistory = new ArrayList<>();
		this.castlingRights = new CastlingRights();
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
	
	public CastlingRights getCastlingRights() {
	    return castlingRights;
	}

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
	
	public boolean makeMove(Position from, Position to) {
	    Piece movedPiece = getPieceAt(from);
	    if (movedPiece == null || movedPiece.getColor() != currentTurn) {
	        return false;
	    }

	    final List<SpecialMoveHandler> specialMoveHandlers = List.of(
	        new CastlingMoveHandler(),
	        new EnPassantMoveHandler(),
	        new PromotionMoveHandler()
	    );

	    Move move = specialMoveHandlers.stream()
	        .filter(handler -> handler.canHandle(movedPiece, from, to))
	        .map(handler -> handler.handle(this, movedPiece, from, to))
	        .filter(Objects::nonNull)
	        .findFirst()
	        .orElseGet(() -> createStandardMove(from, to, movedPiece));

	    if (move == null) {
	    	return false;
	    }

	    applyMove(move, movedPiece, from, to);
	    return true;
	}
	private Move createStandardMove(Position from, Position to, Piece movedPiece) {
	    Piece targetPiece = getPieceAt(to);

	    if (targetPiece == null) {
	        castlingMoveHandler.updateCastlingRightsOnMove(this, movedPiece, from);
	        return MoveFactory.normal(from, to, movedPiece);
	    }

	    if (targetPiece.getColor() != movedPiece.getColor()) {
	        castlingMoveHandler.updateCastlingRightsOnMove(this, movedPiece, from);
	        castlingMoveHandler.updateCastlingRightsOnRookCapture(this, targetPiece, to);
	        return MoveFactory.capture(from, to, movedPiece, targetPiece);
	    }

	    return null;
	}

	private void applyMove(Move move, Piece movedPiece, Position from, Position to) {
	    if (!move.isPromotion()) {
	        setPieceAt(to, movedPiece);
	        movedPiece.setPosition(to);
	    }
	    setPieceAt(from, null);
	    moveHistory.add(move);
	    switchTurn();
	}

	public boolean canCastle(Position from, Position to, Color color) {
	    CastlingRights rights = getCastlingRights();

	    // White King-side castling: e1 -> g1
	    if (from.equals(new Position("e1")) && to.equals(new Position("g1")) && color == Color.WHITE) {
	        return rights.whiteCanCastleKingside() &&
	               isRookAt(new Position("h1"), Color.WHITE);
	    }

	    // White Queen-side castling: e1 -> c1
	    if (from.equals(new Position("e1")) && to.equals(new Position("c1")) && color == Color.WHITE) {
	        return rights.whiteCanCastleQueenside() &&
	               isRookAt(new Position("a1"), Color.WHITE);
	    }

	    // Black King-side castling: e8 -> g8
	    if (from.equals(new Position("e8")) && to.equals(new Position("g8")) && color == Color.BLACK) {
	        return rights.blackCanCastleKingside() &&
	               isRookAt(new Position("h8"), Color.BLACK);
	    }

	    // Black Queen-side castling: e8 -> c8
	    if (from.equals(new Position("e8")) && to.equals(new Position("c8")) && color == Color.BLACK) {
	        return rights.blackCanCastleQueenside() &&
	               isRookAt(new Position("a8"), Color.BLACK);
	    }

	    return false;
	}

	private boolean isRookAt(Position position, Color color) {
	    Piece piece = getPieceAt(position);
	    return piece != null &&
	           piece.getColor() == color &&
	           piece.getPieceType() == PieceType.ROOK;
	}
	
	public boolean isSquareAttacked(Position position, Color defenderColor) {
	    return getActivePieces(defenderColor.opposite()).stream()
	        .flatMap(piece -> AttackMapGenerator.generateAttackSquares(this, piece).stream())
	        .anyMatch(position::equals);
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
