package me.Pseudo.DecisionGraph.Exceptions;

public class InvalidOperationException extends InvalidSyntaxException {
	private static final long serialVersionUID = 1799551486939152318L;
	public InvalidOperationException(int line) {
		super("Invalid Operation:"+line);
	}
}
