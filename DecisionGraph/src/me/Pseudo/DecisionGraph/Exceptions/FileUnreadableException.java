package me.Pseudo.DecisionGraph.Exceptions;

public class FileUnreadableException extends Exception {
	private static final long serialVersionUID = -4387765847668782270L;
	public FileUnreadableException() {
		super("Unable to read from file!");
	}
}
