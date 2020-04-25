package me.Pseudo.DecisionGraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import me.Pseudo.DecisionGraph.Exceptions.InvalidSyntaxException;

public class DecisionGraph {
	
	private final HashMap<String, Node> nodes;
	private String root;
	
	private DecisionGraph() {
		this.nodes = new HashMap<String, Node>();
		this.root = null;
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
				switch(cmd[0]) {
				// define <node_id> <is_endpoint> <label...>
				case "define":
				case "def":
					
					// Catch errors
					if(cmd.length < 4)
						throw new InvalidSyntaxException("Insufficient Parameters for define:" + lineCount);
					
					if(dg.nodeExists(cmd[1]))
						throw new InvalidSyntaxException("Node:" + cmd[1] + " has a pre-existing definition:" + lineCount);
					
					boolean endpoint = Boolean.parseBoolean(cmd[2]);
					
					// Parse label from cmd arguments
					StringBuilder lbl_ = new StringBuilder();
					for(int i = 3; i < cmd.length; i++)
						lbl_.append(cmd[i] + (i == cmd.length - 1 ? "" : " "));
					String label = lbl_.toString();
					
					// Construct node & define node
					Node node = new Node(cmd[1], label, endpoint);
					dg.defineNode(node);
					
					break;
				
				// assert <from_id> <to_id> <response>
				case "assert":
				case "asrt":
					
					// Catch errors
					if(cmd.length < 4)
						throw new InvalidSyntaxException("Insufficient Parameters for assert:"+lineCount);
					
					if(!dg.nodeExists(cmd[1]))
						throw new InvalidSyntaxException("Node:"+cmd[1]+":"+lineCount+" is not defined!");
					
					if(!dg.nodeExists(cmd[2]))
						throw new InvalidSyntaxException("Node:"+cmd[2]+":"+lineCount+" is not defined!");
					
					// Parse response from cmd array
					StringBuilder res_ = new StringBuilder();
					for(int i = 3; i < cmd.length; i++)
						res_.append(cmd[i] + (i == cmd.length - 1 ? "" : " "));
					String response = res_.toString();
					
					// Check for pre-existing response
					Node n = dg.getNode(cmd[1]);
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
					
					if(!dg.nodeExists(cmd[1]))
						throw new InvalidSyntaxException("Node:"+cmd[1]+" is not defined:"+lineCount);
					
					dg.setRoot(cmd[1]);
					
					break;
					
				default:
					throw new InvalidSyntaxException("Invalid Operation:"+lineCount);
				
				}
				
			}
			
		}
		
		return dg;
		
	}
	
}