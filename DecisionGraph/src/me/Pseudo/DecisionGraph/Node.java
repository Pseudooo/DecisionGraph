package me.Pseudo.DecisionGraph;

import java.util.HashMap;

class Node {

	private final String ID;
	private final HashMap<String, String> mappings = new HashMap<String, String>();
	
	/**
	 * Create a new node
	 * @param ID to assign
	 */
	public Node(String ID) {
		this.ID = ID;
	}
	
	/**
	 * Get the ID of this node
	 * @return Node's ID
	 */
	public String ID() {
		return this.ID;
	}
	
	/**
	 * Assign a given response to another node
	 * @param response Trigger response
	 * @param nodeID Node to point too
	 */
	public void assignResponse(String response, String nodeID) {
		this.mappings.put(response, nodeID);
	}
	
	/**
	 * Get the node for a given response
	 * @param response 
	 * @return node ID
	 */
	public String getResponseResult(String response) {
		return this.mappings.get(response);
	}
	
}
