package dev.markconley.chess.engine.state;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import dev.markconley.chess.engine.board.Board;
import dev.markconley.chess.engine.core.Color;
import dev.markconley.chess.engine.core.Copyable;
import dev.markconley.chess.engine.core.Position;
import dev.markconley.chess.engine.move.AttackMapGenerator;
import dev.markconley.chess.engine.move.LastMove;
import dev.markconley.chess.engine.move.Move;
import dev.markconley.chess.engine.move.service.SpecialMoveService;
import dev.markconley.chess.engine.pieces.Piece;

public class BoardState implements Copyable<BoardState> {

    private final Board board;
    private Color currentTurn;
    private final List<Move> moveHistory;
    private LastMove lastMove;
    private int halfMoveClock;
    private CastlingRights castlingRights;
    
	private final SpecialMoveService specialMoveService = new SpecialMoveService();

    public BoardState(Board board) {
        this.board = board;
        this.currentTurn = Color.WHITE;
        this.moveHistory = new ArrayList<>();
        this.castlingRights = new CastlingRights();
    }
    
    public boolean isSquareAttacked(Position position, Color defenderColor) {
        return getActivePieces(defenderColor.opposite()).stream()
            .flatMap(piece -> AttackMapGenerator.generateAttackSquares(board, piece).stream())
            .anyMatch(position::equals);
    }
	
    public List<Piece> getActivePieces(Predicate<Piece> filter) {
    	return board.streamPieces()
    		.filter(filter)
    		.toList();
    }

	public List<Piece> getActivePieces(Color color) {
		return getActivePieces(p -> p.getColor() == color);
	}

	public List<Piece> getAllActivePieces() {
		return getActivePieces(p -> true);
	}

    public Board getBoard() {
        return board;
    }

    public Color getCurrentTurn() {
        return currentTurn;
    }

    public void switchTurn() {
        currentTurn = currentTurn.opposite();
    }

    public void setCurrentTurn(Color color) {
        this.currentTurn = color;
    }

    public void recordMove(Move move) {
        this.lastMove = new LastMove(move.movedPiece(), move.from(), move.to());
        moveHistory.add(move);
    }

    public LastMove getLastMove() {
        return lastMove;
    }

    public List<Move> getMoveHistory() {
        return Collections.unmodifiableList(moveHistory);
    }

    public CastlingRights getCastlingRights() {
        return castlingRights;
    }

    public void setCastlingRights(CastlingRights rights) {
        this.castlingRights = rights;
    }
    
	public SpecialMoveService getSpecialMoveService() {
	    return specialMoveService;
	}
	
    public int getHalfMoveClock() {
        return halfMoveClock;
    }

    public void setHalfMoveClock(int halfMoveClock) {
        this.halfMoveClock = halfMoveClock;
    }

    public void incrementHalfMoveClock() {
        this.halfMoveClock++;
    }

    public void resetHalfMoveClock() {
        this.halfMoveClock = 0;
    }

    public Position getEnPassantTarget() {
        if (lastMove == null) {
        	return null;
        }

        var piece = lastMove.piece();
        var from = lastMove.from();
        var to = lastMove.to();

        if (!"PAWN".equals(piece.getPieceType().name())) {
        	return null;
        }

        int rowDiff = Math.abs(from.getRow() - to.getRow());
        if (rowDiff != 2) {
        	return null;
        }

        int middleRow = (from.getRow() + to.getRow()) / 2;
        return Position.of(middleRow, to.getCol());
    }

    @Override
    public BoardState copy() {
        BoardState copy = new BoardState(this.board.copy());
        copy.currentTurn = this.currentTurn;
        copy.lastMove = this.lastMove != null ? this.lastMove.copy() : null;
        copy.moveHistory.addAll(this.moveHistory); // Move is usually immutable
        copy.castlingRights = this.castlingRights.copy();
        copy.halfMoveClock = this.halfMoveClock;
        return copy;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BoardState that = (BoardState) o;

        return halfMoveClock == that.halfMoveClock &&
               currentTurn == that.currentTurn &&
               castlingRights.equals(that.castlingRights) &&
               board.equals(that.board) &&
               Objects.equals(getEnPassantTarget(), that.getEnPassantTarget());
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, currentTurn, castlingRights, halfMoveClock, getEnPassantTarget());
    }
    
}
