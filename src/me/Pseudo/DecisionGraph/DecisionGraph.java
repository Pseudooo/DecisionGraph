package me.Pseudo.DecisionGraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import me.Pseudo.DecisionGraph.Exceptions.InvalidSyntaxException;

public class DecisionGraph {
	
	private final HashMap<String, Node> nodes;
	private String root = null;
	
	// * * * * * * * * * EXTERNAL FUNCTIONS
	
	public String cur = null;
	
	/**
	 * Function to determine if the graph is currently undergoing traversal
	 * @return is traversing
	 */
	public boolean isTraversing() {
		return cur == null;
	}
	
	/**
	 * Start traversal of the graph at a defined node
	 * Will not initialize if given node does not exist
	 * @param startNode node to start at
	 * @return true if stateNode was valid
	 */
	public boolean initTraversal(String startNode) {
		if(nodes.containsKey(startNode)) {
			cur = startNode;
			return true;
		}
		return false;
	}
	
	/**
	 * Start traversal of the graph at the root
	 * Will not initialize if root is not set
	 */
	public void initTraversal() {
		initTraversal(root);
	}
	
	/**
	 * Determine if the current node is an endpoint to the graph in traversal
	 * If the graph is not currently traversing will return false
	 * @return is endpoint
	 */
	public boolean isCurrentEndpoint() {
		return cur == null ? false : nodes.get(cur).isEndpoint();
	}
	
	/**
	 * Get the label of the current node in traversal
	 * If the graph is not currently traversing will return null
	 * @return
	 */
	public String getCurrentLabel() {
		return cur == null ? null : nodes.get(cur).text();
	}
	
	/**
	 * Get the current responses in traversal
	 * If the graph is not currently traversing or is at an endpoint will return null
	 * @return List of responses or null
	 */
	public List<String> getCurrentResponses() {
		return isCurrentEndpoint() ? null : nodes.get(cur).responses();
	}
	
	/**
	 * Give a response to the current node
	 * @param response to be given
	 * @return true if response was valid false otherwise
	 */
	public boolean giveResponse(String response) {
		
		// Cant respond to non-traversing or endpoint
		if(isCurrentEndpoint()) return false;
		
		// Must be a registered response
		List<String> l = getCurrentResponses();
		if(!l.contains(response)) return false;
		
		// "shift" node
		cur = nodes.get(cur).getResponseResult(response);
		return true; // Success
		
	}
	
	/**
	 * Terminate the traversal of a graph
	 */
	public void terminateTraversal() {
		cur = null;
	}
	
	// * * * * * * * * * INTERNAL FUNCTIONS 
	
	protected DecisionGraph() {
		this.nodes = new HashMap<String, Node>();
		this.root = null;
	}
	
	protected String getRoot() {
		return root;
	}
	
	protected boolean setRoot(String id) {
		boolean s = this.nodes.containsKey(id);
		if(s) root = id;
		return s;
	}
	
	protected boolean nodeExists(String id) {
		return this.nodes.containsKey(id);
	}
	
	protected Node getNode(String id) {
		return this.nodes.get(id);
	}
	
	protected void defineNode(Node node) {
		this.nodes.put(node.ID(), node);
	}
	
	protected void exec(String[] cmd, int lineCount) throws InvalidSyntaxException {
		
		switch(cmd[0]) {
		// define <node_id> <is_endpoint> <label...>
		case "define":
		case "def":
			
			// Catch errors
			if(cmd.length < 4)
				throw new InvalidSyntaxException("Insufficient Parameters for define:" + lineCount);
			
			if(nodeExists(cmd[1]))
				throw new InvalidSyntaxException("Node:" + cmd[1] + " has a pre-existing definition:" + lineCount);
			
			boolean endpoint = Boolean.parseBoolean(cmd[2]);
			
			// Parse label from cmd arguments
			StringBuilder lbl_ = new StringBuilder();
			for(int i = 3; i < cmd.length; i++)
				lbl_.append(cmd[i] + (i == cmd.length - 1 ? "" : " "));
			String label = lbl_.toString();
			
			// Construct node & define node
			Node node = new Node(cmd[1], label, endpoint);
			defineNode(node);
			
			break;
		
		// assert <from_id> <to_id> <response>
		case "assert":
		case "asrt":
			
			// Catch errors
			if(cmd.length < 4)
				throw new InvalidSyntaxException("Insufficient Parameters for assert:"+lineCount);
			
			if(!nodeExists(cmd[1]))
				throw new InvalidSyntaxException("Node:"+cmd[1]+":"+lineCount+" is not defined!");
			
			if(!nodeExists(cmd[2]))
				throw new InvalidSyntaxException("Node:"+cmd[2]+":"+lineCount+" is not defined!");
			
			// Parse response from cmd array
			StringBuilder res_ = new StringBuilder();
			for(int i = 3; i < cmd.length; i++)
				res_.append(cmd[i] + (i == cmd.length - 1 ? "" : " "));
			String response = res_.toString();
			
			// Check for pre-existing response
			Node n = getNode(cmd[1]);
			if(n.responses().contains(response))
				throw new InvalidSyntaxException("Node:"+cmd[1]+":"+lineCount+" already has a route: "+response);
			
			n.assignResponse(response, cmd[2]);
			
			break;
			
		// makeroot <node_id>
		case "makeroot":
		case "mkroot":
		case "mkrt":
			
			if(cmd.length != 2)
				throw new InvalidSyntaxException("Invalid Parameters for makeroot:"+lineCount);
			
			if(!nodeExists(cmd[1]))
				throw new InvalidSyntaxException("Node:"+cmd[1]+" is not defined:"+lineCount);
			
			setRoot(cmd[1]);
			
			break;
			
		default:
			throw new InvalidSyntaxException("Invalid Operation:"+lineCount);
		
		}
		
	}
	
	/**
	 * Method to construct a DecisionGraph given a file containing a dg script
	 * @param f the file to (attempt) reading from
	 * @return The DecisionGraph instance
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws InvalidSyntaxException
	 */
	public static DecisionGraph fromFile(File f) 
	throws FileNotFoundException, IOException, InvalidSyntaxException {
		
		// Check file is ok for reading
		if(!f.exists())
			throw new FileNotFoundException("Provided file doesn't exist!");
		
		if(!f.canRead())
			throw new IOException("Provided file is unreadable!");
		
		DecisionGraph dg = new DecisionGraph();
		
		// Prepare to read from file
		String line;
		int lineCount = 0;
		
		// BufferedReader implements AutoCloseable
		try(BufferedReader br = new BufferedReader(new FileReader(f))) {
			
			// Read from file 
			while((line = br.readLine()) != null) {
				lineCount++;
				
				// Skip blank lines and comments
				if(line.startsWith("#") || line.isEmpty())
					continue;
				
				// Segment command and exec
				String[] cmd = line.split(" ");
				dg.exec(cmd, lineCount);
				
			}
			
		}
		
		return dg;
		
	}
	
}