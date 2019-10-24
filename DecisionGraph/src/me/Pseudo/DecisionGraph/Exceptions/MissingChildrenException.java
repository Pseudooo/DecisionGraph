package me.Pseudo.DecisionGraph.Exceptions;

public class MissingChildrenException extends Exception {
	private static final long serialVersionUID = -1325578476540491590L;
	public MissingChildrenException(String ID) {
		super(String.format("%s Is Missing Children", ID));
	}
}
