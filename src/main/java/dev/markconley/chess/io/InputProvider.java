package dev.markconley.chess.io;

import java.util.List;
import java.util.Optional;

import dev.markconley.chess.engine.move.Move;

public interface InputProvider {
    Optional<Move> getMove(List<Move> legalMoves);
}