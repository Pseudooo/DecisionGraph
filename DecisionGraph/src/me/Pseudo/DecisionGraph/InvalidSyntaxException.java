package me.Pseudo.DecisionGraph;

public class InvalidSyntaxException extends Exception {
	private static final long serialVersionUID = 6941901282905070001L;

	public InvalidSyntaxException(String ln, int lnnum) {
		super(String.format("%s @ line %d%n", ln, lnnum));
	}
}
