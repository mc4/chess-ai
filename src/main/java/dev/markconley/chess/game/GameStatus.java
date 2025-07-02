package dev.markconley.chess.game;

public enum GameStatus {
    IN_PROGRESS,

    // Victory
    CHECKMATE_WHITE_WINS,
    CHECKMATE_BLACK_WINS,

    // Draws
    STALEMATE,
    FIFTY_MOVE_RULE,
    THREEFOLD_REPETITION,
    INSUFFICIENT_MATERIAL,
    DRAW_AGREEMENT,

    // Aborted or Adjudicated
    RESIGNATION,
    TIMEOUT,
    ABANDONED
}
