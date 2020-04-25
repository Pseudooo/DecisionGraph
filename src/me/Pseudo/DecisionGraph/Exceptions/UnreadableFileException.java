package me.Pseudo.DecisionGraph.Exceptions;

public class UnreadableFileException extends Exception{
	private static final long serialVersionUID = 3064344548511853308L;
	public UnreadableFileException() {
		super("Unable to read file!");
	}
}
