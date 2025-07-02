package dev.markconley.chess.io;

import java.util.List;

import dev.markconley.chess.engine.board.Board;
import dev.markconley.chess.engine.core.Color;
import dev.markconley.chess.engine.move.Move;
import dev.markconley.chess.game.GameStatus;

public interface OutputHandler {
    void displayBoard(Board board);
    void promptMove(Color player, List<Move> moves);
    void displayResult(GameStatus result);
    void displayMessage(String message);
}
