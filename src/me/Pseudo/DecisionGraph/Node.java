package me.Pseudo.DecisionGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


class Node {

	private final String ID, text;
	private final boolean isEndpoint;
	private final HashMap<String, String> mappings = new HashMap<String, String>();
	
	/**
	 * Create a new node
	 * @param ID to assign
	 */
	public Node(String ID, String text, boolean isEndpoint) {
		this.ID = ID;
		this.text = text;
		this.isEndpoint = isEndpoint;
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
	 * Determine if node is an end point
	 * @return is endpoint
	 */
	public boolean isEndpoint() {
		return this.isEndpoint;
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
	
	/**
	 * Get an ArrayList of responses
	 * @return response list
	 */
	public ArrayList<String> responses() {
		ArrayList<String> arr = new ArrayList<String>();
		
		Iterator<String> itr = this.mappings.keySet().iterator();
		while(itr.hasNext())
			arr.add(itr.next());
		
		return arr;
		
	}
	
	/**
	 * Get the amount of responses
	 * @return response count
	 */
	public int responseCount() {
		return this.mappings.keySet().size();
	}
	
}
