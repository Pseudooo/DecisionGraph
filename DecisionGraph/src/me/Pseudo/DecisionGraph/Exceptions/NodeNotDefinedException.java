package me.Pseudo.DecisionGraph.Exceptions;

public class NodeNotDefinedException extends InvalidSyntaxException {
	private static final long serialVersionUID = 6131387889384959142L;
	public NodeNotDefinedException(String node, int line) {
		super("Node "+node+" is not defined" + (line > 4 ? ": " + line : "!"));
	}
}
