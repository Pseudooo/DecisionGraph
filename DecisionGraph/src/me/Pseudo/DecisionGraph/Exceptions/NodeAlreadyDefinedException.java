package me.Pseudo.DecisionGraph.Exceptions;

public class NodeAlreadyDefinedException extends InvalidSyntaxException {
	private static final long serialVersionUID = 3778216474485994005L;
	public NodeAlreadyDefinedException(String nodeID, int line) {
		super(String.format("Node %s has already been defined:%d", nodeID, line));
	}
}
