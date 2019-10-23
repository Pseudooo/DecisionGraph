package me.Pseudo.DecisionGraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class DecisionGraph {

	private final Node root;
	
	private Node cur;
	private ArrayList<String> path = null;
	
	private DecisionGraph(Node root) {
		this.root = root;
		this.cur = null;
	}
	
	public void startTraversing() {
		this.cur = this.root;
		this.path = new ArrayList<String>();
	}
	
	/**
	 * Check if graph is currently being traversed
	 * @return
	 */
	public boolean isTraversing() {
		return this.cur == null;
	}
	
	/**
	 * Check if the traversal has reached an end point
	 * @return
	 */
	public boolean endReached() {
		return this.cur.isEndpoint();
	}
	
	/**
	 * End traversing of the graph
	 */
	public void endTraversal() {
		this.cur = null;
		this.path = null;
	}
	
	/**
	 * Get the traversal route of the graph
	 * @return
	 */
	public ArrayList<String> getPath() {
		return this.path;
	}
	
	/**
	 * The current question
	 * @return
	 */
	public String question() {
		return this.cur.question();
	}
	
	/**
	 * Give your current response, i.e. true/false for yes/no
	 * @param response The response given
	 */
	public void respond(boolean response) {
		this.path.add(this.cur.question());
		this.path.add(response ? "Yes" : "No");
		if(response) {
			this.cur = this.cur.getYesNode();
		}else {
			this.cur = this.cur.getNoNode();
		}
	}
	
	/**
	 * Read a decision graph from a file
	 * @param file File to read
	 * @return Decision Graph
	 * @throws Exception IO
	 */
	public static DecisionGraph fromFile(File file) throws
	Exception, FileNotFoundException {
		
		// Handle exceptions
		if(!file.exists()) {
			throw new FileNotFoundException("File not found!");		
		}
		
		if(!file.canRead()) {
			throw new Exception("Can't read file!");
		}
		
		// Setup to read the file
		BufferedReader br = new BufferedReader(new FileReader(file));
		HashMap<String, Node> nodes = new HashMap<String, Node>();
		String ln, root = null;
		int lnnum = 1;
		while((ln = br.readLine()) != null) {
			
			// Ignore comments and whitespace
			if(ln.startsWith("#") || ln.equals("")) {
				continue;
			}
			
			// Setup command
			String[] cmd = ln.split(" ");
			
			if(cmd[0].equalsIgnoreCase("define")) {
				
				// Minimum for 4 parameters
				if(cmd.length < 4) {
					throw new InvalidSyntaxException("Invalid Paramters!", lnnum);
				}
				
				// Check node isn't defined already with ID
				if(nodes.containsKey(cmd[1])) {
					throw new InvalidSyntaxException("Node already defined!", lnnum);
				}
				
				// Attempt to parse boolean
				boolean isEndpoint = false;
				try {
					isEndpoint = Boolean.parseBoolean(cmd[2]);
				}catch(Exception e) {
					throw new InvalidSyntaxException("Invalid Boolean!", lnnum);
				}
				
				String question = "";
				for(int i = 3; i < cmd.length; i++) {
					question += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				
				Node node = new Node(question, cmd[1], isEndpoint);
				nodes.put(cmd[1], node);
				
			}else if(cmd[0].equalsIgnoreCase("assert")) {
				
				// Assert always needs exactly 4 params
				if(cmd.length != 4) {
					throw new InvalidSyntaxException("Invalid Paramters!", lnnum);
				}
				
				// Make sure both nodes given are defined
				if(!nodes.containsKey(cmd[1]) || !nodes.containsKey(cmd[3])) {
					throw new InvalidSyntaxException("Node not defined!", lnnum);
				}
				Node from = nodes.get(cmd[1]), to = nodes.get(cmd[3]);
				
				// Set the node to point to the target
				if(cmd[2].equals("yes")) {
					from.setYesNode(to);
				}else if(cmd[2].equals("no")) {
					from.setNoNode(to);
				}else {
					throw new InvalidSyntaxException("Invalid Response!", lnnum);
				}
						
			}else if(cmd[0].equalsIgnoreCase("mkroot")) {
				
				if(cmd.length != 2) {
					throw new InvalidSyntaxException("Invalid Parameters", lnnum);
				}
				
				if(!nodes.containsKey(cmd[1])) {
					throw new InvalidSyntaxException("Node not defined!", lnnum);
				}
				
				root = cmd[1];
				
			}else {
				// Invalid command, throw exception
				throw new InvalidSyntaxException("Invalid Operation!", lnnum);
			}
			
			// Update line counter for next iter
			lnnum++;
		}
		
		if(root == null) {
			throw new InvalidSyntaxException("Root never declared!", 0);
		}
		
		br.close();
		DecisionGraph dg = new DecisionGraph(nodes.get(root));
		nodes.clear();
		return dg;
		
	}
	
}
