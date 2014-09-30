package unl.cse;

public class Personal extends Customers{

	public Personal(String customerId, String custType, String customerName,
			Address address, String email) {
		super(customerId, custType, customerName, address, email);
	}
	
	public Double getBulkItemDiscount(Double subTotal, int numbUnits){
		double discount = 0.0;
		subTotal *= numbUnits;
		if(numbUnits >= 10){
			discount += subTotal * .1;
		}
		return discount;
	}
	
	public Double getOrderDiscount(Double subTotal, int numbOrders){
		return 0.0;
	}
	
	public Double getFee(int numbOrders){
		return 0.0;
	}
	
	public Double getTaxes(Double subTotal){
		return subTotal * .07;
	}

}
