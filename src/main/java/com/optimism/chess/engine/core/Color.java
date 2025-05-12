package com.optimism.chess.engine.core;

public enum Color {
	WHITE, BLACK;

	public Color opposite() {
		return this == WHITE ? BLACK : WHITE;
	}
}
