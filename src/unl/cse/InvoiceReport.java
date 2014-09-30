package unl.cse;
import java.util.List;

public class InvoiceReport {
	public static void main(String args[]){
		
		CustomerList customers = Customers.customersFromSQL();
		List<ProductCategories> productCategories = ProductCategories.categoriesFromSQL();
		List<Products> products = Products.productsFromSQL(productCategories);
		List<Orders> orders = Orders.ordersFromSQL(customers, products);
		CustomerList.sortByName(customers);
		Orders.printSummaryReport(customers, orders);
		CustomerList.sortByTypeID(customers);
		Orders.printSummaryReport(customers, orders);
		CustomerList.sortByTotal(customers, orders);
		Orders.printSummaryReport(customers, orders);
		
		
	}
}
