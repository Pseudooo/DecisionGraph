package me.Pseudo.DecisionGraph;

import java.util.HashMap;

class Node {

	private final String ID, text;
	private final HashMap<String, String> mappings = new HashMap<String, String>();
	
	/**
	 * Create a new node
	 * @param ID to assign
	 */
	public Node(String ID, String text) {
		this.ID = ID;
		this.text = text;
	}
	
	/**
	 * Get the ID of this node
	 * @return Node's ID
	 */
	public String ID() {
		return this.ID;
	}
	
	/**
	 * The text associated with this node
	 * @return text
	 */
	public String text() {
		return this.text;
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
