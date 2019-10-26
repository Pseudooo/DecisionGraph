package me.Pseudo.DecisionGraph.Exceptions;

public class RootNodeMissingException extends InvalidSyntaxException{
	private static final long serialVersionUID = -8306314453044570700L;
	public RootNodeMissingException() {
		super("Root node not declared");
	}
}
