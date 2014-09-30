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

public class Products {

	private String productId;
	private String productName;
	private String productDescription;
	private Double unitPrice;
	private ProductCategories category;
	
	//products constructor using getters and setters
	public Products(String productId, String productName, String productDescription, Double unitPrice, ProductCategories category) {
		this.setProductId(productId);
		this.setProductName(productName);
		this.setProductDescription(productDescription);
		this.setUnitPrice(unitPrice);
		this.setCategory(category);
	}
	
	public String getProductId() {
		return this.productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return this.productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductDescription() {
		return this.productDescription;
	}
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	public Double getUnitPrice() {
		return this.unitPrice;
	}
	public void setUnitPrice(Double unitPrice) {
		if(unitPrice == null){
			this.unitPrice = 0.0;
		}
		this.unitPrice = unitPrice;
	}
	public ProductCategories getCategory() {
		return this.category;
	}
	public void setCategory(ProductCategories category) {
		this.category = category;
	}
	
	public static List<Products> productsFromSQL(List<ProductCategories> categories){
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
		String query = "SELECT * FROM Product JOIN ProductCategory ON Product.CategoryID = ProductCategory.CategoryID";
		List<Products> products = new ArrayList<Products>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while(rs.next()) {
				String productId = rs.getString("ProductCode");
				String productName = rs.getString("ProductName");
				String productDescription = rs.getString("ProductDescription");
				Double unitPrice = rs.getDouble("ProductPPU");
				ProductCategories cat = null;
				//loop that searches for product category name give the category ID
				for(ProductCategories p: categories){
					String id = p.getCategoryId();
					if(id.compareTo(rs.getString("ProductCategoryCode")) == 0){
						cat = p;
					}
				}
				products.add(new Products(productId, productName, productDescription, unitPrice, cat));
			}rs.close();
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
		return products;
	}
	
	
	
	public static List<Products> scanProducts(List<ProductCategories> categories){
		Scanner u = null;
		try {
			u = new Scanner(new File("Products.dat"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
			int numberOfProducts = Integer.parseInt(u.nextLine());
		List<Products> products = new ArrayList<Products>(numberOfProducts);
		while(u.hasNext()){
			String line = u.nextLine();
			String tokens[] = line.split(";", -1);
			String prodID = tokens[0];
			String prodName = tokens[1];
			String prodDesc = tokens[2];
			double price = Double.parseDouble(tokens[3]);
			ProductCategories cat = null;
			//loop that searches for product category name give the category ID
			for(ProductCategories p: categories){
				String id = p.getCategoryId();
				if(id.compareTo(tokens[4]) == 0){
					cat = p;
				}
			}
			products.add(new Products(prodID, prodName, prodDesc, price, cat));
		}
		u.close();
		return products;
		}
	}

