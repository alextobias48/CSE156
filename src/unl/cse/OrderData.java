package unl.cse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderData {

	/**
	 * This method removes all product category records from the database as well
	 * as any referencing records.
	 */
	public static void removeAllProductCategories() {
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
		
		String query = "DELETE FROM Product WHERE CategoryID > 0";
		String queryTwo = "DELETE FROM ProductCategory WHERE CategoryID > 0";
		PreparedStatement ps = null;

		try {
			ps = conn.prepareStatement(query);
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}try {
			ps = conn.prepareStatement(queryTwo);
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		try {
			if(ps != null && !ps.isClosed())
				ps.close();
			if(conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * This method creates a new product category record in the database
	 * with the given field values.
	 * @param productCategoryId
	 * @param name
	 */
	public static void addProductCategory(String productCategoryId, String name) {
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
		
		String query = "INSERT INTO ProductCategory (CategoryName, ProductCategoryCode) VALUES ( ?, ?);";
		PreparedStatement ps = null;

		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, name);
			ps.setString(2, productCategoryId);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		try {
			if(ps != null && !ps.isClosed())
				ps.close();
			if(conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}

	/**
	 * This method removes all product records from the database as well
	 * as any referencing records.
	 */
	public static void removeAllProducts() {
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
		
		String query = "DELETE FROM Product WHERE Product.ProductID > 0";
		PreparedStatement ps = null;

		try {
			ps = conn.prepareStatement(query);
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		try {
			if(ps != null && !ps.isClosed())
				ps.close();
			if(conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * This method creates a new product record in the database
	 * with the given field values.
	 * @param productId
	 * @param name
	 * @param description
	 * @param unitPrice
	 * @param productCategoryId
	 */
	public static void addProduct(String productId, String name, String description, Double unitPrice, String productCategoryId) {
		int catId = 0;
		
		if(productCategoryId == null){
			return;
		}
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
		String queryOne = "SELECT * FROM ProductCategory WHERE ProductCategoryCode = '" + productCategoryId + "'";
		String query = "INSERT INTO Product (ProductCode, ProductName, ProductDescription, ProductPPU, CategoryID) VALUES ( ?, ?, ?, ?, ?)";
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement(queryOne);
			rs = ps.executeQuery();
			if(rs.next()){
				catId = rs.getInt("CategoryID");
			}
			ps.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, productId);
			ps.setString(2, name);
			ps.setString(3, description);
			ps.setDouble(4, unitPrice);
			ps.setInt(5, catId);
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		try {
			if(ps != null && !ps.isClosed())
				ps.close();
			if(conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * This method removes all customer records from the database as well
	 * as any referencing records.
	 */
	public static void removeAllCustomers() {
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
		
		String query = "DELETE FROM Email WHERE EmailID > 0";
		String queryOne = "DELETE FROM ProductQuantity WHERE OrderID > 0";
		String queryTwo = "DELETE FROM `Order` WHERE OrderID > 0";
		String queryThree = "DELETE FROM Customer WHERE CustomerID > 0";
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement(query);
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		try {
			ps = conn.prepareStatement(queryOne);
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		try {
			ps = conn.prepareStatement(queryTwo);
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		try {
			ps = conn.prepareStatement(queryThree);
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		try {
			if(ps != null && !ps.isClosed())
				ps.close();
			if(conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	/**
	 * This method removes a particular customer record from the database, 
	 * identified by the given <code>customerId</code> as well
	 * as any referencing records.
	 * @param customerId
	 */
	public static void removeCustomer(String customerId) {
		int custId = Integer.parseInt(customerId);
		int ordId = 0;
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
		String query = "SELECT `Order`.OrderID FROM Customer JOIN `Order` ON Customer.CustomerID = `Order`.CustomerID WHERE Customer.CustomerID = " + custId;
		String queryOne = "DELETE FROM Email WHERE Email.CustomerID = " + custId;
		String quer = "DELETE FROM ProductQuantity WHERE ProductQuantity.OrderID = " + ordId;
		String queryTwo = "DELETE FROM Order WHERE Order.CustomerID = " + custId;
		String queryThree = "DELETE FROM Customer WHERE Customer.CustomerID = " + custId;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			if(rs.next()){
				ordId = rs.getInt("OrderID");
			}
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		try {
			ps = conn.prepareStatement(queryOne);
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		try {
			ps = conn.prepareStatement(quer);
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		try {
			ps = conn.prepareStatement(queryTwo);
			 ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		try {
			ps = conn.prepareStatement(queryThree);
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		try {
			if(ps != null && !ps.isClosed())
				ps.close();
			if(conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}
	/**
	 * This method adds a <i>new</i> customer record to the database with the
	 * given field values.
	 * 
	 * For the name parameters: provide either firstName and lastName (Personal Customers) or
	 * just the "name" (for Corporate/Academic customers); provide <code>null</code>
	 * for the unused parameter(s)
	 * @param customerId
	 * @param type
	 * @param address
	 * @param city
	 * @param state
	 * @param zip
	 * @param country
	 */
	public static void addCustomer(String customerId, String type,  String firstName, String lastName, String name, String address, String city, String state, String zip, String country) {
		String wholeName;
		String fullAddress = address + ", " + city + ", " + state + ", " + zip + ", " + country;
		
		if(type.compareTo("P")==0){
			wholeName = lastName + ", " + firstName;
		}
		
		else{
			wholeName = name;
		}
		
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
		
		String query = "INSERT INTO Customer (CustomerCode, CustomerType, CustomerName, CustomerAddress) VALUES ( ?, ?, ?, ?)";
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, customerId);
			ps.setString(2, type);
			ps.setString(3, wholeName);
			ps.setString(4, fullAddress);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		try {
			if(ps != null && !ps.isClosed())
				ps.close();
			if(conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	/**
	 * This method adds an email record associated with the given customer.
	 * @param customerId
	 * @param emailAddress
	 */
	public static void addCustomerEmail(String customerId, String emailAddress) {
		int custId = 0;
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
		String queryOne = "SELECT * FROM Customer WHERE Customer.CustomerCode = '" + customerId + "'";
		String query = "INSERT INTO Email (CustomerID, EmailAddress) VALUES ( ?, ?)";
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement(queryOne);
			rs = ps.executeQuery();
			if(rs.next()){
				custId = rs.getInt("CustomerID");
			}
			rs.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, custId);
			ps.setString(2, emailAddress);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		try {
			if(ps != null && !ps.isClosed())
				ps.close();
			if(conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * This method removes a particular order, associated with the given <code>orderId</code>
	 * record from the database as well as any referencing records.
	 * @param orderId
	 */
	public static void removeOrder(String orderId) {
		int ordId = Integer.parseInt(orderId);
		
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
		String queryOne = "DELETE FROM ProductQuantity WHERE ProductQuantity.OrderID = " + ordId;
		String queryTwo = "DELETE FROM Order WHERE Order.OrderID = " + ordId;
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement(queryOne);
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		try {
			ps = conn.prepareStatement(queryTwo);
			ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		try {
			if(ps != null && !ps.isClosed())
				ps.close();
			if(conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	/**
	 * 
	 * @param orderId
	 * @param date
	 * @param customerId
	 */
	public static void addOrder(String orderId, String date, String customerId) {
		int custId = 0;
		
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
		
		String queryOne = "SELECT * FROM Customer WHERE CustomerCode = '" + customerId + "'";
		String query = "INSERT INTO `Order` (OrderCode, OrderDate, CustomerID) VALUES (?, ?, ?)";
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement(queryOne);
			rs = ps.executeQuery();
			if(rs.next()){
				custId = rs.getInt("CustomerID");
			}
			rs.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, orderId);
			ps.setString(2, date);
			ps.setInt(3, custId);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		try {
			if(ps != null && !rs.isClosed())
				ps.close();
			if(rs != null && !rs.isClosed())
				rs.close();
			if(conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	/**
	 * Adds a record to the database that associates a particular product/quantity with a 
	 * particular order  
	 * @param orderId
	 * @param productId
	 * @param quantity
	 */
	public static void addOrderProduct(String orderId, String productId, Integer quantity) {
		int ordId = 0;
		int prodId = 0;
		
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
		String queryOne = "SELECT * FROM `Order` WHERE `Order`.OrderCode = '" + orderId + "'";
		String queryTwo = "SELECT * FROM Product WHERE Product.ProductCode = '" + productId + "'";
		String query = "INSERT INTO ProductQuantity (OrderID, ProductID, Quantity) VALUES (?, ?, ?)";
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement(queryOne);
			rs = ps.executeQuery();
			if(rs.next()){
				ordId = rs.getInt("OrderID");
			}
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		try {
			ps = conn.prepareStatement(queryTwo);
			rs = ps.executeQuery();
			if(rs.next()){
				prodId = rs.getInt("ProductID");
			}
			rs.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, ordId);
			ps.setInt(2, prodId);
			ps.setInt(3, quantity);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		try {
			if(ps != null && !ps.isClosed())
				ps.close();
			if(conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			System.out.println("SQLException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
