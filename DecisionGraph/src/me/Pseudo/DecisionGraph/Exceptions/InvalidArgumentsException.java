package me.Pseudo.DecisionGraph.Exceptions;

public class InvalidArgumentsException extends InvalidSyntaxException {
	private static final long serialVersionUID = -5459165953171415219L;
	public InvalidArgumentsException(int line) {
		super("Invalid Arguments for given operation:"+line);
	}
}
