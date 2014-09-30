package unl.cse;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Orders {
	private String orderID;
	private String date;
	private Customers customer;
	private List<Products> productOrdered;
	private List<Integer> quantityOrdered;
	
	
	public Orders(String orderID, String date, Customers customer,
			List<Products> productOrdered, List<Integer> quantity) {
		this.setOrderID(orderID);
		this.setDate(date);
		this.setCustomer(customer);
		this.setProductOrdered(productOrdered);
		this.setQuantityOrdered(quantity);
	}
	
	public String getOrderID() {
		return this.orderID;
	}
	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}
	public String getDate() {
		return this.date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Customers getCustomer() {
		return this.customer;
	}
	public void setCustomer(Customers customer) {
		this.customer = customer;
	}
	public List<Products> getProductOrdered() {
		return this.productOrdered;
	}
	public void setProductOrdered(List<Products> productOrdered) {
		this.productOrdered = productOrdered;
	}

	public List<Integer> getQuantityOrdered() {
		return this.quantityOrdered;
	}

	public void setQuantityOrdered(List<Integer> quantityOrdered) {
		this.quantityOrdered = quantityOrdered;
	}
	
	public static List<Orders> ordersFromSQL(CustomerList customer, List<Products> productInStock){
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (InstantiationException e) {
			System.out.println("InstantiationException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			System.out.println("IllegalAccessException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		Connection conn = null;

		try {
			conn = DriverManager.getConnection(DatabaseInfo.url, DatabaseInfo.username, DatabaseInfo.password);
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		String query = "SELECT * FROM Customer JOIN `Order` ON Customer.CustomerID = `Order`.CustomerID";
		String queryTwo = "Select * FROM `Order` JOIN ProductQuantity ON `Order`.OrderID = ProductQuantity.OrderID JOIN " +
				"Product ON ProductQuantity.ProductID = Product.ProductID WHERE `Order`.OrderCode = ?";
		List<Orders> orders = new ArrayList<Orders>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		ResultSet rsTwo = null;
		
		
		
		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while(rs.next()) {
				List<Products> productOrdered = new ArrayList<Products>();
				List<Integer> quantityOrdered = new ArrayList<Integer>();
				String orderId = rs.getString("OrderCode");
				String date = rs.getString("OrderDate");
				Customers cust = null;
				for(int i = 1; i<customer.getCount(); i++){
					String cId = customer.getCustomers(i).getCustomerId();
					if(cId.compareTo(rs.getString("CustomerCode")) == 0){
						cust = customer.getCustomers(i);
					}
				}
			try{
				ps = conn.prepareStatement(queryTwo);
				ps.setString(1, orderId);
				rsTwo = ps.executeQuery();
				while(rsTwo.next()){
					Products prod = null;
					for(Products p: productInStock){
						String pId = p.getProductId();
						if(pId.compareTo(rsTwo.getString("ProductCode")) == 0){
							prod = p;
						}
					}
					productOrdered.add(prod);
					quantityOrdered.add(rsTwo.getInt("Quantity"));
				}rsTwo.close();
			}catch (SQLException e) {
				System.out.println("SQLException: ");
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			orders.add(new Orders(orderId, date, cust, productOrdered, quantityOrdered));
		}
		rs.close();
	}catch (SQLException e) {
		System.out.println("SQLException: ");
		e.printStackTrace();
		throw new RuntimeException(e);
	}try {
		if(rs != null && !rs.isClosed())
			rs.close();
		if(ps != null && !ps.isClosed())
			ps.close();
		if(conn != null && !conn.isClosed())
			conn.close();
	} catch (SQLException e) {
		System.out.println("SQLException: ");
		e.printStackTrace();
		throw new RuntimeException(e);
	}
	return orders;
	}
	
	
	
	//program to scan in orders from a dat file
	public static List<Orders> scanOrders(List<Customers> customer, List<Products> productInStock){
		Scanner s = null; 
		try {
			s = new Scanner(new File("Orders.dat"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		int numberOfOrders = Integer.parseInt(s.nextLine());
		List<Orders> orders = new ArrayList<Orders>(numberOfOrders);
		while(s.hasNext()){
			String line = s.nextLine();
			String tokens[] = line.split(";", -1);
			String id = tokens[0];
			String date = tokens[1];
			String custId = tokens[2];
			Customers cust = null;
			for(Customers c: customer){
				String cId = c.getCustomerId();
				if(cId.compareTo(custId) == 0){
					cust = c;
				}
			}
			String prodLine = tokens[3]; 										//reads in string of all productId's/quantities
			String tokProd[] = prodLine.split(",", -1);								// splits into individual orders of "productId : quantity"
			List<Products> products = new ArrayList<Products>(tokProd.length);	//creates products and quantity lists
			List<Integer> quantity = new ArrayList<Integer>(tokProd.length);
			
			for(int i = 0; i<tokProd.length;i++){ 								//for each order
				String temp[] = tokProd[i].split(":", -1); 							//splits into to seperate strings, order and quantity	
				Products tempProd = null;										//creates product object
				for(Products p: productInStock){								//while scanning through all of the products in the system
					String pId = p.getProductId();								//gets string of product in system
					if(pId.compareTo(temp[0]) == 0){							//if it is the same as the pId from the order file 
						tempProd = p;											//sets the product object to that product
					}
				}
				products.add(tempProd);											//adds the product to the products List
				quantity.add(Integer.parseInt(temp[1]));						//adds the quantity to the quantity List
			}
			orders.add(new Orders(id, date, cust, products, quantity));			//creates new orders instance
		}
		s.close();
		return orders;
	}
	
	public static void printSummaryReport(CustomerList customer, List<Orders> orders){
		System.out.println("========== SUMMARY REPORT ========================================");
		System.out.printf("%-35s %-15s %-10s %-10s %-11s %-8s %-12s %-12s\n","Customer", "Type", "# Orders", "Order", "Discounts", "Fees", "Taxes", "Total");
		for(int i = 0; i<customer.getCount(); i++){
			double subTotal = 0;
			double discount = 0.0;
			double fee = 0.0;
			double taxes = 0.0;
			int numbOrders = 0;
			for(Orders o: orders){
				if(!(o.getCustomer() == null)){
					if(customer.getCustomers(i).getCustomerId().compareTo(o.getCustomer().getCustomerId()) == 0){
						numbOrders++;
						int j = 0;
						for(Products prod: o.getProductOrdered()){
							subTotal += (prod.getUnitPrice() * o.getQuantityOrdered().get(j)); 
							discount += customer.getCustomers(i).getBulkItemDiscount(prod.getUnitPrice(), o.getQuantityOrdered().get(j));
							j++;
						}
						double temp = subTotal - discount;
						discount += customer.getCustomers(i).getOrderDiscount(temp, o.getProductOrdered().size());
					}
					fee = customer.getCustomers(i).getFee(o.getProductOrdered().size());
					taxes = customer.getCustomers(i).getTaxes(subTotal - discount + fee);
				}
			}
			System.out.printf("%-35s %-15s %-10d $%-10.2f $%-10.2f $%-7.2f $%-10.2f $%-10.2f\n", customer.getCustomers(i).getCustomerName(), customer.getCustomers(i).getClass().getSimpleName(),
					 numbOrders, subTotal, discount, fee, taxes, (subTotal - discount + fee + taxes));	
		}
	}
	

	public static void printInvoices(List<Orders> orders, CustomerList customers){
		System.out.println("\n\n\n========== CUSTOMER INVOICES =====================================");
		System.out.println("------------------------------------------------------------------");
		Customers lastCust = null;
		for(int i = 1; i< customers.getCount(); i++){
			boolean hasOrder = false;
			
			if(customers.getCustomers(i).getCustomerId() != null && customers.getCustomers(i) != lastCust){
			System.out.printf("%s customer #%s\n", customers.getCustomers(i).getClass().getSimpleName(), customers.getCustomers(i).getCustomerId());
			System.out.printf("%s\n", customers.getCustomers(i).getCustomerName());
			System.out.printf("%s\n", customers.getCustomers(i).getAddress().street);
			System.out.printf("%s %s %s %s\n\n\n", customers.getCustomers(i).getAddress().city, customers.getCustomers(i).getAddress().getState(),customers.getCustomers(i).getAddress().getZip(), customers.getCustomers(i).getAddress().getCountry());
			for(Orders o: orders){
				if(!(o.getCustomer() == null)){
				if(o.getCustomer().getCustomerId().compareTo(customers.getCustomers(i).getCustomerId()) == 0){
					hasOrder = true;
					System.out.printf("Order ID: %s\n", o.getOrderID());
					System.out.printf("Customer: %s (%s)\n", customers.getCustomers(i).getCustomerName(), customers.getCustomers(i).getClass().getSimpleName());
					System.out.printf("Contact: %s\n", customers.getCustomers(i).getEmail());
					System.out.printf("Date: %s\n", o.getDate());
					System.out.printf("%-25s %-15s %-10s %-12s\n", "Item", "Unit Price", "Quantity", "Item Total");
					int k = 0;
					double bulkDiscount = 0.0;
					double fees = customers.getCustomers(i).getFee(o.getProductOrdered().size());
			//retrieves discount from purchasing large quantities of items
					for(Products prod: o.getProductOrdered()){
						bulkDiscount += customers.getCustomers(i).getBulkItemDiscount(prod.getUnitPrice(), o.getQuantityOrdered().get(k));
						k++;
					}
			
					double orderTotal = 0.0;
					int j = 0;
					for(Products p: o.getProductOrdered()){
						double unitPrice = p.getUnitPrice();
						int quantity = o.getQuantityOrdered().get(j);
						double total = unitPrice * quantity;
						orderTotal += total;
						System.out.printf("%-25s $%-15.2f %-10d $%10.2f\n", p.getProductName(), unitPrice, quantity, total);
						j++;
					}
					double temp = orderTotal - bulkDiscount;
					double orderDiscount = customers.getCustomers(i).getOrderDiscount(temp, o.getProductOrdered().size());
					double discount = bulkDiscount + orderDiscount;
					double subTotal = orderTotal - discount + fees;
					double taxes = customers.getCustomers(i).getTaxes(subTotal);
					System.out.printf("%53s -----------\n", "");
					System.out.printf("%53s $%10.2f\n", "Order Total", orderTotal);
					System.out.printf("%53s $%10.2f\n", "Bulk Item Discounts", bulkDiscount);
					System.out.printf("%53s $%10.2f\n", "Per Order Discounts", orderDiscount);
					System.out.printf("%53s $%10.2f\n", "Discounts", discount);
					System.out.printf("%53s $%10.2f\n", "Fees", fees);
					System.out.printf("%53s $%10.2f\n", "Sub-Total", subTotal);
					System.out.printf("%53s $%10.2f\n", "Taxes", taxes);
					System.out.printf("%53s $%10.2f\n", "Total", subTotal + taxes);
					System.out.println("------------------------------------------------------------------\n");
					}
				}	
			}
		}
		if(hasOrder == false){
					System.out.println("***No orders***");
					System.out.println("------------------------------------------------------------------\n");
			}
		lastCust = customers.getCustomers(i);
		}
	}
}
