package unl.cse;

import java.util.List;


public class CustomerList {
	private int count;
	private Node head;
	
    public void clear() {
    	this.head = null;
    	this.count = 0;
    	return;
    }

    public void addToStart(Customers t) {
    	
    	Node newNode = new Node(t);
    	if(t == null){
    		System.out.println("Invalid Entry, Truck cannot be null");
    		return;
    	}
    	else if(count == 0){
    		head = newNode;
    		count++;
    		return;
    	}
    	else{
    		newNode.setNext(head);
    		head = newNode;
    		count++;
    	}
    	return;
    }

    
    public int getCount(){
    	return this.count;
    }
    
    public void addToEnd(Customers t) {
    	Node node = head;
    	if(t == null){
    		System.out.println("Invalid Entry, Truck cannot be null");
    		return;
    	}
    	else if(head == null){
    		head = new Node(t);
    		count++;
    		return;
    	}
    	else if(head.getNext() == null){
    		head.setNext(new Node(t));
    		count++;
    		return;
    	}
    	else{
    		while(node.getNext() != null){
    			node = node.getNext();
    			if(node.getNext() == null){
    				node.setNext(new Node(t));
    				count++;
    				return;
    			}
    		}
    	}
    }
    
    public void insertAtIndex(Customers c, int index) {

		if(index < 0 || index > this.getCount()) {
			throw new IllegalArgumentException("index out of bounds");
		}
		
		if(index == this.getCount()) {
			addToEnd(c);
			return;
		} else if(index == 0) {
			this.addToStart(c);
			return;
		}
		
		//find the node with index, index-1
		Node theNode = this.getNode(index-1);
		Node newNode = new Node(c);
		newNode.setNext(theNode.getNext());
		theNode.setNext(newNode);
		
	}

    
    
    public void remove(int position) {
    	Node current = head;
    	Node previous = null;
    	
    	if(position > count){
    		System.out.println("Invalid Entry: position does not exist");
    		return;
    	}
    	else if(position == 0){
    		head = head.getNext();
    		return;
    	}
    	
    	for(int i = 0; i < position; i++){
    		previous = current;
    		current = current.getNext();
    	}
    	previous.setNext(current.getNext());
    	current.setNext(null);
    	count--;
    	return;
    }
    
    
    
    public Node getNode(int position) {
    	Node current = head;
    	if(position > count){
    		System.out.println("Invalid Entry: position does not exist");
    	}
    	else if(position == 0){
    		return current;
    	}
    	
    	for(int i = 0; i < position; i++){
    		current = current.getNext();
    	}
    	
    	return current;
    }
    
    public Customers getCustomers(int position) {
    	if(position > count){
    		System.out.println("Invalid Entry: position does not exist");
    	}
    	Node t = getNode(position);
    	
    	return t.getCustomers();
    }

    public void print() {
    	Node t = head;
    	while(t != null){
    		System.out.println(t.getCustomers().getCustomerName());
    		t = t.getNext();
    	}
    	return;
    }
   
	public static void sortByTypeID(CustomerList list){ 
    	Customers auxCust;
    	for(int i = 1; i <= list.getCount()-1; i++){
    		for(int j = 0; j < list.getCount() - i; j++){
    			if(compareByTypeID(list.getCustomers(j), list.getCustomers(j+1)) > 0){
    				auxCust = list.getCustomers(j);
    				list.getNode(j).setCust(list.getCustomers(j+1));
    				list.getNode(j+1).setCust(auxCust);
    			}
    		}
    	}
	}
	
	public static void sortByName(CustomerList list){ 
    	Customers auxCust;
    	for(int i = 1; i <= list.getCount()-1; i++){
    		for(int j = 0; j < list.getCount() - i; j++){
    			if(compareByName(list.getCustomers(j), list.getCustomers(j+1)) > 0){
    				auxCust = list.getCustomers(j);
    				list.getNode(j).setCust(list.getCustomers(j+1));
    				list.getNode(j+1).setCust(auxCust);
    			}
    		}
    	}
	}
	
	public static void sortByTotal(CustomerList list, List<Orders> orders){ 
    	Customers auxCust;
    	for(int i = 1; i <= list.getCount()-1; i++){
    		for(int j = 0; j < list.getCount() - i; j++){
    			if(compareByTotal(list.getCustomers(j), list.getCustomers(j+1), orders) < 0.0){
    				auxCust = list.getCustomers(j);
    				list.getNode(j).setCust(list.getCustomers(j+1));
    				list.getNode(j+1).setCust(auxCust);
    			}
    		}
    	}
	}
    
    public static int compareByName(Customers a, Customers b){
		return a.getCustomerName().compareTo(b.getCustomerName());
	}
    
    public static int compareByTypeID(Customers a, Customers b){
		if(a.getCustomerType().compareTo(b.getCustomerType())==0){
			return a.getCustomerId().compareTo(b.getCustomerId());
		}
		else{
			return a.getCustomerType().compareTo(b.getCustomerType());
		}
	}
    
    public static double compareByTotal(Customers a, Customers b, List<Orders> orders){
    	return a.getTotal(orders) - b.getTotal(orders);	
	}
    
}
