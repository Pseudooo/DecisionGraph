package me.Pseudo.DecisionGraph;

class Node {

	private final String question, id;
	// Children could not be final due to loop possibility
	private Node yes = null, no = null;
	private final boolean isEndpoint;
	
	public Node(String question, String id, boolean isEndpoint) {
		this.question = question;
		this.id = id;
		this.isEndpoint = isEndpoint;
		// If this is an endpoint it should have no children
	}
	
	public String question() {
		return this.question;
	}
	
	public boolean isEndpoint() {
		return this.isEndpoint;
	}
	
	public Node getYesNode() {
		return this.yes;
	}
	
	public void setYesNode(Node node) {
		this.yes = node;
	}
	
	public Node getNoNode() {
		return this.no;
	}
	
	public void setNoNode(Node node) {
		this.no = node;
	}
	
	public String ID() {
		return this.id;
	}
	
	@Override
	public boolean equals(Object object) {
		if(object instanceof Node) {
			
			Node other = (Node) object;
			return this.id.equals(other.id);
			
		}else return false;
	}
	
	@Override
	public int hashCode() {
		return this.id.hashCode();
	}
	
}
