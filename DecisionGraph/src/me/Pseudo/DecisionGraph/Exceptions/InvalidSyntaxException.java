package me.Pseudo.DecisionGraph.Exceptions;

public class InvalidSyntaxException extends Exception {
	private static final long serialVersionUID = 6941901282905070001L;

	public InvalidSyntaxException(String ln, int lnnum) {
		super(String.format("Invalid Syntax! %s! Line#:%d%n", ln, lnnum));
	}
}
