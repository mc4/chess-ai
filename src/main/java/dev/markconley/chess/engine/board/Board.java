package dev.markconley.chess.engine.board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import dev.markconley.chess.engine.core.Color;
import dev.markconley.chess.engine.core.Copyable;
import dev.markconley.chess.engine.core.Position;
import dev.markconley.chess.engine.move.AttackMapGenerator;
import dev.markconley.chess.engine.move.LastMove;
import dev.markconley.chess.engine.move.Move;
import dev.markconley.chess.engine.move.MoveFactory;
import dev.markconley.chess.engine.move.handler.CastlingMoveHandler;
import dev.markconley.chess.engine.move.handler.EnPassantMoveHandler;
import dev.markconley.chess.engine.move.handler.PromotionMoveHandler;
import dev.markconley.chess.engine.move.handler.SpecialMoveHandler;
import dev.markconley.chess.engine.pieces.Bishop;
import dev.markconley.chess.engine.pieces.King;
import dev.markconley.chess.engine.pieces.Knight;
import dev.markconley.chess.engine.pieces.Pawn;
import dev.markconley.chess.engine.pieces.Piece;
import dev.markconley.chess.engine.pieces.PieceType;
import dev.markconley.chess.engine.pieces.Queen;
import dev.markconley.chess.engine.pieces.Rook;
import dev.markconley.chess.engine.state.CastlingRights;

public class Board implements Copyable<Board> {

	private static final int BOARD_SIZE = 8;
	private Piece[][] board;
	private Color currentTurn;
	private List<Move> moveHistory;
	private LastMove lastMove;
	
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
		this.castlingRights = new CastlingRights();
		
		if (!skipSetup) {
			setupInitialPosition();
		}
	}
	
	public static Board emptyBoard() {
	    return new Board(true); 
	}
	
	public Board place(String square, Piece piece) {
	    Position pos = Position.of(square);
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
	
	public LastMove getLastMove() {
	    return lastMove;
	}
	
	public void recordLastMove(Piece piece, Position from, Position to) {
	    this.lastMove = new LastMove(piece, from, to);
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

	    recordLastMove(movedPiece, from, to);
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
	    if (from.equals(Position.of("e1")) && to.equals(Position.of("g1")) && color == Color.WHITE) {
	        return rights.whiteCanCastleKingside() &&
	               isRookAt(Position.of("h1"), Color.WHITE);
	    }

	    // White Queen-side castling: e1 -> c1
	    if (from.equals(Position.of("e1")) && to.equals(Position.of("c1")) && color == Color.WHITE) {
	        return rights.whiteCanCastleQueenside() &&
	               isRookAt(Position.of("a1"), Color.WHITE);
	    }

	    // Black King-side castling: e8 -> g8
	    if (from.equals(Position.of("e8")) && to.equals(Position.of("g8")) && color == Color.BLACK) {
	        return rights.blackCanCastleKingside() &&
	               isRookAt(Position.of("h8"), Color.BLACK);
	    }

	    // Black Queen-side castling: e8 -> c8
	    if (from.equals(Position.of("e8")) && to.equals(Position.of("c8")) && color == Color.BLACK) {
	        return rights.blackCanCastleQueenside() &&
	               isRookAt(Position.of("a8"), Color.BLACK);
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
		final String separator = "===============================";
		System.out.println(separator);
		System.out.println("  a b c d e f g h");
		for (int row = 7; row >= 0; row--) {
			System.out.print((row + 1) + " ");
			for (int col = 0; col < 8; col++) {
				Piece piece = getPieceAt(Position.of(row, col));
				String symbol = piece == null ? "." : getSymbol(piece);
				System.out.print(symbol + " ");
			}
			System.out.println(row + 1);
		}
		System.out.println("  a b c d e f g h");
		System.out.println(separator);
	}

	private String getSymbol(Piece piece) {
		String base = switch (piece.getPieceType()) {
			case KING -> "K";
			case QUEEN -> "Q";
			case ROOK -> "R";
			case BISHOP -> "B";
			case KNIGHT -> "N";
			case PAWN -> "P";
		};
		return piece.getColor() == Color.WHITE ? base : base.toLowerCase();
	}

	
}
