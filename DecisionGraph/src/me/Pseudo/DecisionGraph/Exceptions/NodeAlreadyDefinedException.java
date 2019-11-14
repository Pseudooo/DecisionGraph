package me.Pseudo.DecisionGraph.Exceptions;

public class NodeAlreadyDefinedException extends InvalidSyntaxException {
	private static final long serialVersionUID = 3778216474485994005L;
	public NodeAlreadyDefinedException(String nodeID, int line) {
		super("Node " + nodeID + " has already been defined" + (line > 4 ? ": " + line : "!"));
	}
}
