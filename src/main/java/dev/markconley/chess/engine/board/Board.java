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
import dev.markconley.chess.engine.move.promotion.PromotionStrategy;
import dev.markconley.chess.engine.move.promotion.QueenPromotionStrategy;
import dev.markconley.chess.engine.pieces.Bishop;
import dev.markconley.chess.engine.pieces.King;
import dev.markconley.chess.engine.pieces.Knight;
import dev.markconley.chess.engine.pieces.Pawn;
import dev.markconley.chess.engine.pieces.Piece;
import dev.markconley.chess.engine.pieces.PieceType;
import dev.markconley.chess.engine.pieces.Queen;
import dev.markconley.chess.engine.pieces.Rook;
import dev.markconley.chess.engine.state.CastlingRights;
import dev.markconley.chess.engine.state.GameStateEvaluator;

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
	
	public void clearBoard() {
		for (int rank = 0; rank < 8; rank++) {
			for (int file = 0; file < 8; file++) {
				Position position = Position.of(rank, file);
				setPieceAt(position, null);
			}
		}
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
	
	public void setCurrentTurn(Color color) {
		this.currentTurn = color;
	}

	public void switchTurn() {
		currentTurn = currentTurn.opposite();
	}
	
	public boolean makeMove(Position from, Position to) {
	    return makeMove(from, to, new QueenPromotionStrategy());
	}
	
	public boolean makeMove(Position from, Position to, PromotionStrategy promotionStrategy) {
		Piece piece = getPieceAt(from);
		if (piece == null || piece.getColor() != currentTurn) {
			return false;
		}

		Move move = generateMove(from, to, promotionStrategy);
		if (move == null || !isMoveLegal(move, currentTurn)) {
			return false;
		}
		
		applyMove(move);
		
	    recordLastMove(piece, from, to);
	    moveHistory.add(move);
	    switchTurn();
		return true;
	}
	
	public Move generateMove(Position from, Position to, PromotionStrategy promotionStrategy) {
	    Piece piece = getPieceAt(from);
	    if (piece == null || piece.getColor() != currentTurn) {
	        return null;
	    }

	    List<SpecialMoveHandler> specialHandlers = List.of(
	        new CastlingMoveHandler(),
	        new EnPassantMoveHandler(),
	        new PromotionMoveHandler(promotionStrategy)
	    );

	    for (SpecialMoveHandler handler : specialHandlers) {
	        if (handler.canHandle(piece, from, to)) {
	            Move move = handler.handle(this, piece, from, to);
	            if (move != null) {
	            	return move;
	            }
	        }
	    }

	    return createStandardMove(from, to, piece);
	}
	
	private Move createStandardMove(Position from, Position to, Piece movedPiece) {

		if (!movedPiece.getPossibleMoves(this)
				.stream()
				.map(Move::to)
				.anyMatch(to::equals)) {
			return null;
		}
		
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

	public void applyMove(Move move) {
	    Piece movedPiece = move.movedPiece();
	    Position from = move.from();
	    Position to = move.to();
	    
	    setPieceAt(from, null);
	    setPieceAt(to, movedPiece);
	    movedPiece.setPosition(to);

	    if (move.isEnPassant()) {
	        Position capturedPawnPos = new Position(from.getRow(), to.getCol());
	        setPieceAt(capturedPawnPos, null);
	    }

	    if (move.isCastling()) {
	    	handleCastling(from, to);
	    }

	    if (move.isPromotion()) {
	        Piece promoted = move.promotionPiece();
	        promoted.setPosition(to);
	        setPieceAt(to, promoted);
	    }

	}
	
	private void handleCastling(Position from, Position to) {
	    int row = from.getRow();
	    int toCol = to.getCol();

	    if (toCol == 6) { // Kingside castling
	        moveRookForCastling(row, 7, 5);
	    } else if (toCol == 2) { // Queenside castling
	        moveRookForCastling(row, 0, 3);
	    }
	}

	private void moveRookForCastling(int row, int fromCol, int toCol) {
	    Position rookFrom = new Position(row, fromCol);
	    Position rookTo = new Position(row, toCol);
	    Piece rook = getPieceAt(rookFrom);
	    setPieceAt(rookTo, rook);
	    setPieceAt(rookFrom, null);
	    rook.setPosition(rookTo);
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
	
	/**
	 * 
	 * @param move
	 * @param currentTurn
	 * 
	 * A move is legal if:
	 *
	 * 1. The piece at the from square belongs to the player whose turn it is.
	 * 2. The piece can move from from to to based on movement rules (including special rules: castling, en passant, promotion).
	 * 3. The destination square does not contain a friendly piece.
	 * 4. The move does not leave the moving player in check.
	 * 
	 * @return
	 */
	public boolean isMoveLegal(Move move, Color currentTurn) {
	    Position from = move.from();
	    Position to = move.to();
	    Piece movingPiece = getPieceAt(from);
	    
	    // Rule 1: Must be a piece of current turn
	    if (movingPiece == null || movingPiece.getColor() != currentTurn) {
	        return false;
	    }

	    // Rule 2 and 3: Move must be in the piece's possible moves
	    List<Move> legalMoves = movingPiece.getPossibleMoves(this).stream()
	        .filter(m -> m.to().equals(to))
	        .toList();

	    if (legalMoves.isEmpty()) {
	        return false;
	    }

	    // Rule 4: Simulate and check for check
	    Board clone = this.copy();
	    clone.applyMove(move);
	    return !GameStateEvaluator.isInCheck(clone, currentTurn);
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

	public Position getEnPassantTarget() {
		if (lastMove == null) {
			return null;
		}

		Piece movedPiece = lastMove.piece();
		Position from = lastMove.from();
		Position to = lastMove.to();

		if (!(movedPiece instanceof Pawn)) {
			return null;
		}

		int rowDiff = Math.abs(from.getRow() - to.getRow());
		if (rowDiff != 2) {
			return null;
		}

		// Return the square "behind" the pawn that moved two steps forward
		int enPassantRow = (from.getRow() + to.getRow()) / 2;
		int col = to.getCol();

		return Position.of(enPassantRow, col);
	}
	
    public static Position findKingPosition(Board board, Color color) {
    	return board.getActivePieces(color)
        		.stream()
        		.filter(Objects::nonNull)
                .filter(p -> p.getPieceType() == PieceType.KING)
                .map(Piece::getPosition)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("King not found on board"));
    }

	@Override
	public Board copy() {
		Board newBoard = Board.emptyBoard();
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

	private String getBoardString() {
		StringBuilder sb = new StringBuilder();
		final String separator = "===============================";
		sb.append(separator).append("\n");
		sb.append("  a b c d e f g h\n");
		for (int row = 7; row >= 0; row--) {
			sb.append(row + 1).append(" ");
			for (int col = 0; col < 8; col++) {
				Piece piece = getPieceAt(Position.of(row, col));
				String symbol = piece == null ? "." : getSymbol(piece);
				sb.append(symbol).append(" ");
			}
			sb.append(row + 1).append("\n");
		}
		sb.append("  a b c d e f g h\n");
		sb.append(separator);
		return sb.toString();
	}

	public void printBoard() {
		System.out.println(getBoardString());
	}

	@Override
	public String toString() {
		return getBoardString();
	}

}
