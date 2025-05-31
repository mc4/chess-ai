package dev.markconley.chess.exception;

public class InvalidMoveException extends Exception {
	
	private static final long serialVersionUID = -384461818970327940L;

	public InvalidMoveException(String message) {
		super(message);
	}

}
