package me.Pseudo.DecisionGraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import me.Pseudo.DecisionGraph.Exceptions.ChildlessNodeException;
import me.Pseudo.DecisionGraph.Exceptions.InvalidArgumentsException;
import me.Pseudo.DecisionGraph.Exceptions.InvalidOperationException;
import me.Pseudo.DecisionGraph.Exceptions.NodeAlreadyDefinedException;
import me.Pseudo.DecisionGraph.Exceptions.NodeNotDefinedException;
import me.Pseudo.DecisionGraph.Exceptions.RootNodeMissingException;
import me.Pseudo.DecisionGraph.Exceptions.UnreadableFileException;

public class DecisionGraph {

	private final String root;
	private final HashMap<String, Node> nodes = new HashMap<String, Node>();
	
	// Current and path for active traversal
	private String cur = null;
	
	private ArrayList<String> textPath = null;
	private ArrayList<String> responsePath = null;
	
	/**
	 * Create a new decision graph from a dg script
	 * @param f File to load
	 * @throws Exception
	 */
	public DecisionGraph(File f) throws FileNotFoundException, UnreadableFileException, IOException,
	InvalidArgumentsException, NodeAlreadyDefinedException, NodeNotDefinedException, 
	InvalidOperationException, RootNodeMissingException, ChildlessNodeException {
		
		// No file
		if(!f.exists()) {
			throw new FileNotFoundException();
		}
		
		// Can't read
		if(!f.canRead()) {
			throw new UnreadableFileException();
		}
		
		// ******************************* START OF FILE READING **************************************
		
		BufferedReader br = new BufferedReader(new FileReader(f));
		String ln, rt = null;
		int line = 0;
		while((ln = br.readLine()) != null) {
			line++;
			
			// Ignore whitespace and comments
			if(ln.startsWith("#") || ln.equals("")) continue;		
		
			String[] cmd = ln.split(" ");
			if(cmd[0].equals("define") || cmd[0].equals("def")) {
				
				/*
				 * Syntax for define is:
				 * define <node_id> <is_endpoint> <text>
				 */
				if(cmd.length < 4) { // Not enough args
					throw new InvalidArgumentsException(line);
				}
				
				if(this.nodes.containsKey(cmd[1])) { // Node already defined
					throw new NodeAlreadyDefinedException(cmd[1], line);
				}
				
				// Parse endpoint status
				boolean isEndpoint = Boolean.parseBoolean(cmd[2]);
				
				String text = "";
				for(int i = 3; i < cmd.length; i++) {
					text += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				
				// Place node into hashmap
				Node node = new Node(cmd[1], text, isEndpoint);
				this.nodes.put(cmd[1], node);
				
			}else if(cmd[0].equals("assert") || cmd[0].equals("asrt")) {
				
				/*
				 * Syntax for assert is:
				 * assert <from_id> <to_id> <response>
				 */
				if(cmd.length < 4) { // Not enough args
					throw new InvalidArgumentsException(line);
				}
				
				// Check both nodes are defined
				if(!this.nodes.containsKey(cmd[1])) {
					throw new NodeNotDefinedException(cmd[1], line);
				}
				if(!this.nodes.containsKey(cmd[2])) {
					throw new NodeNotDefinedException(cmd[1], line);
				}
				
				// Parse response
				String response = "";
				for(int i = 3; i < cmd.length; i++) {
					response += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				
				// Assign response mapping to node
				Node node = this.nodes.get(cmd[1]);
				node.assignResponse(response, cmd[2]);
				
			}else if(cmd[0].equals("mkroot") || cmd[0].equals("mkrt")) {
				
				/*
				 * Syntax for mkroot is:
				 * mkroot <node_id>
				 */
				if(cmd.length != 2) { // Invalid Arguments
					throw new InvalidArgumentsException(line);
				}
				
				if(!this.nodes.containsKey(cmd[1])) { // Not defined
					throw new NodeNotDefinedException(cmd[1], line);
				}
				
				rt = cmd[1];
				
			}else {
				
				// Invalid Operation!
				throw new InvalidOperationException(line);
				
			}
			
		}
		br.close();
		
		// ********************************** END OF FILE READING *************************************
		
		// Make sure that a root has been declared
		if(rt == null) {
			throw new RootNodeMissingException();
		}
		this.root = rt;
		
		// Check all non-endings have children
		for(Node node : this.nodes.values()) {
			if(!node.isEndpoint() && node.responseCount() == 0) {
				throw new ChildlessNodeException(node.ID());
			}
		}
		
	}
	
	/**
	 * Check if this graph is actively being traversed
	 * @return
	 */
	public boolean isBeingTraversed() {
		return this.cur == null;
	}
	
	/**
	 * Setup this graph to start traversing
	 */
	public void startTraversing() {
		this.cur = this.root;
		this.textPath = new ArrayList<String>();
		this.textPath.add(this.text()); // Enter root node into path
		this.responsePath = new ArrayList<String>();
	}
	
	/**
	 * End traversing of the graph
	 */
	public void endTraversal() {
		this.cur = null;
		this.textPath = null;
		this.responsePath = null;
	}
	
	/**
	 * Get a list of valid responses
	 * @return Responses
	 */
	public ArrayList<String> getResponses() {
		return this.nodes.get(this.cur).responses();
	}
	
	/**
	 * Get the text of the nodes traversed through in order
	 * @return Text path
	 */
	public ArrayList<String> getTextPath() {
		return this.textPath;
	}
	
	/**
	 * Get the responses given during traversal in order
	 * @return Response path
	 */
	public ArrayList<String> getResponsePath() {
		return this.responsePath;
	}
	
	/**
	 * Assert a response during traversal
	 * @param response
	 */
	public void assertResponse(String response) {
		Node node = this.nodes.get(this.cur);
		this.cur = this.nodes.get(node.getResponseResult(response)).ID();
		this.responsePath.add(response);
		this.textPath.add(this.text());
	}
	
	/**
	 * Check if currently at an end-point
	 * @return
	 */
	public boolean isEndpoint() {
		return this.nodes.get(this.cur).isEndpoint();
	}
	
	/**
	 * Get the text of the current node
	 * @return
	 */
	public String text() {
		return this.nodes.get(this.cur).text();
	}
	
}
