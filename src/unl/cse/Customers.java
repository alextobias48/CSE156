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

public abstract class Customers {
	private String customerId;
	private String customerType;
	private String customerName;
	private Address address;
	private String email;
	
	//Customer class constructor using getters and setters
	public Customers(String customerId, String custType, String customerName,
			Address address, String email) {
		this.setCustomerId(customerId);
		this.setCustomerType(custType);
		this.setCustomerName(customerName);
		this.setAddress(address);
		this.setEmail(email);
	}
	
	public String getCustomerId() {
		return this.customerId;
	}
	public void setCustomerId(String customerId) {
		if(customerId == null){
			this.customerId = "N/A";
		}
		this.customerId = customerId;
	}
	public String getCustomerType() {
		return this.customerType;
	}
	public void setCustomerType(String customerType) {
		if(customerType == null){
			this.customerType = "N/A";
		}
		this.customerType = customerType;
	}
	public String getCustomerName() {
		return this.customerName;
	}
	public void setCustomerName(String customerName) {
		if(customerName == null){
			this.customerName = "N/A";
		}
		this.customerName = customerName;
	}
	public Address getAddress() {
		return this.address;
	}
	public void setAddress(Address address) {
		if(address == null){
			this.address = null;
		}
		this.address = address;
	}
	public String getEmail() {
		return this.email;
	}
	public void setEmail(String email) {
		if(email == null || email.compareTo("") == 0){
			this.email = "N/A";
		}
		this.email = email;
	}
	
	public static CustomerList customersFromSQL(){
		
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
		String query = "SELECT * FROM Customer";
		CustomerList customers = new CustomerList();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while(rs.next()) {
				String customerId = rs.getString("CustomerCode");
				String customerType = rs.getString("CustomerType");
				String customerName = rs.getString("CustomerName");
				String address[] = rs.getString("CustomerAddress").split(",", -1);
					String street = address[0];
					String city = address[1];
					String state = address[2];
					String zip = address[3];
					String country = address[4];
				Address add= new Address(street, city, state, zip, country);
				String email = "N/A";
				Customers c = null;
				if(customerType.compareTo("A") == 0){
					c = new Academic(customerId, customerType, customerName, add, email);
				}else if(customerType.compareTo("C") == 0){
					c = new Corporate(customerId, customerType, customerName, add, email);
				}else{
					c = new Personal(customerId, customerType, customerName, add, email);
				}
				customers.addToStart(c);
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
		return customers;	
	}
	
	public Double getTotal(List<Orders> orders){
		Double total = 0.0;
		Double discount = 0.0;
		Double taxes = 0.0;
		Double fee = 0.0;
		String custId = this.getCustomerId();
		for(Orders o: orders){
			if(o.getCustomer() != null){
			if(o.getCustomer().getCustomerId().compareTo(custId) == 0){
				int j = 0;
				for(Products prod: o.getProductOrdered()){
					total += (prod.getUnitPrice() * o.getQuantityOrdered().get(j)); 
					discount += this.getBulkItemDiscount(prod.getUnitPrice(), o.getQuantityOrdered().get(j));
					j++;
				}
				double temp = total - discount;
				discount += this.getOrderDiscount(temp, o.getProductOrdered().size());
			}
			fee = this.getFee(o.getProductOrdered().size());
			taxes = this.getTaxes(total - discount + fee);
			}
		}
		return total - discount + fee + taxes;
	}

	//scans in customer data
	public static List<Customers> scanCustomers(){
		Scanner s = null; 
		try {
			s = new Scanner(new File("Customers.dat"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		int numberOfCustomers = Integer.parseInt(s.nextLine());
		//stores all customers and their info in Array List
		List<Customers> customers = new ArrayList<Customers>(numberOfCustomers);
		while(s.hasNext()){
			String line = s.nextLine();
			String tokens[] = line.split(";",-1);
			String custId = tokens[0];
			String custType = tokens[1];
			String custName = tokens[2];
			String address[] = tokens[3].split(",", -1);
			//splits address line into following sub-categories
				String street = address[0];
				String city = address[1];
				String state = address[2];
				String zip = address[3];
				String country = address[4];
				Address add = new Address(street, city, state, zip, country);
			String emailAdd = tokens[4];
			//checks customer type and places customer in appropriate subclass
			if(custType.compareTo("P")==0){
				customers.add(new Personal(custId, custType, custName, add, emailAdd));
			}
			else if(custType.compareTo("C")==0){
				customers.add(new Corporate(custId, custType, custName, add, emailAdd));
			}
			else{
				customers.add(new Academic(custId, custType, custName, add, emailAdd));
			}
		}
			
		s.close();
		return customers;
	}
	//abstract functions for finding the discount, fee, and taxes, implementation is given in subclasses
	public abstract Double getBulkItemDiscount(Double subTotal, int numbUnits);
	public abstract Double getOrderDiscount(Double subTotal, int numbOrders);
	public abstract Double getFee(int numbOrders);
	
	public abstract Double getTaxes(Double subTotal);
}
