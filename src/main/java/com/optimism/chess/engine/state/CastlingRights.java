package com.optimism.chess.engine.state;

import java.util.Objects;

import com.optimism.chess.engine.core.Color;
import com.optimism.chess.engine.core.Copyable;

public final class CastlingRights implements Copyable<CastlingRights> {
	private boolean whiteKingside;
	private boolean whiteQueenside;
	private boolean blackKingside;
	private boolean blackQueenside;

	public CastlingRights() {
		this(true, true, true, true);
	}

	public CastlingRights(boolean canCastleWhiteKingside, boolean canCastleWhiteQueenside,
			boolean canCastleBlackKingside, boolean canCastleBlackQueenside) {
		this.whiteKingside = canCastleWhiteKingside;
		this.whiteQueenside = canCastleWhiteQueenside;
		this.blackKingside = canCastleBlackKingside;
		this.blackQueenside = canCastleBlackQueenside;
	}

	public boolean whiteCanCastleKingside() {
		return whiteKingside;
	}

	public boolean whiteCanCastleQueenside() {
		return whiteQueenside;
	}

	public boolean blackCanCastleKingside() {
		return blackKingside;
	}

	public boolean blackCanCastleQueenside() {
		return blackQueenside;
	}

	public void revokeWhiteKingside() {
		this.whiteKingside = false;
	}

	public void revokeWhiteQueenside() {
		this.whiteQueenside = false;
	}

	public void revokeBlackKingside() {
		this.blackKingside = false;
	}

	public void revokeBlackQueenside() {
		this.blackQueenside = false;
	}

	public void disableAll(Color color) {
		if (color == Color.WHITE) {
			revokeWhiteKingside();
			revokeWhiteQueenside();
		} else {
			revokeBlackKingside();
			revokeBlackQueenside();
		}
	}

	public void disableQueenside(Color color) {
		if (color == Color.WHITE) {
			revokeWhiteQueenside();
		} else {
			revokeBlackQueenside();
		}
	}

	public void disableKingside(Color color) {
		if (color == Color.WHITE) {
			revokeWhiteKingside();
		} else {
			revokeBlackKingside();
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(blackKingside, blackQueenside, whiteKingside, whiteQueenside);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		CastlingRights other = (CastlingRights) obj;
		return blackKingside == other.blackKingside && blackQueenside == other.blackQueenside
				&& whiteKingside == other.whiteKingside && whiteQueenside == other.whiteQueenside;
	}

	@Override
	public CastlingRights copy() {
		CastlingRights cr = new CastlingRights();
		cr.whiteKingside = this.whiteKingside;
		cr.whiteQueenside = this.whiteQueenside;
		cr.blackKingside = this.blackKingside;
		cr.blackQueenside = this.blackQueenside;
		return cr;
	}

}
