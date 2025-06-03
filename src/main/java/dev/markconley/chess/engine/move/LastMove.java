package dev.markconley.chess.engine.move;

import dev.markconley.chess.engine.core.Position;
import dev.markconley.chess.engine.pieces.Piece;

public record LastMove(Piece piece, Position from, Position to) {}
