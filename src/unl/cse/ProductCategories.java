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

public class ProductCategories {
	private String categoryId;
	private String categoryName;
	

	public ProductCategories(String categoryId, String categoryName) {
		this.setCategoryId(categoryId);
		this.setCategoryName(categoryName);
	}
	
	public String getCategoryId() {
		return this.categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return this.categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	public static List<ProductCategories> categoriesFromSQL(){
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
		String query = "SELECT * FROM ProductCategory";
		List<ProductCategories> categories = new ArrayList<ProductCategories>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while(rs.next()) {
				String categoryId = rs.getString("ProductCategoryCode");
				String categoryName = rs.getString("CategoryName");
				categories.add(new ProductCategories(categoryId, categoryName));
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
		return categories;
	}
	
	
	
	public static List<ProductCategories> scanCategories(){
		Scanner t = null;
		try {
			t = new Scanner(new File("ProductCategories.dat"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
			int numberOfCategories = Integer.parseInt(t.nextLine());
		//stores product categories in array list again
		List<ProductCategories> productCategories = new ArrayList<ProductCategories>(numberOfCategories);
		while(t.hasNext()){
			String line = t.nextLine();
			String tokens[] = line.split(";");
			String catID = tokens[0];
			String catName = tokens[1];
			productCategories.add(new ProductCategories(catID, catName));
		}
		t.close();
		return productCategories;
	}
	
}
