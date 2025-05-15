package com.optimism.chess.engine.pieces;

//@formatter:off
public enum Direction {
    UP(-1, 0),
    DOWN(1, 0),
    LEFT(0, -1),
    RIGHT(0, 1),
    UP_LEFT(-1, -1),
    UP_RIGHT(-1, 1),
    DOWN_LEFT(1, -1),
    DOWN_RIGHT(1, 1),
    
    // 8 possible moves for the knight (L-shaped moves)
    UP_2_RIGHT_1(2, 1),
    UP_2_LEFT_1(2, -1),
    DOWN_2_RIGHT_1(-2, 1),
    DOWN_2_LEFT_1(-2, -1),
    RIGHT_2_UP_1(1, 2),
    RIGHT_2_DOWN_1(1, -2),
    LEFT_2_UP_1(-1, 2),
    LEFT_2_DOWN_1(-1, -2);

    private final int rowOffset;
    private final int colOffset;

    Direction(int rowOffset, int colOffset) {
        this.rowOffset = rowOffset;
        this.colOffset = colOffset;
    }

    public int rowOffset() {
        return rowOffset;
    }

    public int colOffset() {
        return colOffset;
    }

    public static final Direction[] ROOK_DIRECTIONS = {
        UP, DOWN, LEFT, RIGHT
    };

    public static final Direction[] BISHOP_DIRECTIONS = {
        UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT
    };

    public static final Direction[] QUEEN_DIRECTIONS = {
        UP, DOWN, LEFT, RIGHT,
        UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT
    };
    
    public static final Direction[] KING_DIRECTIONS = {
            UP, DOWN, LEFT, RIGHT,
            UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT
        };

	public static final Direction[] KNIGHT_DIRECTIONS = { UP_2_RIGHT_1, UP_2_LEFT_1, DOWN_2_RIGHT_1, DOWN_2_LEFT_1,
			RIGHT_2_UP_1, RIGHT_2_DOWN_1, LEFT_2_UP_1, LEFT_2_DOWN_1

	};
}
