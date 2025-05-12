package com.optimism.chess.engine.move;

import java.util.Objects;

import com.optimism.chess.engine.core.Position;
import com.optimism.chess.engine.pieces.Piece;

public class Move {

	// Required
	private final Position from;
	private final Position to;

	// Optional
	private final Piece movedPiece;
	private final Piece capturedPiece;

	private final boolean isCastling;
	private final boolean isEnPassant;
	private final Piece promotionPiece;

	private Move(Builder builder) {
		this.from = builder.from;
		this.to = builder.to;
		this.movedPiece = builder.movedPiece;
		this.capturedPiece = builder.capturedPiece;
		this.isCastling = builder.isCastling;
		this.isEnPassant = builder.isEnPassant;
		this.promotionPiece = builder.promotionPiece;
	}

	public Position getFrom() {
		return from;
	}

	public Position getTo() {
		return to;
	}

	public Piece getMovedPiece() {
		return movedPiece;
	}

	public Piece getCapturedPiece() {
		return capturedPiece;
	}

	public boolean isCastling() {
		return isCastling;
	}

	public boolean isEnPassant() {
		return isEnPassant;
	}

	public boolean isPromotion() {
		return promotionPiece != null;
	}

	public Piece getPromotionPiece() {
		return promotionPiece;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Move))
			return false;
		Move move = (Move) o;
		return isCastling == move.isCastling && isEnPassant == move.isEnPassant && Objects.equals(from, move.from)
				&& Objects.equals(to, move.to) && Objects.equals(movedPiece, move.movedPiece)
				&& Objects.equals(capturedPiece, move.capturedPiece)
				&& Objects.equals(promotionPiece, move.promotionPiece);
	}

	@Override
	public int hashCode() {
		return Objects.hash(from, to, movedPiece, capturedPiece, isCastling, isEnPassant, promotionPiece);
	}

	@Override
	public String toString() {
		return from + " -> " + to + (isPromotion() ? " (promotes to " + promotionPiece + ")" : "");
	}

	public static class Builder {
		private final Position from;
		private final Position to;

		private Piece movedPiece;
		private Piece capturedPiece;
		private boolean isCastling;
		private boolean isEnPassant;
		private Piece promotionPiece;

		public Builder(Position from, Position to) {
		    Objects.requireNonNull(from, "From position is required");
		    Objects.requireNonNull(to, "To position is required");
			this.from = from;
			this.to = to;
		}

		public Builder movedPiece(Piece movedPiece) {
			this.movedPiece = movedPiece;
			return this;
		}

		public Builder capturedPiece(Piece capturedPiece) {
			this.capturedPiece = capturedPiece;
			return this;
		}

		public Builder castling(boolean isCastling) {
			this.isCastling = isCastling;
			return this;
		}

		public Builder enPassant(boolean isEnPassant) {
			this.isEnPassant = isEnPassant;
			return this;
		}

		public Builder promotionPiece(Piece promotionPiece) {
			this.promotionPiece = promotionPiece;
			return this;
		}

		public Move build() {
			return new Move(this);
		}
	}
}
