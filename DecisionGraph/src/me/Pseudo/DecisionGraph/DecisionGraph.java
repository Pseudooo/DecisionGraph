package me.Pseudo.DecisionGraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import me.Pseudo.DecisionGraph.Exceptions.FileUnreadableException;
import me.Pseudo.DecisionGraph.Exceptions.InvalidSyntaxException;
import me.Pseudo.DecisionGraph.Exceptions.MissingChildrenException;
import me.Pseudo.DecisionGraph.Exceptions.NoDeclaredRootNodeException;

public class DecisionGraph {

	private final Node root;
	
	private Node cur;
	private ArrayList<String> path = null;
	
	public DecisionGraph(File f) throws Exception, FileNotFoundException, FileUnreadableException,
	NoDeclaredRootNodeException, MissingChildrenException {
		
		// File exceptions
		if(!f.exists()) {
			throw new FileNotFoundException();
		}
		if(!f.canRead()) {
			throw new FileUnreadableException();
		}
		
		// Setup items needed for constructing map
		HashMap<String, Node> nodes = new HashMap<String, Node>();
		String tmproot = null;
		
		// Read from given file
		BufferedReader br = new BufferedReader(new FileReader(f));
		String ln;
		int line = 0;
		
		// ********** START OF FILE READING
		while((ln = br.readLine()) != null) {
			line++; // Increment line count
			
			if(ln.startsWith("#") || ln.equals("")) {
				// Ignore comments and whitespace
				continue;
			}
			
			// Break up as command array
			String[] cmd = ln.split(" ");
			
			/* cmd[0] is the command keyword of which there are three:
			 * define, assert and mkroot. Only these three are valid
			*/
			if(cmd[0].equalsIgnoreCase("define")) {
				
				// define <node_id> <is_endpoint> <question...>
				// Define needs atleast 3 arguments + keyword
				if(cmd.length < 4) {
					throw new InvalidSyntaxException("Too few arguments for define", line);
				}
				
				// Check node isn't already defined
				if(nodes.containsKey(cmd[1])) {
					throw new InvalidSyntaxException("Node already defined", line);
				}
				
				// Parse boolean
				boolean isEndpoint = Boolean.parseBoolean(cmd[2]);
				
				// Parse question from remaining parameters
				String question = "";
				for(int i = 3; i < cmd.length; i++) {
					//                          Handle space at end
					question += cmd[i] + (i == cmd.length - 1 ? "" : " ");
				}
				
				// Define node and place in hashmap
				Node node = new Node(question, cmd[1], isEndpoint);
				nodes.put(cmd[1], node);
				
			}else if(cmd[0].equalsIgnoreCase("assert")) {
				
				// assert <from_node> <Yes|No> <to_node>
				// Assert needs exactly 3 arguments + keyword
				if(cmd.length != 4) {
					throw new InvalidSyntaxException("Invalid Argument Count", line);
				}
				
				// Check both nodes are defined
				if(!nodes.containsKey(cmd[1]) || !nodes.containsKey(cmd[3])) {
					throw new InvalidSyntaxException("Node not defined", line);
				}
				
				// Nodes cannot be self-referential
				if(cmd[1].equals(cmd[3])) {
					throw new InvalidSyntaxException("Self-Referential Nodes Are Disallowed", line);
				}
				
				Node from = nodes.get(cmd[1]), to = nodes.get(cmd[3]);
				
				// Endpoint nodes cannot have children
				if(from.isEndpoint()) {
					throw new InvalidSyntaxException("Endpoint Nodes Cannot Have Children", line);
				}
				
				// Potential responses are yes||no
				if(cmd[2].equalsIgnoreCase("yes")) {
					from.setYesNode(to);
				}else if(cmd[2].equalsIgnoreCase("no")) {
					from.setNoNode(to);
				}else {
					// Invalid response, raise syntax error
					throw new InvalidSyntaxException("Invalid Response", line);
				}
				
			}else if(cmd[0].equalsIgnoreCase("mkroot")) {
				
				// mkroot <node_id>
				// Exactly 1 argument + keyword
				if(cmd.length != 2) {
					throw new InvalidSyntaxException("Invalid Argument Count", line);
				}
				
				// Check the node is defined
				if(!nodes.containsKey(cmd[1])) {
					throw new InvalidSyntaxException("Node Not Defined", line);
				}
				
				// Place string ID in tmproot
				tmproot = cmd[1];
				
			}else {
				// All other possibilities are invalid
				throw new InvalidSyntaxException("Invalid Operation!", line);
			}
			
		}
		// ****** END OF FILE READING
		
		// Check that a root has been declared
		if(tmproot == null) {
			throw new NoDeclaredRootNodeException();
		}
		
		// Check all non endpoint nodes have two children
		for(Node n : nodes.values()) {
			
			// Skip endpoint nodes
			if(n.isEndpoint()) continue;
			
			if(n.getYesNode() == null || n.getNoNode() == null) {
				// Throw exception if children not declared
				throw new MissingChildrenException(n.ID());
			}
			
		}
		
		// Validation complete, now assign to this instance
		
		this.root = nodes.get(tmproot);
		nodes.clear();
		br.close();
		
		// Not traversing, so current is null
		this.cur = null;
	}
	
	/**
	 * Setup to start traversing the graph
	 */
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
	
}
