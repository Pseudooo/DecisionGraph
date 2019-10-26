package me.Pseudo.DecisionGraph.Exceptions;

public class ChildlessNodeException extends InvalidSyntaxException {
	private static final long serialVersionUID = 7228139286637345840L;
	public ChildlessNodeException(String node) {
		super("Node "+node+" is not an end-point but lacks children");
	}
}
