package unl.cse;



public class Node {

	 	private Node next;
	    private Customers item;
	    
	 
	    public Node(Customers item) {
	        this.item = item;
	        this.next = null;
	    }

	    public Customers getCustomers() {
	        return item;
	    }

	    public Node getNext() {
	        return next;
	    }
	    
	    public void setCust(Customers item){
	    	this.item = item;
	    }
	    public void setNext(Node next) {
	        this.next = next;
	    }
}
